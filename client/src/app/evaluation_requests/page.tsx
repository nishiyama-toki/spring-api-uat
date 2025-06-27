'use client';

import React, { useEffect, useState } from 'react';
import axios from '@/utils/axiosInstance';
import { useRouter } from 'next/navigation';
import { useSessionTimeout } from '@/hooks/useSessionTimeout';
import styles from './evaluation_requests.module.css';

// --- JWTの中身をデコードするユーティリティ関数 ---
function parseJwt(token: string) {
  try {
    return JSON.parse(atob(token.split('.')[1]));
  } catch (e) {
    return null;
  }
}

interface EvaluationResponse {
  phaseId: number;
  targetId: number;
  targetName: string;
  phaseNumber: number;
  quarterName: string;
  startDate: string;
  endDate: string;
  evaluationType: string;
}

export default function EvaluationRequestPage() {
  const [requests, setRequests] = useState<EvaluationResponse[]>([]);
  const router = useRouter();

  useSessionTimeout(30);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      console.error('トークンが存在しません');
      return;
    }

    const payload = parseJwt(token);
    const evaluatorId = payload?.sub;

    if (!evaluatorId) {
      console.error('トークンからevaluatorIdを取得できません');
      return;
    }

    axios
      .get(`/api/evaluations?evaluatorId=${evaluatorId}`)
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

  return (
    <main className={styles.container}>
      <div className={styles.titleBar}>
        <h1>提出依頼一覧</h1>
      </div>

      <div className={styles.descriptionBox}>
        <p>下記の評価提出依頼を選択してください</p>
      </div>

      {requests.length === 0 ? (
        <p className={styles.noDataMsg}>現在、評価依頼はありません。</p>
      ) : (
        <ul className={styles.evaluationList}>
          {requests.map((req, index) => {
            let navPath = '';
            let label = '';

            if (req.evaluationType === 'SELF') {
              navPath = `/selfEvaluation?phase=${req.phaseId}&quarter=${encodeURIComponent(req.quarterName)}`;
              label = `${req.phaseNumber}期 ${req.quarterName} 自己評価`;
            } else if (req.evaluationType === 'PEER') {
              navPath = `/multi-evaluations?phase=${req.phaseId}&quarter=${encodeURIComponent(req.quarterName)}`;
              label = `${req.phaseNumber}期 ${req.quarterName} 多面評価`;
            } else {
              navPath = '#';
              label = `${req.phaseNumber}期 ${req.quarterName} (評価タイプ不明)`;
            }

            return (
              <li key={index} className={styles.evaluationItem}>
                <p className={styles.periodLabel}>
                  提出期間 {formatDate(req.startDate)} ～ {formatDate(req.endDate)}
                </p>
                <button
                  type="button"
                  className={styles.evaluationLink}
                  onClick={() => {
                    localStorage.setItem('heading', label);
                    router.push(navPath);
                  }}
                >
                  {label}
                </button>
              </li>
            );
          })}
        </ul>
      )}
    </main>
  );
}