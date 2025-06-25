
'use client'; // このファイルはクライアントコンポーネントであると明示

// React本体（JSX使用のため）と、Next.jsのルーティング機能をインポート
import React from 'react';
import { useRouter } from 'next/navigation';
<<<<<<< HEAD
=======
import { useSessionTimeout } from '@/hooks/useSessionTimeout';
>>>>>>> mizukami

// このコンポーネントが「送信完了画面」の本体
export default function SubmittedPage() {
  // Next.jsのuseRouterフックでルーターオブジェクトを取得
  const router = useRouter();

<<<<<<< HEAD
=======
  // useSessionTimeout カスタムフックを呼び出す
  useSessionTimeout(30); // JWT有効期限が30分の場合

>>>>>>> mizukami
  // 「評価依頼一覧へ」ボタンが押されたときに実行される処理
  const handleBack = () => {
    // 一覧画面（評価依頼一覧）にクライアント遷移する
    router.push('/evaluation-requests'); // ← URIは画面ID「Evaluation-008」に対応
  };

  // 実際の画面の構成を返す（JSXで定義）
  return (
    <main className="submitted-container">
      {/* 完了アイコン（チェックマーク） */}
      <div className="submitted-icon">✅</div>

      {/* メッセージテキスト */}
      <h1 className="submitted-message">送信完了しました</h1>

      {/* 「評価依頼一覧へ」ボタン */}
      <button onClick={handleBack} className="submitted-button">
        評価依頼一覧へ
      </button>
    </main>
  );
}
