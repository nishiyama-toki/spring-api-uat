<<<<<<< HEAD

'use client'; // クライアントコンポーネントとして明示

// --- 必要なライブラリやフックをインポート ---
import React, { useEffect, useState } from 'react'; // Reactの基本機能とHooks
import axios from 'utils/axiosInstance'; // ← 修正：共通のaxiosインスタンスに変更
import { useRouter } from 'next/navigation'; // ページ遷移用のフック
=======
'use client'; // クライアントコンポーネントとして明示

import React, { useEffect, useState } from 'react';
import axios from '@/utils/axiosInstance';
import { useRouter } from 'next/navigation';
import { useSessionTimeout } from '@/hooks/useSessionTimeout';
>>>>>>> mizukami

// --- JWTの中身をデコードするユーティリティ関数（Base64 → JSON） ---
function parseJwt(token: string) {
  try {
    return JSON.parse(atob(token.split('.')[1]));
  } catch (e) {
    return null;
  }
}

// --- サーバーから受け取る評価依頼情報の型定義（Spring Bootのレスポンスに対応） ---
interface EvaluationResponse {
  targetId: number; // バックエンドから0Lが来るのでnumber型でOK
  targetName: string; // バックエンドから空文字列が来る
  phaseNumber: number;
  quarterName: string;
  startDate: string;
  endDate: string;
  evaluationType: string; // 'SELF' or 'PEER'
}

// --- コンポーネント本体 ---
export default function EvaluationRequestPage() {
  const [requests, setRequests] = useState<EvaluationResponse[]>([]);
  const router = useRouter();

  // useSessionTimeout カスタムフックを呼び出す
  useSessionTimeout(30); // JWT有効期限が30分の場合

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      console.error('トークンが存在しません');
      // 未認証ユーザーをログインページにリダイレクトすることも検討
      // router.push('/login');
      return;
    }

    const payload = parseJwt(token);
    const evaluatorId = payload?.sub;

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
        // エラーハンドリング (例: ユーザーにメッセージを表示)
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
              navPath = `/selfEvaluation?phase=${req.phaseNumber}&quarter=${encodeURIComponent(req.quarterName)}`;
              label = `${req.phaseNumber}期 ${req.quarterName} 自己評価`;
            } else if (req.evaluationType === 'PEER') {
              // 多面評価の場合: targetIdとtargetNameはダミーだが、URLには含めておく
              navPath = `/multi-evaluations?phase=${req.phaseNumber}&quarter=${encodeURIComponent(req.quarterName)}&targetId=${req.targetId}&targetName=${encodeURIComponent(req.targetName)}`;
              // 表示テキストから個人名を削除
              label = `${req.phaseNumber}期 ${req.quarterName} 多面評価`; // <- ここを変更
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
