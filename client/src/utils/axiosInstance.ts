// utils/axiosInstance.ts
// 共通のAxiosインスタンス設定ファイル
// API通信で使用されるインスタンス
// リクエストごとにJWTトークンをヘッダーに付与
// レスポンスで新トークンがあればlocalStorageに保存

import axios from 'axios'

// Axiosインスタンスの作成
const instance = axios.create({
  baseURL: 'http://localhost:8080', // Spring Boot バックエンドのAPIベースURL
})

// リクエストインターセプター
// リクエスト前にJWTをAuthorizationヘッダーに付与
// 未認証API（ログイン/パスワードリセット関連）は除外
instance.interceptors.request.use(config => {
<<<<<<< HEAD
  const token = localStorage.getItem('jwt')
   // /api/login のときは Authorization ヘッダーを付けない
  if (token && config.url !== '/api/login') {
    config.headers.Authorization = `Bearer ${token}` // Bearer を付ける
  }
  return config
})

// レスポンスで新トークンがあれば保存（TokenRefreshFilter対応）
instance.interceptors.response.use(response => {
  const newToken = response.headers['authorization']
  if (newToken) {
    localStorage.setItem('jwt', newToken)
  }
  return response
})
=======
  const token = localStorage.getItem('token');

  // 認証不要なAPI一覧（トークンを送らない）
  const isPublicApi =
    config.url?.endsWith('/api/login') ||
    config.url?.endsWith('/api/reset-mail') ||
    config.url?.endsWith('/api/reset-password');
>>>>>>> origin/yukihiro

  // 認証が必要なAPIにはトークンを付与
  if (!isPublicApi && token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

// レスポンスインターセプター
// TokenRefreshFilterが返す新しいトークンをlocalStorageに保存
instance.interceptors.response.use(
  (response) => {
    const newToken = response.headers['authorization'];
    if (newToken && newToken.startsWith('Bearer ')) {
      const token = newToken.replace('Bearer ', '');
      localStorage.setItem('token', token); // トークンを更新保存
    }
    return response;
  },
  (error) => Promise.reject(error) // エラーはそのまま呼び出し元に返す
);

// インスタンスをエクスポートして全画面から共通利用可能にする
export default instance;
