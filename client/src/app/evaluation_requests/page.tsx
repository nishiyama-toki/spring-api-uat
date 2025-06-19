
'use client'; // クライアントコンポーネントとして明示

// --- 必要なライブラリやフックをインポート ---
import React, { useEffect, useState } from 'react'; // Reactの基本機能とHooks
import axios from 'axios'; // HTTPリクエスト送信用ライブラリ
import { useRouter } from 'next/navigation'; // ページ遷移用のフック

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
  targetName: string;        // 評価対象者のID（文字列として表示 or 遷移に使う）
  phaseNumber: number;       // 期（例：19）
  quarterName: string;       // Q名（例：1Q、2Q）
  type: string;              // 評価種別（"SELF" or "PEER"）
  startDate: string;         // 提出開始日
  endDate: string;           // 提出終了日
}

// --- コンポーネント本体 ---
export default function EvaluationRequestPage() {
  // 評価依頼一覧のステート（初期値は空配列）
  const [requests, setRequests] = useState<EvaluationResponse[]>([]);
  const router = useRouter();

  // --- 初回レンダリング時にGETリクエストを実行 ---
  useEffect(() => {
    const token = localStorage.getItem('token'); // ← ローカルストレージからJWTトークンを取得
    if (!token) {
      console.error('トークンが存在しません');
      return;
    }

    const payload = parseJwt(token); // ← トークンからpayload部分を抽出
    const evaluatorId = payload?.userId; // ← JWT内のuserId（評価者ID）を取得

    if (!evaluatorId) {
      console.error('トークンからevaluatorIdを取得できません');
      return;
    }

    // Spring BootのバックエンドAPIにGETリクエストを送信する（認証ヘッダー付き）
    axios
      .get(`http://localhost:8080/api/evaluations?evaluatorId=${evaluatorId}`, {
        headers: {
          Authorization: `Bearer ${token}`, // ← トークンをAuthorizationヘッダーに付与
        },
      })
      .then((res) => {
        setRequests(res.data); // 成功したらレスポンスをステートに反映
      })
      .catch((err) => {
        console.error('評価依頼の取得に失敗しました', err); // エラーが出た場合のログ
      });
  }, []);

  // --- 日付を「M/D」形式に整形して返す関数 ---
  const formatDate = (iso: string): string => {
    const d = new Date(iso); // ISO文字列をDateオブジェクトに変換
    return `${d.getMonth() + 1}/${d.getDate()}`; // 月は0始まりなので +1
  };

  // --- JSXの返却（画面の見た目） ---
  return (
    <main className="evaluation-list-container">
      <h1 className="heading">提出依頼一覧</h1>

      {/* データが空の場合の表示 */}
      {requests.length === 0 ? (
        <p>現在、評価依頼はありません。</p>
      ) : (
        <ul className="evaluation-list">
          {/* 評価依頼ごとにリスト項目を生成 */}
          {requests.map((req, index) => (
            <li key={index} className="evaluation-item">
              <p className="period-label">
                提出期間 {formatDate(req.startDate)} ～ {formatDate(req.endDate)}
              </p>

              {/* 評価フォームへのリンク（クリックで画面遷移） */}
              <a
                className="evaluation-link"
                onClick={() => {
                  // localStorageに見出し文字列を一時保存（フォーム側で使用）
                  const label = `${req.phaseNumber}期 ${req.quarterName} ${
                    req.type === 'SELF' ? '自己評価' : '多面評価'
                  }`;
                  localStorage.setItem('heading', label);

                  // フォームページへ遷移（例：/form/3）
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
