'use client';

import { useSearchParams, useRouter } from 'next/navigation';
import { useEffect, useMemo, useState } from 'react';
import axios from 'axios';
import styles from './SelfEvaluation.module.css';
import { useSessionTimeout } from '@/hooks/useSessionTimeout';

// BASE URL
const BASE = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080';

// DTO
interface EvaluationRequest {
  phase_id: number;
  evaluator_id: number;
  target_id: number;
  skill_score: number | null;
  business_score: number | null;
  team_score: number | null;
  comment: string;
}

// モーダル
const ConfirmationModal = ({
  onConfirm,
  onCancel,
  isLoading,
  data,
}: {
  onConfirm: () => void;
  onCancel: () => void;
  isLoading: boolean;
  data: { skill: string; business: string; team: string; comment: string };
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
        <button onClick={onCancel} disabled={isLoading} className="px-4 py-2 bg-gray-200 rounded-md hover:bg-gray-300 disabled:opacity-50">戻る</button>
        <button onClick={onConfirm} disabled={isLoading} className="px-4 py-2 bg-blue-600 text-white rounded-md flex items-center hover:bg-blue-700 disabled:bg-blue-400">
          {isLoading ? '送信中...' : '送信'}
        </button>
      </div>
    </div>
  </div>
);

export default function SelfEvaluationClient() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const phase = parseInt(searchParams.get('phase') || '0', 10);
  const [heading, setHeading] = useState('自己評価');
  const quarter = parseInt(searchParams.get('quarter') || '0', 10);
  const [skill, setSkill] = useState('');
  const [business, setBusiness] = useState('');
  const [team, setTeam] = useState('');
  const [comment, setComment] = useState('');
  const [message, setMessage] = useState<string | null>(null);
  const [isConfirmOpen, setIsConfirmOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const userId = 1;

  useSessionTimeout(30); // ← JWT有効期限フック

  useEffect(() => {
    const storedHeading = localStorage.getItem('heading');
    if (storedHeading) {
      setHeading(storedHeading);
    }
  }, []);

  useEffect(() => {
    if (phase > 0 && userId > 0) {
      const fetchEvaluation = async () => {
        try {
          const res = await axios.get(`${BASE}/api/self-evaluations`, {
            params: { phase_id: phase, user_id: userId }
          });
          const data = res.data;
          setSkill(data.skillScore?.toString() || '');
          setBusiness(data.businessScore?.toString() || '');
          setTeam(data.teamScore?.toString() || '');
          setComment(data.comment || '');
          setMessage('以前の評価を読み込みました。');
        } catch (err: any) {
          if (axios.isAxiosError(err) && err.response?.status === 404) {
            setMessage(null);
          } else {
            setMessage('評価データの読み込みに失敗しました。');
          }
        }
      };
      fetchEvaluation();
    }
  }, [phase, userId]);

  const isSubmittable = useMemo(() =>
    skill.trim() !== '' || business.trim() !== '' || team.trim() !== '' || comment.trim() !== '',
    [skill, business, team, comment]
  );

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!isSubmittable) {
      setMessage('いずれかの項目を入力してください');
      return;
    }
    setIsConfirmOpen(true);
  };

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
      const res = await axios.post(`${BASE}/api/self-evaluations`, payload, {
        headers: { 'Content-Type': 'application/json' },
      });
      router.push('/submitted');
    } catch (error: any) {
      setMessage('登録に失敗しました。');
    } finally {
      setIsLoading(false);
      setIsConfirmOpen(false);
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.mainWrapper}>
        <header className={styles.header}>
          <h1 className={styles.headerTitle}>{heading}</h1>
        </header>

        <main className={styles.mainContent}>
          <form onSubmit={handleSubmit} className={styles.formSections}>
            <div className={styles.scoreGrid}>
              {/* スキル */}
              <div className={styles.inputGroup}>
                <label className={styles.label}>スキル</label>
                <input type="number" value={skill} onChange={e => setSkill(e.target.value)} className={styles.input} />
              </div>
              {/* ビジネス */}
              <div className={styles.inputGroup}>
                <label className={styles.label}>ビジネス</label>
                <input type="number" value={business} onChange={e => setBusiness(e.target.value)} className={styles.input} />
              </div>
              {/* チーム */}
              <div className={styles.inputGroup}>
                <label className={styles.label}>チーム</label>
                <input type="number" value={team} onChange={e => setTeam(e.target.value)} className={styles.input} />
              </div>
            </div>

            {/* コメント */}
            <div className={styles.inputGroup}>
              <label className={styles.label}>コメント</label>
              <textarea value={comment} onChange={e => setComment(e.target.value)} className={styles.textarea} maxLength={1000} rows={5} />
              <div className={styles.commentCounter}>{comment.length} / 1000</div>
            </div>

            {/* メッセージ */}
            {message && <div className={message.includes('失敗') ? styles.errorMessage : styles.successMessage}>{message}</div>}

            <div className={styles.submitSection}>
              <button type="submit" className={styles.submitButton} disabled={isLoading || !isSubmittable}>
                送信
              </button>
            </div>
          </form>
        </main>
      </div>

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
