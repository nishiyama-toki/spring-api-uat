'use client';

import React from 'react';
import { useRouter } from 'next/navigation';
import styles from './SubmittedPage.module.css'; // ← module CSSをインポート
import { useSessionTimeout } from '@/hooks/useSessionTimeout';

export default function SubmittedPage() {
  const router = useRouter();

  // useSessionTimeout カスタムフックを呼び出す
  useSessionTimeout(30); // JWT有効期限が30分の場合

  // 「評価依頼一覧へ」ボタンが押されたときに実行される処理
  const handleBack = () => {
    // 一覧画面（評価依頼一覧）にクライアント遷移する
    router.push('/evaluation_requests'); // ← URIは画面ID「Evaluation-008」に対応
  };

  return (
    <main className={styles.container}>
      <div className={styles.icon}>✅</div>
      <h1 className={styles.message}>送信完了しました</h1>
      <button onClick={handleBack} className={styles.button}>
        評価依頼一覧へ
      </button>
    </main>
  );
}
