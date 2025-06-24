// 'use client';

// import React, { useState } from 'react';
// import axios from 'axios';
// import styles from './SelfEvaluation.module.css';

// // .env.local で NEXT_PUBLIC_API_BASE_URL を設定しておく
// const BASE = process.env.NEXT_PUBLIC_API_BASE_URL!;

// interface EvaluationRequest {
//   phase_id: number;
//   evaluator_id: number;
//   target_id: number;
//   skill_score: number | null;
//   business_score: number | null;
//   team_score: number | null;
//   comment: string;
// }

// /**
//  * props:
//  *  phase: 期の番号
//  *  quarter: Qの番号
//  */
// export default function SelfEvaluationClient({
//   phase,
//   quarter,
// }: {
//   phase: number;
//   quarter: number;
// }) {
//   const [skill, setSkill] = useState('');
//   const [business, setBusiness] = useState('');
//   const [team, setTeam] = useState('');
//   const [comment, setComment] = useState('');
//   const [message, setMessage] = useState<string | null>(null);

//   const handleSubmit = async (e: React.FormEvent) => {
//     e.preventDefault();
//     const userId = 1;
//     const payload: EvaluationRequest = {
//       phase_id: phase,
//       evaluator_id: userId,
//       target_id: userId,
//       skill_score: skill ? parseFloat(skill) : null,
//       business_score: business ? parseFloat(business) : null,
//       team_score: team ? parseFloat(team) : null,
//       comment,
//     };
//     try {
//       const res = await axios.post(
//         `${BASE}http://localhost:8080/api/self-evaluations`,
//         payload,
//         { headers: { 'Content-Type': 'application/json' } }
//       );
//       setMessage(res.data.message || '登録完了');
//     } catch {
//       setMessage('登録に失敗しました');
//     }
//   };

//   return (
//     <div className={styles.container}>
//       <header className={styles.header}>
//         <h1 className={styles.headerTitle}>
//           自己評価 ({phase}期 {quarter}Q)
//         </h1>
//       </header>

//       <form onSubmit={handleSubmit} className={styles.formSections}>
//         <div className={styles.scoreGrid}>
//           {/** スキル */}
//           <div className={styles.inputGroup}>
//             <label className={styles.label}>スキル</label>
//             <input
//               type="number"
//               className={styles.input}
//               value={skill}
//               onChange={(e) => setSkill(e.target.value)}
//               step={0.1} min={1} max={5}
//             />
//           </div>
//           {/** ビジネス */}
//           <div className={styles.inputGroup}>
//             <label className={styles.label}>ビジネス</label>
//             <input
//               type="number"
//               className={styles.input}
//               value={business}
//               onChange={(e) => setBusiness(e.target.value)}
//               step={0.1} min={1} max={5}
//             />
//           </div>
//           {/** チームマネジメント */}
//           <div className={styles.inputGroup}>
//             <label className={styles.label}>チームマネジメント</label>
//             <input
//               type="number"
//               className={styles.input}
//               value={team}
//               onChange={(e) => setTeam(e.target.value)}
//               step={0.1} min={1} max={5}
//             />
//           </div>
//         </div>

//         {/** コメント */}
//         <div className={styles.inputGroup}>
//           <label className={styles.label}>コメント</label>
//           <textarea
//             className={styles.textarea}
//             value={comment}
//             onChange={(e) => setComment(e.target.value)}
//             maxLength={1000}
//           />
//         </div>

//         {message && (
//           <div
//             className={`${styles.message} ${
//               message.includes('完了')
//                 ? styles.successMessage
//                 : styles.errorMessage
//             }`}
//           >
//             {message}
//           </div>
//         )}

//         <div className={styles.submitSection}>
//           <button type="submit" className={styles.submitButton}>
//             送信
//           </button>
//         </div>
//       </form>
//     </div>
//   );
// }

'use client';

import React, { useState, useEffect, useMemo } from 'react';
import { useSearchParams } from 'next/navigation';
import axios from 'axios';
import styles from './SelfEvaluation.module.css';

// API ベース URL
const BASE = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080';

interface EvaluationRequest {
  phase_id: number;
  evaluator_id: number;
  target_id: number;
  skill_score: number | null;
  business_score: number | null;
  team_score: number | null;
  comment: string;
}

const ConfirmationModal = ({ onConfirm, onCancel, isLoading, data }: {
  onConfirm: () => void;
  onCancel: () => void;
  isLoading: boolean;
  data: { skill: string; business: string; team: string; comment: string };
}) => (
  <div className="fixed inset-0 bg-black/60 flex items-center justify-center p-4 z-50">
    <div className="bg-white rounded-xl shadow-xl max-w-md w-full p-6">
      <h2 className="text-xl font-bold mb-4">登録内容の確認</h2>
      <div className="space-y-2">
        <div className="flex justify-between"><span>スキル:</span><span>{data.skill || 'ー'}</span></div>
        <div className="flex justify-between"><span>ビジネス:</span><span>{data.business || 'ー'}</span></div>
        <div className="flex justify-between"><span>チーム:</span><span>{data.team || 'ー'}</span></div>
        {data.comment.trim() && (
          <div className="mt-2"><span>コメント:</span>
            <p className="mt-1 p-2 bg-gray-100 rounded whitespace-pre-wrap break-words max-h-32 overflow-auto text-sm">
              {data.comment}
            </p>
          </div>
        )}
      </div>
      <div className="mt-6 flex justify-end space-x-3">
        <button onClick={onCancel} disabled={isLoading} className="px-4 py-2 bg-gray-200 rounded-md hover:bg-gray-300 disabled:opacity-50">戻る</button>
        <button onClick={onConfirm} disabled={isLoading} className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:bg-blue-400">
          {isLoading ? '送信中...' : '送信'}
        </button>
      </div>
    </div>
  </div>
);

export default function SelfEvaluationClient() {
  const searchParams = useSearchParams();
  const [phase, setPhase] = useState(0);
  const [quarter, setQuarter] = useState(0);
  const [heading, setHeading] = useState('自己評価');

  const [skill, setSkill] = useState('');
  const [business, setBusiness] = useState('');
  const [team, setTeam] = useState('');
  const [comment, setComment] = useState('');
  const [message, setMessage] = useState<string | null>(null);
  const [isConfirmOpen, setIsConfirmOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const userId = 1;

  useEffect(() => {
    const storedHeading = localStorage.getItem('heading');
    if (storedHeading) setHeading(storedHeading);
  }, []);

  useEffect(() => {
    const parsedPhase = parseInt(searchParams.get('phase') || '0', 10);
    const parsedQuarter = parseInt(searchParams.get('quarter') || '0', 10);
    setPhase(parsedPhase);
    setQuarter(parsedQuarter);
  }, [searchParams]);

  useEffect(() => {
    if (phase > 0 && userId > 0) {
      axios.get(`${BASE}/api/self-evaluations`, {
        params: { phase_id: phase, user_id: userId },
      }).then((res) => {
        const data = res.data;
        setSkill(data.skillScore?.toString() || '');
        setBusiness(data.businessScore?.toString() || '');
        setTeam(data.teamScore?.toString() || '');
        setComment(data.comment || '');
        setMessage('以前の評価を読み込みました。');
      }).catch((err) => {
        if (axios.isAxiosError(err) && err.response?.status === 404) {
          console.log('新規作成です');
          setMessage(null);
        } else {
          console.error('取得失敗:', err);
          setMessage('評価データの読み込みに失敗しました。');
        }
      });
    }
  }, [phase, userId]);

  const isSubmittable = useMemo(() =>
    skill.trim() || business.trim() || team.trim() || comment.trim(), [skill, business, team, comment]
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
      setMessage(res.data.message || '評価を登録・更新しました。');
    } catch (err) {
      if (axios.isAxiosError(err) && err.response) {
        setMessage(`登録に失敗しました: ${err.response.data.message || err.message}`);
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
          <h1 className={styles.headerTitle}>{heading}</h1>
        </header>
        <main className={styles.mainContent}>
          <form onSubmit={handleSubmit} className={styles.formSections}>
            <div className={styles.scoreGrid}>
              <div className={styles.inputGroup}>
                <label className={styles.label}>スキル</label>
                <input type="number" className={styles.input} value={skill} onChange={(e) => setSkill(e.target.value)} step={0.1} min={1} max={5} />
              </div>
              <div className={styles.inputGroup}>
                <label className={styles.label}>ビジネス</label>
                <input type="number" className={styles.input} value={business} onChange={(e) => setBusiness(e.target.value)} step={0.1} min={1} max={5} />
              </div>
              <div className={styles.inputGroup}>
                <label className={styles.label}>チーム</label>
                <input type="number" className={styles.input} value={team} onChange={(e) => setTeam(e.target.value)} step={0.1} min={1} max={5} />
              </div>
            </div>
            <div className={styles.inputGroup}>
              <label className={styles.label}>コメント</label>
              <textarea className={styles.textarea} value={comment} onChange={(e) => setComment(e.target.value)} maxLength={1000} rows={5} />
              <div className={styles.commentCounter}>{comment.length} / 1000</div>
            </div>
            {message && <div className={`${styles.message} ${message.includes('失敗') ? styles.errorMessage : styles.successMessage}`}>{message}</div>}
            <div className={styles.submitSection}>
              <button type="submit" className={styles.submitButton} disabled={isLoading || !isSubmittable}>送信</button>
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
