'use client';

import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';

// データ型定義
interface EvaluationResponse {
  targetId: string;
  phaseNumber: number;
  name: string;      // Q（例：2Q）
  type: string;      // SELF or PEER
  startDate: string; // ISO形式
  endDate: string;   // ISO形式
}

export default function EvaluationRequestPage() {
  // ダミーデータで初期化
  const [requests, setRequests] = useState<EvaluationResponse[]>([
    {
      targetId: '101',
      phaseNumber: 19,
      name: '1Q',
      type: 'SELF',
      startDate: '2024-04-01',
      endDate: '2024-04-07'
    },
    {
      targetId: '102',
      phaseNumber: 19,
      name: '1Q',
      type: 'PEER',
      startDate: '2024-04-01',
      endDate: '2024-04-10'
    },
    {
      targetId: '103',
      phaseNumber: 18,
      name: '4Q',
      type: 'PEER',
      startDate: '2023-12-01',
      endDate: '2023-12-15'
    }
  ]);
  const router = useRouter();

  // 日付フォーマット（M/D）
  const formatDate = (iso: string) => {
    const d = new Date(iso);
    return `${d.getMonth() + 1}/${d.getDate()}`;
  };

  return (
    <main className="pe-container" style={{ maxWidth: 720 }}>
      <div className="pe-titleBar">
        <h1>提出依頼一覧</h1>
      </div>

      <div className="pe-mainContent" style={{ display: 'block' }}>
        {requests.length === 0 ? (
          <p className="pe-noDataMsg" style={{ marginTop: 24 }}>現在、評価依頼はありません。</p>
        ) : (
          <ul style={{ padding: 0, listStyle: 'none', marginTop: 28 }}>
            {requests.map((req, index) => (
              <li key={index} className="pe-quarterItem" style={{ marginBottom: 16, padding: 18 }}>
                <div className="pe-quarterList" style={{ fontSize: 14, marginBottom: 6, color: '#0070f3', fontWeight: 600 }}>
                  提出期間 {formatDate(req.startDate)} ～ {formatDate(req.endDate)}
                </div>
                <button
                  className="pe-termDropdownButton"
                  style={{
                    width: '100%',
                    background: '#f2f2f2',
                    color: '#333',
                    border: '1px solid #ccc',
                    borderRadius: 4,
                    fontSize: 18,
                    fontWeight: 600,
                    padding: '14px 0',
                    cursor: 'pointer',
                    boxShadow: 'none'
                  }}
                  onClick={() => {
                    const label = `${req.phaseNumber}期 ${req.name} ${req.type === 'SELF' ? '自己評価' : '多面評価'}`;
                    localStorage.setItem('heading', label);
                    router.push('/form/selfEvaluation');
                  }}
                >
                  {req.phaseNumber}期 {req.name} {req.type === 'SELF' ? '自己評価' : '多面評価'}
                </button>
              </li>
            ))}
          </ul>
        )}
      </div>
    </main>
  );
}
