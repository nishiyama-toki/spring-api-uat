# FROM eclipse-temurin:17-jdk-alpine
# WORKDIR /app
# COPY ./api /app
# WORKDIR /app
# RUN ./mvnw clean package -DskipTests
# CMD ["java", "-jar", "target/api-0.0.1-SNAPSHOT.jar"]

# ===== build stage =====
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY ./api /app
RUN mvn clean package -DskipTests

# ===== runtime stage =====
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
