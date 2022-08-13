FROM openjdk:18-oracle

WORKDIR /app

COPY target/backend-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "/app/backend-0.0.1-SNAPSHOT.jar"]