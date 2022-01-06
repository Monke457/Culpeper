FROM openjdk:15-jdk-alpine
EXPOSE 8080
VOLUME /tmp
WORKDIR /code

RUN apt-get update
RUN apt-get install -y maven

ADD pom.xml /code/pom.xml
RUN ["mvn", "dependency:resolve"]
RUN ["mvn", "verify"]

# Adding source, compile and package into a fat jar
ADD src /code/src
RUN ["mvn", "package"]

#COPY target/Culpeper-1.0-SNAPSHOT.jar .
#ENTRYPOINT ["java","-jar","Culpeper-1.0-SNAPSHOT.jar"]

ADD target/Culpeper-1.0-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]