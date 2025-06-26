'use client'; 

import React, { useEffect, useState } from 'react';
import axios from '@/utils/axiosInstance';
import { useRouter } from 'next/navigation';
import { useSessionTimeout } from '@/hooks/useSessionTimeout';

function parseJwt(token: string) {
  try {
    return JSON.parse(atob(token.split('.')[1]));
  } catch (e) {
    return null;
  }
}

interface EvaluationResponse {
  phaseId: number; // ★ DTOに合わせて追加
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
    <main className="evaluation-list-container">
      <h1 className="heading">提出依頼一覧</h1>

      {requests.length === 0 ? (
        <p>現在、評価依頼はありません。</p>
      ) : (
        <ul className="evaluation-list">
          {requests.map((req, index) => {
            let navPath = '';
            let label = '';

            if (req.evaluationType === 'SELF') {
              // ★ 正しい phaseId を使うように修正
              navPath = `/selfEvaluation?phase=${req.phaseId}&quarter=${encodeURIComponent(req.quarterName)}`;
              label = `${req.phaseNumber}期 ${req.quarterName} 自己評価`;
            } else if (req.evaluationType === 'PEER') {
              // ★ 正しい phaseId を使うように修正
              navPath = `/multi-evaluations?phase=${req.phaseId}&quarter=${encodeURIComponent(req.quarterName)}`;
              label = `${req.phaseNumber}期 ${req.quarterName} 多面評価`;
            } else {
              navPath = '#';
              label = `${req.phaseNumber}期 ${req.quarterName} (評価タイプ不明)`;
            }

            return (
              <li key={index} className="evaluation-item">
                <p className="period-label">
                  提出期間 {formatDate(req.startDate)} ～ {formatDate(req.endDate)}
                </p>

                <a
                  className="evaluation-link"
                  style={{ cursor: 'pointer' }} // クリック可能であることを示す
                  onClick={() => {
                    localStorage.setItem('heading', label);
                    router.push(navPath);
                  }}
                >
                  {label}
                </a>
              </li>
            );
          })}
        </ul>
      )}
    </main>
  );
}
