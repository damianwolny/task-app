FROM openjdk:17
ADD target/task-api-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD java -jar task-api-0.0.1-SNAPSHOT.jar