'use client';

// --- 必要なライブラリやフックをインポート ---
import React, { useEffect, useState } from 'react';
import axios from 'utils/axiosInstance';
import { useRouter } from 'next/navigation';

import styles from './evaluation_requests.module.css'; // そのまま！

// --- JWTの中身をデコードするユーティリティ関数 ---
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

  // --- JSXの返却 ---
  return (
    <div className={styles.container}>
      <div className={styles.titleBar}>
        <h1>提出依頼一覧</h1>
      </div>
      <div className={styles.descriptionBox}>
        <p>下記の評価提出依頼を選択してください</p>
      </div>
      {requests.length === 0 ? (
        <p className={styles.noDataMsg}>現在、評価依頼はありません。</p>
      ) : (
        <ul style={{ listStyle: "none", padding: 0, marginTop: 24 }}>
          {requests.map((req, index) => (
            <li key={index} className={styles.scoreTableWrapper} style={{ marginBottom: 16 }}>
              <div className={styles.phaseHeader}>
                <span className={styles.nameCol}>
                  {req.phaseNumber}期 {req.quarterName} {req.type === 'SELF' ? '自己評価' : '多面評価'}
                </span>
                <span style={{ marginLeft: 12, color: '#888' }}>
                  提出期間 {formatDate(req.startDate)} ～ {formatDate(req.endDate)}
                </span>
              </div>
              <button
                className={styles.snippetButton}
                onClick={() => {
                  const label = `${req.phaseNumber}期 ${req.quarterName} ${
                    req.type === 'SELF' ? '自己評価' : '多面評価'
                  }`;
                  localStorage.setItem('heading', label);
                  router.push(`/form/${req.targetName}`);
                }}
              >
                提出フォームへ
              </button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
