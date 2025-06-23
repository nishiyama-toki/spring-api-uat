<<<<<<< Updated upstream
'use client';

import React, { useEffect, useState } from 'react';
import axios from 'utils/axiosInstance';
import { useRouter } from 'next/navigation';
import styles from './EvaluationRequestPage.module.css';

=======
'use client'; // クライアントコンポーネントとして明示

// --- 必要なライブラリやフックをインポート ---
import React, { useEffect, useState } from 'react'; // Reactの基本機能とHooks
import axios from 'utils/axiosInstance'; // 共通のaxiosインスタンスを使用
import { useRouter } from 'next/navigation'; // ページ遷移用のフック

import styles from './evaluation_requests.module.css'; // モジュールCSSの読み込み

// --- JWTの中身をデコードするユーティリティ関数（Base64 → JSON） ---
>>>>>>> Stashed changes
function parseJwt(token: string) {
  try {
    return JSON.parse(atob(token.split('.')[1]));
  } catch (e) {
    return null;
  }
}

interface EvaluationResponse {
  targetName: string;
  phaseNumber: number;
  quarterName: string;
  type: string;
  startDate: string;
  endDate: string;
}

export default function EvaluationRequestPage() {
  const [requests, setRequests] = useState<EvaluationResponse[]>([]);
  const router = useRouter();

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      console.error('トークンが存在しません');
      return;
    }

    const payload = parseJwt(token);
    const evaluatorId = payload?.userId;

    if (!evaluatorId) {
      console.error('トークンからevaluatorIdを取得できません');
      return;
    }

    axios
      .get(`/api/evaluations?evaluatorId=${evaluatorId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        setRequests(res.data);
      })
      .catch((err) => {
        console.error('評価依頼の取得に失敗しました', err);
      });
  }, []);

  const formatDate = (iso: string): string => {
    const d = new Date(iso);
    return `${d.getMonth() + 1}/${d.getDate()}`;
  };

<<<<<<< Updated upstream
  return (
    <main className={styles.container}>
=======
  // --- JSXの返却 ---
  return (
    <main className={styles['evaluation-list-container']}>
>>>>>>> Stashed changes
      <h1 className={styles.heading}>提出依頼一覧</h1>

      {requests.length === 0 ? (
        <p className={styles.noData}>現在、評価依頼はありません。</p>
      ) : (
<<<<<<< Updated upstream
        <ul className={styles.list}>
          {requests.map((req, index) => (
            <li key={index} className={styles.item}>
              <p className={styles.period}>
                提出期間 {formatDate(req.startDate)} ～ {formatDate(req.endDate)}
              </p>
              <a
                className={styles.link}
                tabIndex={0}
=======
        <ul className={styles['evaluation-list']}>
          {requests.map((req, index) => (
            <li key={index} className={styles['evaluation-item']}>
              <p className={styles['period-label']}>
                提出期間 {formatDate(req.startDate)} ～ {formatDate(req.endDate)}
              </p>
              <a
                className={styles['evaluation-link']}
>>>>>>> Stashed changes
                onClick={() => {
                  const label = `${req.phaseNumber}期 ${req.quarterName} ${
                    req.type === 'SELF' ? '自己評価' : '多面評価'
                  }`;
                  localStorage.setItem('heading', label);
                  router.push(`/form/${req.targetName}`);
                }}
              >
                {req.phaseNumber}期 {req.quarterName} {req.type === 'SELF' ? '自己評価' : '多面評価'}
              </a>
            </li>
          ))}
        </ul>
      )}
    </main>
  );
}
