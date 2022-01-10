FROM openjdk:8-alpine
MAINTAINER mose
EXPOSE 8080
COPY target/wps-web-office-1.0.0.jar /opt/app.jar
WORKDIR /opt
ENTRYPOINT ["java","-jar","app.jar"]