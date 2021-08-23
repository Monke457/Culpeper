FROM openjdk:15-jdk-alpine
EXPOSE 8080
COPY target/Culpeper-1.0-SNAPSHOT.jar .
ENTRYPOINT ["java","-jar","Culpeper-1.0-SNAPSHOT.jar"]