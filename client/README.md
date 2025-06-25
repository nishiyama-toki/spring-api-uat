<<<<<<< HEAD
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
=======
This is a [Next.js](https://nextjs.org) project bootstrapped with [`create-next-app`](https://nextjs.org/docs/app/api-reference/cli/create-next-app).

## Getting Started

First, run the development server:

```bash
npm run dev
# or
yarn dev
# or
pnpm dev
# or
bun dev
```

Open [http://localhost:3000](http://localhost:3000) with your browser to see the result.

You can start editing the page by modifying `app/page.tsx`. The page auto-updates as you edit the file.

This project uses [`next/font`](https://nextjs.org/docs/app/building-your-application/optimizing/fonts) to automatically optimize and load [Geist](https://vercel.com/font), a new font family for Vercel.

## Learn More

To learn more about Next.js, take a look at the following resources:

- [Next.js Documentation](https://nextjs.org/docs) - learn about Next.js features and API.
- [Learn Next.js](https://nextjs.org/learn) - an interactive Next.js tutorial.

You can check out [the Next.js GitHub repository](https://github.com/vercel/next.js) - your feedback and contributions are welcome!

## Deploy on Vercel

The easiest way to deploy your Next.js app is to use the [Vercel Platform](https://vercel.com/new?utm_medium=default-template&filter=next.js&utm_source=create-next-app&utm_campaign=create-next-app-readme) from the creators of Next.js.

Check out our [Next.js deployment documentation](https://nextjs.org/docs/app/building-your-application/deploying) for more details.
>>>>>>> mizukami
