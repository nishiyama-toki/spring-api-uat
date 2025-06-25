// utils/axiosInstance.ts
// 共通のAxiosインスタンス設定ファイル
// API通信で使用されるインスタンス
// リクエストごとにJWTトークンをヘッダーに付与
// レスポンスで新トークンがあればlocalStorageに保存

import axios from 'axios'

// Axiosインスタンスの作成
// const instance = axios.create({
//   baseURL: 'http://localhost:8080', // Spring Boot バックエンドのAPIベースURL
// })
const instance = axios.create({
  baseURL: 'https://toki-nishiyama-project-2.onrender.com', // ← バックエンドの本番URLを直書き！
})



// リクエストインターセプター
// リクエスト前にJWTをAuthorizationヘッダーに付与
// 未認証API（ログイン/パスワードリセット関連）は除外
instance.interceptors.request.use(config => {
  const token = localStorage.getItem('token'); //

  // 認証不要なAPI一覧（トークンを送らない）
  const isPublicApi =
    config.url?.endsWith('/api/login') ||
    config.url?.endsWith('/api/reset-mail') ||
    config.url?.endsWith('/api/reset-password');

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
    const newToken = response.headers['authorization']; //
    if (newToken && newToken.startsWith('Bearer ')) {
      const token = newToken.replace('Bearer ', ''); //
      localStorage.setItem('token', token); // トークンを更新保存
    }
    return response;
  },
  (error) => {
    // 認証エラー (401 Unauthorized) を捕捉し、ログイン画面へリダイレクト
    // axios.isAxiosError を使用してエラーがAxiosErrorインスタンスであるかチェック
    if (axios.isAxiosError(error) && error.response && error.response.status === 401) {
      console.log('401 Unauthorized: Session expired or invalid token. Redirecting to login.'); //
      localStorage.removeItem('token'); // 無効なトークンを削除
      // Next.jsのルーターはインターセプターからは直接使えないため、
      // ここではwindow.locationを使って強制リダイレクト
      // アラートなどでユーザーに通知することも検討
      alert('セッションが切れました。再度ログインしてください。'); //
      window.location.href = '/login'; // ログインページのパス
    }
    return Promise.reject(error); // 他のエラーはそのまま返す
  }
);

// インスタンスをエクスポートして全画面から共通利用可能にする
export default instance;
