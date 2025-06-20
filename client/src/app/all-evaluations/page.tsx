'use client';

import React, { useEffect, useState } from 'react';

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

  // ダミーデータ用
  useEffect(() => {
    // PeriodId未選択ならクリア
    if (!periodId) {
      setEvaluations([]);
      return;
    }
    // mockデータ
    setEvaluations([
      {
        targetId: 1,
        targetName: '山田太郎',
        averageSkillScore: 4.2,
        averageBusinessScore: 3.9,
        averageTeamScore: 4.0,
        overallAverageScore: 4.03,
        hasComment: true,
      },
      {
        targetId: 2,
        targetName: '田中花子',
        averageSkillScore: 3.8,
        averageBusinessScore: null,
        averageTeamScore: 3.6,
        overallAverageScore: 3.7,
        hasComment: false,
      }
    ]);
  }, [periodId]);

  // コメント取得もダミー化
  const handleViewComments = async (targetId: number, targetName: string) => {
    setSelectedEmployeeName(targetName);
    setIsModalOpen(true);
    setIsLoadingComments(true);
    // 1秒後にダミーコメント
    setTimeout(() => {
      setComments([
        { evaluatorName: "佐藤次郎", comment: "素晴らしいリーダーシップでした！" }
      ]);
      setIsLoadingComments(false);
    }, 1000);
  };

  return (
    <div className="pe-container">
      <div className="pe-titleBar">
        <h1>全社員評価一覧（ダミー）</h1>
      </div>
      <div className="pe-descriptionBox">
        <p>✖:評価対象項目外　-:未提出<br />期・Qを選択してください。</p>
      </div>
      <div style={{ margin: "16px 0" }}>
        <select
          className="pe-termDropdownButton"
          value={periodId}
          onChange={e => setPeriodId(e.target.value)}
        >
          <option value="">期・Qを選択</option>
          <option value="1">2024Q1</option>
          <option value="2">2024Q2</option>
        </select>
      </div>
      <div className="pe-scoreTableWrapper">
        <table className="pe-table">
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
            {evaluations.length === 0 && (
              <tr>
                <td colSpan={6} className="pe-noDataMsg">データがありません</td>
              </tr>
            )}
            {evaluations.map((e) => (
              <tr key={e.targetId}>
                <td>{e.targetName}</td>
                <td>{e.averageSkillScore?.toFixed(1) ?? '-'}</td>
                <td>{e.averageBusinessScore?.toFixed(1) ?? '-'}</td>
                <td>{e.averageTeamScore?.toFixed(1) ?? '-'}</td>
                <td>{e.overallAverageScore.toFixed(2)}</td>
                <td>
                  {e.hasComment ? (
                    <button className="pe-snippetButton" onClick={() => handleViewComments(e.targetId, e.targetName)}>
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
      </div>
      {isModalOpen && (
        <div className="pe-modalOverlay">
          <div className="pe-modalContent">
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
              <h2>{selectedEmployeeName}さんへのコメント</h2>
              <button className="pe-closeButton" onClick={() => setIsModalOpen(false)}>✖</button>
            </div>
            <div style={{ marginTop: 16 }}>
              {isLoadingComments ? (
                <p>読み込み中…</p>
              ) : comments.length > 0 ? (
                <ul>
                  {comments.map((comment, index) => (
                    <li key={index}><b>{comment.evaluatorName}:</b> {comment.comment}</li>
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

export default EvaluationSummaryPage;
