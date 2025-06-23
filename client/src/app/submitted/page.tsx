'use client';

import React from 'react';
import { useRouter } from 'next/navigation';
import styles from './SubmittedPage.module.css'; // ← module CSSをインポート

export default function SubmittedPage() {
  const router = useRouter();

  const handleBack = () => {
    router.push('/evaluation-requests');
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
