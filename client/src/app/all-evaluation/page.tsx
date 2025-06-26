'use client';

import React, { useEffect, useState } from 'react';
import { TermQuarterSelector } from '../components/TermQuarterSelector';
import styles from './all-evaluation.module.css';
<<<<<<< HEAD
import axios from '../../utils/axiosInstance';
import { withAdminAuth } from '../hooks/useAuth';
=======
import axios from '@/utils/axiosInstance';
import { useSessionTimeout } from '@/hooks/useSessionTimeout';
>>>>>>> finaltest-from-develop

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

<<<<<<< HEAD
=======
    useSessionTimeout(30);

>>>>>>> finaltest-from-develop
    useEffect(() => {
        if (!periodId) {
            setEvaluations([]);
            return;
        }
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
<<<<<<< HEAD
            }catch (error) {
=======
            } catch (error) {
>>>>>>> finaltest-from-develop
                console.error('評価データの取得に失敗しました:', error);
                setEvaluations([]);
            }
        };
        fetchEvaluations();
    }, [periodId]);

<<<<<<< HEAD
    // コメント取得ボタンが押された時の関数を追加
=======
    // コメント取得ボタンが押された時の関数
>>>>>>> finaltest-from-develop
    const handleViewComments = async (targetId: number, targetName: string) => {
        if (!periodId) return;
        setSelectedEmployeeName(targetName);
        setIsModalOpen(true);
        setIsLoadingComments(true);

        try {
            const response = await axios.get(`/api/employees/${targetId}/comments`, {
                params: { phase_id: periodId }
            });
            setComments(response.data);
<<<<<<< HEAD
        }   catch (error) {
                console.error('コメントの取得に失敗しました', error);
                setComments([]);
        }   finally {
                setIsLoadingComments(false);
=======
        } catch (error) {
            console.error('コメントの取得に失敗しました', error);
            setComments([]);
        } finally {
            setIsLoadingComments(false);
>>>>>>> finaltest-from-develop
        }
    };

    return (
        <div className={styles.container}>
            {/* 見出し */}
            <div className={styles.titleBar}>
                <h1>全社員評価一覧</h1>
            </div>

            {/* サブ説明 */}
            <div className={styles.descriptionBox}>
                <p>✖:評価対象項目外　-:未提出</p>
            </div>

            {/* 期・Q選択（TermQuarterSelector部分は適宜置き換えてください） */}
            <div style={{ marginBottom: 20 }}>
                {/* <TermQuarterSelector value={periodId} onChange={setPeriodId} /> */}
            </div>

            <div className={styles.mainContent}>
                <div className={styles.content}>
                    <div className={styles.scoreTableWrapper}>
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
                                                    className={styles.snippetButton}
                                                    onClick={() => handleViewComments(e.targetId, e.targetName)}
                                                >
                                                    コメントを見る
                                                </button>
                                            ) : (
                                                '-'
                                            )}
                                        </td>
                                    </tr>
                                ))}
                                {evaluations.length === 0 && (
                                    <tr>
                                        <td colSpan={6} className={styles.noDataMsg}>
                                            データがありません
                                        </td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            {/* コメントモーダル */}
            {isModalOpen && (
                <div className={styles.modalOverlay}>
                    <div className={styles.modalContent}>
                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                            <h2>{selectedEmployeeName}さんへのコメント</h2>
                            <button className={styles.closeButton} onClick={() => setIsModalOpen(false)}>✖</button>
                        </div>
                        <div style={{ marginTop: 24 }}>
                            {isLoadingComments ? (
                                <p>読み込み中…</p>
                            ) : comments.length > 0 ? (
                                <ul style={{ padding: 0, margin: 0 }}>
                                    {comments.map((comment, index) => (
                                        <li key={index} style={{ listStyle: 'none', marginBottom: 16 }}>
                                            <strong>{comment.evaluatorName}さんより：</strong>
                                            <p className={styles.modalText}>{comment.comment}</p>
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
<<<<<<< HEAD

};

export default withAdminAuth(EvaluationSummaryPage);
=======
};

export default EvaluationSummaryPage;
>>>>>>> finaltest-from-develop
