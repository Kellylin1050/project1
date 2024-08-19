FROM openjdk:17
EXPOSE 8080
#VOLUME /tmp
COPY target/project1-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]