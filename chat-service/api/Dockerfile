FROM openjdk:17-jdk-slim

VOLUME /tmp

ARG JAR_FILE=api/build/libs/api-1.0.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar", "/app.jar"]
