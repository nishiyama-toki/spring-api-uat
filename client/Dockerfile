#renderとの接続のためにdockerを追加

# Stage 1: Build
FROM node:18 AS builder

WORKDIR /app
COPY . .

RUN npm install
RUN npm run build && npm run export

# Stage 2: Serve with nginx
FROM nginx:stable-alpine

# ビルド成果物をnginxの公開ディレクトリにコピー
COPY --from=builder /app/out /usr/share/nginx/html

# nginxの設定をコピー（あれば）
COPY ../docker/nginx/default.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
