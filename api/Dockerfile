# === Build Stage ===
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# プロジェクト全体をコピー（.dockerignore で target など除外前提）
COPY . .

# JAR をビルド（テストスキップ）
RUN mvn clean package -DskipTests

# === Run Stage ===
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# ビルド成果物（fat JAR）をコピー
COPY --from=builder /app/target/api-0.0.1-SNAPSHOT.jar ./app.jar

# ポートを開放（Spring Boot が使うポート）
EXPOSE 8080

# アプリを起動
CMD ["java", "-jar", "app.jar"]
