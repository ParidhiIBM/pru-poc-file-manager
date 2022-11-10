#cmd : docker build -t filemanager-service .
FROM eclipse-temurin:8-jdk-alpine
EXPOSE 9003
VOLUME /tmp
COPY target/*.jar filemanager-service.jar
ENTRYPOINT ["java","-jar","/filemanager-service.jar"]