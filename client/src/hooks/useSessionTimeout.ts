// hooks/useSessionTimeout.ts
import { useEffect, useCallback } from 'react';
import { useRouter } from 'next/navigation';

// JWTのペイロードの型定義（必要最低限のクレーム）
interface JwtPayload {
  exp: number; // 有効期限（Unixタイムスタンプ、秒）
  // 他のクレームがあればここに追加（例: sub, iat など）
}

// JWTの中身をデコードするユーティリティ関数（Base64 → JSON）
function parseJwt(token: string): JwtPayload | null {
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/'); // Base64URLから通常のBase64に変換
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    return JSON.parse(jsonPayload);
  } catch (e) {
    console.error("Failed to parse JWT:", e);
    return null;
  }
}

/**
 * JWTの有効期限を監視し、期限切れ時にログイン画面へリダイレクトするカスタムフック
 * @param timeoutMinutes JWTの有効期限（分）。JWTクレームのexpが優先される。
 */
export const useSessionTimeout = (timeoutMinutes: number) => { // <-- export が正しくあることを確認
  const router = useRouter();

  const handleLogout = useCallback(() => {
    console.log('Session timed out. Redirecting to login.');
    localStorage.removeItem('token'); // トークンを削除
    router.push('/login'); // ログイン画面へ遷移
  }, [router]);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      // トークンがない場合は即座にログインページへリダイレクト
      handleLogout();
      return;
    }

    const payload = parseJwt(token);
    if (!payload || !payload.exp) {
      // 有効期限情報がない不正なJWTは、ログアウト処理
      console.error('Invalid JWT: No expiration claim or failed to parse.');
      handleLogout();
      return;
    }

    // JWTの 'exp' クレームはUnixタイムスタンプ (秒)。ミリ秒に変換。
    const expirationTimeMs = payload.exp * 1000;
    const currentTimeMs = Date.now();

    // 実際にリダイレクトするまでの時間 (ミリ秒)
    const bufferTimeMs = 5 * 1000; // 期限切れの5秒前に処理を開始するバッファ
    let timeUntilRedirect = expirationTimeMs - currentTimeMs - bufferTimeMs;

    let timeoutId: NodeJS.Timeout;

    if (timeUntilRedirect <= 0) {
      // すでに期限切れに近いか、または期限切れの場合、即座にリダイレクト
      handleLogout();
    } else {
      // 期限切れ時にリダイレクトするようにタイマーを設定
      timeoutId = setTimeout(() => {
        handleLogout();
      }, timeUntilRedirect);
    }

    // コンポーネントがアンマウントされたらタイマーをクリア
    return () => {
      if (timeoutId) {
        clearTimeout(timeoutId);
      }
    };
  }, [handleLogout, timeoutMinutes]);
};