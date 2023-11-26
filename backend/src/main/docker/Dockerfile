FROM eclipse-temurin:17-jre

RUN mkdir -p /opt/application
COPY ../../../target/deploy/franklyn-application-server-image.jar /opt/application/backend.jar
CMD [ "java", "-jar", "backend.jar" ]
WORKDIR /opt/application