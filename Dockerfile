FROM maven:3.8.3-openjdk-17 AS builder
COPY ./ ./
RUN mvn clean package -DskipTests
FROM eclipse-temurin:17-jdk-alpine
COPY --from=builder /target/CloudStorage-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8000
ENTRYPOINT ["java","-jar","/app.jar"]