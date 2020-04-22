FROM adoptopenjdk/openjdk8:alpine-jre
ARG JAR_FILE=target/server-0.0.1.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]