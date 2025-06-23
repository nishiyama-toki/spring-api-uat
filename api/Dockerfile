# -------- Step 1: ビルド用ステージ --------
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

COPY api/pom.xml .
RUN mvn dependency:go-offline

COPY api /app
RUN mvn clean package -DskipTests

# -------- Step 2: 実行用ステージ --------
FROM openjdk:17-jdk-slim
WORKDIR /app

# jar名をワイルドカードで対応（より柔軟）
COPY --from=builder /app/target/*.jar app.jar

# Spring Boot は 8080 番で起動するため公開
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
