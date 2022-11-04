#cmd : docker build -t filemanager-service .
FROM openjdk:8
EXPOSE 9003
ADD target/filemanager-service.jar filemanager-service.jar
ENTRYPOINT ["java","-jar","/filemanager-service.jar"]