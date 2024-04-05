FROM openjdk:17-jdk-alpine

EXPOSE 8080

COPY target/cloud_storage-0.0.1-SNAPSHOT.jar cloud_storage.jar

ADD src/main/resources/applicationyaml src/main/resources/application.properties

CMD ["java", "-jar", "cloud_storage.jar"]