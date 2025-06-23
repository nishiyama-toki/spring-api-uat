'use client';

import React, { useEffect, useState } from 'react';
import { TermQuarterSelector } from '../components/TermQuarterSelector';
import styles from './all-evaluation.module.css';
// 本番用APIインスタンス
import axios from '../../utils/axiosInstance';
// import { withAdminAuth } from '../hooks/useAuth';

type EmployeeEvaluation = {
    targetId: number;
    targetName: string;
    averageSkillScore: number | null;
    averageBusinessScore: number | null;
    averageTeamScore: number | null;
    overallAverageScore: number;
    hasComment: boolean;
};

type Comment = {
    evaluatorName: string;
    comment: string;
};

const EvaluationSummaryPage: React.FC = () => {
    const [periodId, setPeriodId] = useState<string>('');
    const [evaluations, setEvaluations] = useState<EmployeeEvaluation[]>([]);
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [comments, setComments] = useState<Comment[]>([]);
    const [selectedEmployeeName, setSelectedEmployeeName] = useState<string>('');
    const [isLoadingComments, setIsLoadingComments] = useState<boolean>(false);

    useEffect(() => {
        if (!periodId) {
            setEvaluations([]);
            return;
        }

        // === ★【ここを切り替え】====================
        // --- 本番API使用時はこちらを有効化 ---
        /*
        const fetchEvaluations = async () => {
            try {
                const response = await axios.get('/api/all_evaluations', {
                    params: { phase_id: periodId }
                });
                if (response.data && Array.isArray(response.data.employees)) {
                    setEvaluations(response.data.employees);
                } else {
                    setEvaluations([]);
                }
            } catch (error) {
                setEvaluations([]);
            }
        };
        fetchEvaluations();
        */

        // --- フロント単体で確認したい場合はこちらを有効化 ---
        const dummyEvaluations: EmployeeEvaluation[] = [
            {
                targetId: 1,
                targetName: '山田 太郎',
                averageSkillScore: 4.2,
                averageBusinessScore: 3.8,
                averageTeamScore: 4.0,
                overallAverageScore: 4.0,
                hasComment: true,
            },
            {
                targetId: 2,
                targetName: '田中 花子',
                averageSkillScore: null,
                averageBusinessScore: null,
                averageTeamScore: null,
                overallAverageScore: 0,
                hasComment: false,
            },
        ];
        setEvaluations(dummyEvaluations);
        // === ★ここまで =======================

    }, [periodId]);

    // コメント取得（ダミー対応）
    const handleViewComments = async (targetId: number, targetName: string) => {
        if (!periodId) return;

        setSelectedEmployeeName(targetName);
        setIsModalOpen(true);
        setIsLoadingComments(true);

        // === ★【ここを切り替え】====================
        // --- 本番API使用時はこちらを有効化 ---
        /*
        try {
            const response = await axios.get(`/api/employees/${targetId}/comments`, {
                params: { phase_id: periodId }
            });
            setComments(response.data);
        } catch (error) {
            setComments([]);
        } finally {
            setIsLoadingComments(false);
        }
        */

        // --- フロント単体で確認したい場合はこちらを有効化 ---
        setTimeout(() => {
            if (targetId === 1) {
                setComments([
                    { evaluatorName: "佐藤", comment: "よく頑張っていました！" },
                    { evaluatorName: "鈴木", comment: "積極的に取り組めていました。" }
                ]);
            } else {
                setComments([]);
            }
            setIsLoadingComments(false);
        }, 700);
        // === ★ここまで =======================
    };

    return (
        <div className={styles.container}>
            {/* 見出し部分 */}
            <div className={styles.header}>
                <h2>全社員評価一覧</h2>
                <p>✖:評価対象項目外 -:未提出</p>
            </div>
            {/* 期・Q選択 */}
            <div className={styles.selectorContainer}>
                <TermQuarterSelector
                    value={periodId}
                    onChange={(newId: string) => setPeriodId(newId)} />
            </div>
            {/* 評価一覧 */}
            <table className={styles.table}>
                <thead>
                    <tr>
                        <th>氏名</th>
                        <th>スキル</th>
                        <th>ビジネス</th>
                        <th>チームマネジメント</th>
                        <th>評価合計平均</th>
                        <th>コメント</th>
                    </tr>
                </thead>
                <tbody>
                    {evaluations.map((e) => (
                        <tr key={e.targetId}>
                            <td>{e.targetName}</td>
                            <td>{e.averageSkillScore?.toFixed(1) ?? '-'}</td>
                            <td>{e.averageBusinessScore?.toFixed(1) ?? '-'}</td>
                            <td>{e.averageTeamScore?.toFixed(1) ?? '-'}</td>
                            <td>{e.overallAverageScore.toFixed(2)}</td>
                            <td>
                                {e.hasComment ? (
                                    <button
                                        className={styles.button}
                                        onClick={() =>
                                            handleViewComments(e.targetId, e.targetName)
                                        }
                                    >
                                        コメントを見る
                                    </button>
                                ) : (
                                    '-'
                                )}
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {/* コメントモーダル */}
            {isModalOpen && (
                <div className={styles.modalOverlay}>
                    <div className={styles.modalContent}>
                        <div className={styles.modalHeader}>
                            <h2>{selectedEmployeeName}さんへのコメント</h2>
                            <button className={styles.modalCloseButton} onClick={() => setIsModalOpen(false)}>✖</button>
                        </div>
                        <div className={styles.modalBody}>
                            {isLoadingComments ? (
                                <p>読み込み中…</p>
                            ) : comments.length > 0 ? (
                                <ul className={styles.commentList}>
                                    {comments.map((comment, index) => (
                                        <li key={index} className={styles.commentItem}>
                                            <strong className={styles.commenterName}>{comment.evaluatorName}さんより:</strong>
                                            <p className={styles.commentText}>{comment.comment}</p>
                                        </li>
                                    ))}
                                </ul>
                            ) : (
                                <p>コメントはありません。</p>
                            )}
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default withAdminAuth(EvaluationSummaryPage);
