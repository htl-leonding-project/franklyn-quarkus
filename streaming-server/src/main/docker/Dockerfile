FROM eclipse-temurin:17-jre

RUN mkdir -p /opt/application
COPY *-runner.jar /opt/application/quarkus-run.jar
CMD [ "java", "-jar", "quarkus-run.jar" ]
WORKDIR /opt/application