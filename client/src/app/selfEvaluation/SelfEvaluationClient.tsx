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