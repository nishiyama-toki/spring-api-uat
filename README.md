# SetUp
## git clone
任意のディレクトリでgit clone

```
git clone {リポジトリurl}
```

## wsl起動
```
wsl --import eval-system c:\wsl\eval-system c:\wsl\repository\dev-default\alma-docker.tar
wsl -d eval-system
```

## apiコンテナ起動
本リポジトリをクローンした場所までcdし、以下のコマンドを実行
```
# docker起動
docker-compose up -d

# spring boot起動
docker-compose exec api /bin/bash
cd api
mvn spring-boot:run
```

### 動作確認①
http://localhost:8080/

Hello, World!と出力されること

## front
wsl上でclientディレクトリまでcdし、以下のコマンドを実行
```
npm install
```

### build command
`http://localhost:3000`でアクセスする場合
```
npm run dev
```

`http://localhost:80`でアクセスする場合
```
npm run build
```

### 動作確認②
http://localhost:3000/test

This is a test page.と出力されること

http://localhost:80/test

This is a test page.と出力されること

http://localhost:80/api

Hello, World!と出力されること


仮ユーザー

管理者
Secure!2024
dummy2@example.com

一般
Kabuki1!!!!1
kabuki@example.com