FROM node:20-alpine AS builder

WORKDIR /app
COPY ./client/package*.json ./          # 依存関係だけ先にコピー（キャッシュ効かせるなら）
COPY ./client/src/ .                    # srcディレクトリの中身を/app直下にコピー

RUN npm install
RUN npm run build

FROM node:20-alpine

WORKDIR /app
COPY --from=builder /app ./

EXPOSE 3000

CMD ["npm", "start"]
