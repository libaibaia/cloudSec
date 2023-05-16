FROM openjdk:8-jdk-alpine

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /home/app.jar

ENTRYPOINT ["java","-jar","/home/app.jar"]
