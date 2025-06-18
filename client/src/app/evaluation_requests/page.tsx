
'use client';

import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useRouter } from 'next/navigation';

// --- サーバーから受け取る評価依頼情報の型定義（Spring Bootのレスポンスに対応）
interface EvaluationResponse {
  targetName: string;        // 評価対象者のID（文字列として表示 or 遷移に使う）
  phaseNumber: number;       // 期（例：19）
  quarterName: string;       // Q名（例：1Q、2Q）
  type: string;              // 評価種別（"SELF" or "PEER"）
  startDate: string;         // 提出開始日
  endDate: string;           // 提出終了日
}

export default function EvaluationRequestPage() {
  const [requests, setRequests] = useState<EvaluationResponse[]>([]);
  const router = useRouter();
  const evaluatorId = 1;

  useEffect(() => {
    axios
      .get(`http://localhost:8080/api/evaluations?evaluatorId=${evaluatorId}`)
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
    <main className="evaluation-list-container">
      <h1 className="heading">提出依頼一覧</h1>

      {requests.length === 0 ? (
        <p>現在、評価依頼はありません。</p>
      ) : (
        <ul className="evaluation-list">
          {requests.map((req, index) => (
            <li key={index} className="evaluation-item">
              <p className="period-label">
                提出期間 {formatDate(req.startDate)} ～ {formatDate(req.endDate)}
              </p>

              <a
                className="evaluation-link"
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
