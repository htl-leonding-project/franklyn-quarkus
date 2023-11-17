FROM openjdk:17-alpine

WORKDIR /app

# Copy Maven Wrapper files
COPY .mvn .mvn

# Copy the rest of the application files
COPY lib lib
COPY src src
COPY pom.xml pom.xml
COPY mvnw mvnw

# Build the application
RUN ["chmod", "+x", "mvnw"]
RUN ["./mvnw", "clean", "package", "-DskipTests"]

# Run the application
CMD ["java", "-jar", "./target/quarkus-app/quarkus-run.jar"]
