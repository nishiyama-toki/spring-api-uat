FROM ubuntu:latest

# Java
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    maven \
    curl \
    tar \
    vim \
    unzip \
    locales \
    && apt-get clean

ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH=$JAVA_HOME/bin:$PATH

# ロケールを設定
RUN locale-gen ja_JP.UTF-8 && update-locale LANG=ja_JP.UTF-8

# 環境変数の設定
ENV LANG=ja_JP.UTF-8 \
    LC_ALL=ja_JP.UTF-8 \
    LANGUAGE=ja_JP.UTF-8

WORKDIR /var/www/html

# nginx
ENV TZ Asia/Tokyo
RUN apt-get update && apt-get install -y \
    nginx \
    && apt-get clean
COPY ./docker/nginx/default.conf /etc/nginx/conf.d

# port
EXPOSE 8080 80

CMD ["nginx", "-g", "daemon off;"]