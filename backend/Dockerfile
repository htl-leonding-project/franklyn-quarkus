FROM maven

WORKDIR /app

# Copy Maven Wrapper files

# Copy the rest of the application files
COPY lib lib
COPY src src
COPY pom.xml pom.xml


ENV DB_URL="jdbc:postgresql://localhost:5432/db" \
    POSTGRES_USERNAME="app" \
    POSTGRES_PASSWORD="app"
# Build the application
RUN ["mvn", "clean", "package", "-DskipTests"]

# Run the application
CMD ["java", "-jar", "./target/quarkus-app/quarkus-run.jar"]
