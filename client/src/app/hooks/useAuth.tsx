import { jwtDecode } from 'jwt-decode';
import { useRouter } from 'next/navigation';
import { useState, useEffect } from 'react';

// トークンに含まれるデータの型を定義
interface TokenPayload {
    sub: string;    // ユーザーID
    role: 'admin' | 'employee'; // 権限
    iat: number;    // 発行日時
    exp: number;    // 有効期限
}

export function useAuth(): TokenPayload | null {
    // サーバーサイドでは実行しないようにする
    if (typeof window === 'undefined') {
        return null;
    }

    // ブラウザのlocalStorageからトークンを取得
    const token = localStorage.getItem('jwt');
    if (!token) {
        return null;
    }

    try {
        // トークンをデコード(解読)して中身を取り出す
        const payload = jwtDecode<TokenPayload>(token);

        // トークンの有効期限をチェック
        // Date.now()はミリ秒、expは秒なので、1000倍して比較
        if (payload.exp * 1000 <Date.now()) {

            // 期限切れならトークンを削除
            localStorage.removeItem('jwt');
            console.error("トークンの有効期限が切れています。");
            return null;
        }

        return payload;
    }   catch (error) {
        console.error("無効なトークンです:", error);

        // 不正なトークンも削除
        localStorage.removeItem('jwt');
        return null;
    }
}

// 管理者専用ページを保護するための高階コンポーネント
export const withAdminAuth = (Page: React.ComponentType) => {
    return (props: any) => {
        const auth = useAuth();
        const router = useRouter();
        const [isVerified, setIsVerified] = useState(false);

        useEffect(() => {

            // 認証情報を取得中、または既にリダイレクト処理が走っている場合は何もしない
            if (auth === undefined) return;

            if (auth?.role == 'admin') {
                setIsVerified(true);
            } else {
                // 管理者でなければログインページへ
                router.replace('/login');
            }
        }, [auth,router]);

        // 管理者であればページをレンダリング
        if (isVerified) {
            return <Page {...props} />;
        }

        return null;
    }
}

