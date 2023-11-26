FROM eclipse-temurin:17-jre

RUN mkdir -p /opt/application
COPY *quarkus-run.jar /opt/application/quarkus-run.jar
CMD [ "java", "-jar", "quarkus-run.jar" ]
WORKDIR /opt/application