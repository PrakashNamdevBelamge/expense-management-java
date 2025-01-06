FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/expense-app.jar expense-app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "expense-app.jar"]
