# FROM ubuntu:latest

# # Java
# RUN apt-get update && apt-get install -y \
#     openjdk-17-jdk \
#     maven \
#     curl \
#     tar \
#     vim \
#     unzip \
#     locales \
#     && apt-get clean

# ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
# ENV PATH=$JAVA_HOME/bin:$PATH

# # ロケールを設定
# RUN locale-gen ja_JP.UTF-8 && update-locale LANG=ja_JP.UTF-8

# # 環境変数の設定
# ENV LANG=ja_JP.UTF-8 \
#     LC_ALL=ja_JP.UTF-8 \
#     LANGUAGE=ja_JP.UTF-8

# WORKDIR /var/www/html

# # nginx
# ENV TZ Asia/Tokyo
# RUN apt-get update && apt-get install -y \
#     nginx \
#     && apt-get clean
# COPY ./docker/nginx/default.conf /etc/nginx/conf.d

# # port
# EXPOSE 8080 80

# CMD ["nginx", "-g", "daemon off;"]



# render用に以前のものは一旦コメントアウト

# === Build Stage ===
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# ソースを全部コピー（.dockerignoreでtargetは除外される想定）
COPY . .

# JARをビルド（テストスキップ推奨）
RUN mvn clean package -DskipTests

# === Run Stage ===
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# ビルド成果物（fat JAR）をコピー
# COPY --from=builder /app/target/api-0.0.1-SNAPSHOT.jar ./app.jar

# ポート公開（Spring Boot用）
EXPOSE 8080

# アプリ実行
CMD ["java", "-jar", "app.jar"]
