FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu

COPY build/libs/*SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]

