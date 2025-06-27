FROM node:20-alpine AS builder

WORKDIR /app
COPY ./client/package*.json ./
COPY ./client/src/ .   

RUN npm install
RUN npm run build

FROM node:20-alpine

WORKDIR /app
COPY --from=builder /app ./

EXPOSE 3000

CMD ["npm", "start"]
