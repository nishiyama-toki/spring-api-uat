// app/selfEvaluation/page.tsx
'use client';

// useEffect をインポートに追加します
import { useSearchParams } from 'next/navigation';
import React, { useState, useMemo, useEffect } from 'react';
import axios from 'axios';
import styles from './SelfEvaluation.module.css';

// API ベース URL を環境変数で設定
const BASE = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080';

// 登録用リクエスト型
interface EvaluationRequest {
  phase_id: number;
  evaluator_id: number;
  target_id: number;
  skill_score: number | null;
  business_score: number | null;
  team_score: number | null;
  comment: string;
}

// 確認モーダルコンポーネント (変更なし)
const ConfirmationModal = ({
  onConfirm,
  onCancel,
  isLoading,
  data,
}: {
  onConfirm: () => void;
  onCancel: () => void;
  isLoading: boolean;
  data: { skill: string; business: string; team: string; comment:string };
}) => (
  <div className="fixed inset-0 bg-black/60 flex items-center justify-center p-4 z-50">
    <div className="bg-white rounded-xl shadow-xl max-w-md w-full p-6">
      <h2 className="text-xl font-bold mb-4">登録内容の確認</h2>
      <div className="space-y-2">
        <div className="flex justify-between">
          <span className="text-gray-600">スキル:</span><span className="font-medium">{data.skill || 'ー'}</span>
        </div>
        <div className="flex justify-between">
          <span className="text-gray-600">ビジネス:</span><span className="font-medium">{data.business || 'ー'}</span>
        </div>
        <div className="flex justify-between">
          <span className="text-gray-600">チーム:</span><span className="font-medium">{data.team || 'ー'}</span>
        </div>
        {data.comment.trim() && (
          <div className="mt-2">
            <span className="text-gray-600">コメント:</span>
            <p className="mt-1 p-2 bg-gray-100 rounded whitespace-pre-wrap break-words max-h-32 overflow-auto text-sm">
              {data.comment}
            </p>
          </div>
        )}
      </div>
      <div className="mt-6 flex justify-end space-x-3">
        <button
          onClick={onCancel}
          disabled={isLoading}
          className="px-4 py-2 bg-gray-200 rounded-md hover:bg-gray-300 disabled:opacity-50"
        >戻る</button>
        <button
          onClick={onConfirm}
          disabled={isLoading}
          className="px-4 py-2 bg-blue-600 text-white rounded-md flex items-center hover:bg-blue-700 disabled:bg-blue-400"
        >
          {isLoading ? (
            <span>送信中...</span>
          ) : (
            '送信'
          )}
        </button>
      </div>
    </div>
  </div>
);

export default function SelfEvaluationPage() {
  const searchParams = useSearchParams();
  const phase = parseInt(searchParams.get('phase') || '0', 10);
  // const quarter = parseInt(searchParams.get('quarter') || '0', 10);どこにも使用されていなかったのでコメントアウトしてます

  const [heading, setHeading] = useState('自己評価');

  const [skill, setSkill] = useState('');
  const [business, setBusiness] = useState('');
  const [team, setTeam] = useState('');
  const [comment, setComment] = useState('');
  const [message, setMessage] = useState<string | null>(null);
  const [isConfirmOpen, setIsConfirmOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  
  const userId = 1; // TODO: 実際のユーザーIDを取得する処理に置き換えてください

  // localStorageから見出しを取得する処理
  useEffect(() => {
    const storedHeading = localStorage.getItem('heading');
    if (storedHeading) {
      setHeading(storedHeading);
    }
  }, []);

  // ページ読み込み時に、既存の評価データを取得する
  useEffect(() => {
    // phaseとuserIdが有効な値の場合のみ実行
    if (phase > 0 && userId > 0) {
      const fetchEvaluation = async () => {
        try {
          // バックエンドのGETエンドポイントを呼び出す
          const response = await axios.get(`${BASE}/api/self-evaluations`, {
            params: {
              phase_id: phase,
              user_id: userId,
            },
          });

          // レスポンスデータでフォームの初期値を設定
          if (response.data) {
            const data = response.data;
            setSkill(data.skillScore?.toString() || '');
            setBusiness(data.businessScore?.toString() || '');
            setTeam(data.teamScore?.toString() || '');
            setComment(data.comment || '');
            setMessage('以前の評価を読み込みました。');
          }
        } catch (error: unknown) {
          // 404エラーの場合は、まだ評価が存在しないだけなので正常な動作
          if (axios.isAxiosError(error) && error.response?.status === 404) {
            console.log('まだ評価データはありません。新規作成します。');
            setMessage(null); // メッセージをクリア
          } else {
            // その他のエラーはコンソールに出力
            console.error('評価データの取得に失敗しました:', error);
            setMessage('評価データの読み込みに失敗しました。');
          }
        }
      };

      fetchEvaluation();
    }
  }, [phase, userId]); // phaseかuserIdが変わったときに再実行される

  const isSubmittable = useMemo(
    () =>
      skill.trim() !== '' ||
      business.trim() !== '' ||
      team.trim() !== '' ||
      comment.trim() !== '',
    [skill, business, team, comment]
  );

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!isSubmittable) {
      setMessage('いずれかの項目を入力してください');
      return;
    }
    setMessage(null);
    setIsConfirmOpen(true);
  };

  // 本送信の処理は変更ありません
  const handleConfirm = async () => {
    setIsLoading(true);
    const payload: EvaluationRequest = {
      phase_id: phase,
      evaluator_id: userId,
      target_id: userId,
      skill_score: skill ? parseFloat(skill) : null,
      business_score: business ? parseFloat(business) : null,
      team_score: team ? parseFloat(team) : null,
      comment,
    };
    try {
      const res = await axios.post(
        `${BASE}/api/self-evaluations`,
        payload,
        { headers: { 'Content-Type': 'application/json' } }
      );
      setMessage(res.data.message || '評価を登録・更新しました。');
    } catch (error) {
      if (axios.isAxiosError(error) && error.response) {
        setMessage(`登録に失敗しました: ${error.response.data.message || error.message}`);
      } else {
        setMessage('登録に失敗しました。');
      }
    } finally {
      setIsLoading(false);
      setIsConfirmOpen(false);
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.mainWrapper}>
        <header className={styles.header}>
          <h1 className={styles.headerTitle}>
            {heading}
          </h1>
        </header>

        <main className={styles.mainContent}>
          <form onSubmit={handleSubmit} className={styles.formSections}>
            <div className={styles.scoreGrid}>
              {/* スキル */}
              <div className={styles.inputGroup}>
                <label htmlFor="skill-input" className={styles.label}>スキル</label>
                <input
                  id="skill-input"
                  type="number"
                  name="skill"
                  className={styles.input}
                  value={skill}
                  onChange={(e) => setSkill(e.target.value)}
                  step={0.1} min={1} max={5}
                />
              </div>
              {/* ビジネス */}
              <div className={styles.inputGroup}>
                <label htmlFor="business-input" className={styles.label}>ビジネス</label>
                <input
                  id="business-input"
                  type="number"
                  name="business"
                  className={styles.input}
                  value={business}
                  onChange={(e) => setBusiness(e.target.value)}
                  step={0.1} min={1} max={5}
                />
              </div>
              {/* チーム */}
              <div className={styles.inputGroup}>
                <label htmlFor="team-input" className={styles.label}>チームマネジメント</label>
                <input
                  id="team-input"
                  type="number"
                  name="team"
                  className={styles.input}
                  value={team}
                  onChange={(e) => setTeam(e.target.value)}
                  step={0.1} min={1} max={5}
                />
              </div>
            </div>

            {/* コメント */}
            <div className={styles.inputGroup}>
              <label htmlFor="comment-input" className={styles.label}>コメント</label>
              <textarea
                id="comment-input"
                name="comment"
                className={styles.textarea}
                value={comment}
                onChange={(e) => setComment(e.target.value)}
                maxLength={1000}
                rows={5}
              />
              {/* ▼▼▼ この部分を追加しました ▼▼▼ */}
              <div className={styles.commentCounter}>
                {comment.length} / 1000
              </div>
              {/* ▲▲▲ ここまで ▲▲▲ */}
            </div>

            {/* メッセージ */}
            {message && (
              <div className={`${styles.message} ${
                message.includes('失敗') ? styles.errorMessage : styles.successMessage
              }`}
              >
                {message}
              </div>
            )}

            <div className={styles.submitSection}>
              <button
                type="submit"
                className={styles.submitButton}
                disabled={isLoading || !isSubmittable}
              >
                送信
              </button>
            </div>
          </form>
        </main>
      </div>

      {/* 確認モーダル */}
      {isConfirmOpen && (
        <ConfirmationModal
          onConfirm={handleConfirm}
          onCancel={() => setIsConfirmOpen(false)}
          isLoading={isLoading}
          data={{ skill, business, team, comment }}
        />
      )}
    </div>
  );
}
