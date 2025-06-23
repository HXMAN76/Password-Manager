# Use Eclipse Temurin (AdoptOpenJDK) as it's well-maintained and lightweight
FROM eclipse-temurin:11-jdk-focal

# Install X11 libraries and utilities needed for GUI apps
RUN apt-get update && apt-get install -y \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libfontconfig1 \
    netcat \
    iputils-ping \
    dnsutils \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy the JAR files we need
COPY target/password-manager-1.0-SNAPSHOT-jar-with-dependencies.jar /app/password-manager.jar
COPY src/main/resources/images /app/images
COPY src/main/resources/config.properties /app/config.properties

# Set environment variables
ENV DB_URL=jdbc:mysql://mysql:3306/Password_Manager
ENV DB_USERNAME=root
ENV DB_PASSWORD=password

# Create startup script
RUN echo '#!/bin/bash\n\
echo "Checking network connection..."\n\
cat /etc/hosts\n\
echo "DNS lookup for mysql:"\n\
nslookup mysql || echo "DNS lookup failed. This is expected if using host network mode."\n\
\n\
echo "Using DATABASE_HOST: ${DATABASE_HOST:-mysql}"\n\
echo "Using DATABASE_PORT: ${DATABASE_PORT:-3306}"\n\
\n\
echo "Waiting for MySQL to become available..."\n\
RETRIES=30\n\
HOST=${DATABASE_HOST:-mysql}\n\
PORT=${DATABASE_PORT:-3306}\n\
\n\
# More robust MySQL connection check using netcat\n\
while [ $RETRIES -gt 0 ]; do\n\
  if nc -z -w1 $HOST $PORT; then\n\
    echo "MySQL is available at $HOST:$PORT"\n\
    break\n\
  fi\n\
  echo "Waiting for MySQL at $HOST:$PORT..."\n\
  sleep 5\n\
  RETRIES=$((RETRIES-1))\n\
done\n\
\n\
if [ $RETRIES -eq 0 ]; then\n\
  echo "WARNING: Could not confirm MySQL is up at $HOST:$PORT. Trying to start the application anyway..."\n\
fi\n\
\n\
# Additional 5-second wait to ensure MySQL is fully ready\n\
sleep 5\n\
\n\
# List resources to ensure they are available\n\
echo "Checking resources:"\n\
ls -la /app\n\
\n\
echo "Starting Password Manager application..."\n\
# Form the database URL using environment variables\n\
HOST=${DATABASE_HOST:-mysql}\n\
PORT=${DATABASE_PORT:-3306}\n\
DB=${DATABASE_NAME:-Password_Manager}\n\
\n\
# Override the configured URL if needed\n\
if [ -n "$DATABASE_HOST" ]; then\n\
  export DB_URL="jdbc:mysql://$HOST:$PORT/$DB?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"\n\
  echo "Using database URL: $DB_URL"\n\
fi\n\
\n\
java -Ddb.url=${DB_URL} -Ddb.username=${DB_USERNAME} -Ddb.password=${DB_PASSWORD} -Djava.awt.headless=false -jar /app/password-manager.jar\n' > /app/start.sh && \
chmod +x /app/start.sh

# Set the entry point
ENTRYPOINT ["/app/start.sh"]
