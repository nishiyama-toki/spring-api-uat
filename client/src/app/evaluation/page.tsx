// client/src/app/past-evaluation/page.tsx
"use client";

import React, { useState } from "react";
import axios from '@/utils/axiosInstance';
import styles from "./PastEvaluation.module.css";

// --- 型定義(interface) ---
interface CommentData {
  name: string;
  comment: string;
}

// 生データ用
interface EvaluationRawDTO {
  skillScore: number;
  businessScore: number;
  teamScore: number;
  comment: string;
  evaluatorName: string;
}

// API レスポンスの型
interface PastEvaluationResponse {
  phase_number: number;
  name: string;
  comments: CommentData[];
  rawEvaluations: EvaluationRawDTO[]; // バックエンドからの生データ配列
}


// ------- モーダル（全文表示モーダル）--------
const CommentModal: React.FC<{
  name: string;
  fullComment: string;
  onClose: () => void;
}> = ({ name, fullComment, onClose }) => (
  <div className={styles.modalOverlay}>
    <div className={styles.modalContent}>
      <h2>{name} さんのコメント</h2>
      <p className={styles.modalText}>{fullComment}</p>
      <div className={styles.modalFooter}>
        <button className={styles.closeButton} onClick={onClose}>
          閉じる
        </button>
      </div>
    </div>
  </div>
);

const PastEvaluationPage: React.FC = () => {
  // --- state ---
  const termList = ["18"];
  const [selectedTerm, setSelectedTerm] = useState<string | null>(null);
  const [termDropdownOpen, setTermDropdownOpen] = useState(false);
  const [selectedQuarter, setSelectedQuarter] = useState<number | null>(null);

  const [phaseNumber, setPhaseNumber] = useState<number | null>(null);
  const [phaseName, setPhaseName] = useState<string | null>(null);

  const [skillScore, setSkillScore] = useState<number | null>(null);
  const [businessScore, setBusinessScore] = useState<number | null>(null);
  const [teamScore, setTeamScore] = useState<number | null>(null);

  const [comments, setComments] = useState<CommentData[]>([]);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const [showModal, setShowModal] = useState(false);
  const [modalName, setModalName] = useState("");
  const [modalFullComment, setModalFullComment] = useState("");

  // --- handlers ---
  const toggleTermDropdown = () =>
    setTermDropdownOpen((open) => !open);

  const handlePhaseSelect = (term: string) => {
    setSelectedTerm(term);
    setTermDropdownOpen(false);
    setSelectedQuarter(null);
    setPhaseNumber(null);
    setPhaseName(null);
    setSkillScore(null);
    setBusinessScore(null);
    setTeamScore(null);
    setComments([]);
    setErrorMessage(null);
  };

  const handleQuarterSelect = async (q: number) => {
    // 表示をリセット
    setSelectedQuarter(q);
    setPhaseNumber(null);
    setPhaseName(null);
    setSkillScore(null);
    setBusinessScore(null);
    setTeamScore(null);
    setComments([]);
    setErrorMessage(null);

    try {
      const token = localStorage.getItem("token");
      const res = await axios.get<PastEvaluationResponse>(
        "/api/past-evaluations",
        {
          params: { phase_id: q },
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      // フェーズ情報をセット
      setPhaseNumber(res.data.phase_number);
      setPhaseName(res.data.name);

      // --- ここからがスコア平均値計算の修正箇所 ---

      // 生データ一覧を取得
      const raw = res.data.rawEvaluations;

      if (raw && raw.length > 0) {
        // スコアが0より大きい（＝有効な）評価のみを対象に平均を計算する関数
        const calculateAverage = (key: keyof EvaluationRawDTO) => {
          // スコアが0より大きいレコードだけを抽出
          const validEvaluations = raw.filter(v => (v[key] as number) > 0);
          const count = validEvaluations.length;

          // 有効な評価が1件もなければ、nullを返す (画面には '─' が表示される)
          if (count === 0) {
            return null;
          }

          // 有効な評価の合計スコアを計算
          const sum = validEvaluations.reduce((acc, v) => acc + (v[key] as number), 0);

          // 平均値を小数点第1位まで計算して返す
          return parseFloat((sum / count).toFixed(1));
        };

        // 各スコアの平均値を計算してstateを更新
        setSkillScore(calculateAverage("skillScore"));
        setBusinessScore(calculateAverage("businessScore"));
        setTeamScore(calculateAverage("teamScore"));

      } else {
        // データがない場合はnullのまま
        setSkillScore(null);
        setBusinessScore(null);
        setTeamScore(null);
      }
      
      // --- ここまでがスコア平均値計算の修正箇所 ---

      // コメント一覧はそのままセット
      setComments(res.data.comments);

    } catch (e: any) {
      const data = e.response?.data;
      const msg =
        data && typeof data === "object" && data.message
          ? data.message
          : data ?? "サーバー接続に失敗しました。";
      setErrorMessage(msg);
    }
  };


  const openCommentModal = (name: string, full: string) => {
    setModalName(name);
    setModalFullComment(full);
    setShowModal(true);
  };
  const closeCommentModal = () => setShowModal(false);

  const renderCommentSnippet = (text: string, name: string) => {
    if (text.length <= 100) return <span>{text}</span>;
    const snippet = text.slice(0, 100);
    return (
      <>
        <span>{snippet}…</span>
        <button
          className={styles.snippetButton}
          onClick={() => openCommentModal(name, text)}
        >
          続きを読む
        </button>
      </>
    );
  };

  return (
    <div className={styles.container}>
      {/* ヘッダー */}

      {/* 見出し */}
      <div className={styles.titleBar}>
        <h1>過去評価履歴</h1>
      </div>

      {/* 説明文 */}
      <div className={styles.descriptionBox}>
        <p>
          【操作方法】期を選択→四半期を選択すると、その期・四半期の評価平均値と
          コメント一覧が表示されます。
        </p>
      </div>

      {/* メイン */}
      <div className={styles.mainContent}>
        <aside className={styles.sidebar}>
          <button
            className={styles.termDropdownButton}
            onClick={toggleTermDropdown}
          >
            {selectedTerm ? `${selectedTerm}期 ▼` : "期を選択 ▼"}
          </button>
          {termDropdownOpen && (
            <ul className={styles.dropdownMenu}>
              {termList.map((t) => (
                <li key={t}>
                  <div
                    className={`${styles.dropdownItem} ${
                      selectedTerm === t
                        ? styles.dropdownItemSelected
                        : ""
                    }`}
                    onClick={() => handlePhaseSelect(t)}
                  >
                    {t}期
                  </div>
                </li>
              ))}
            </ul>
          )}

          {selectedTerm && (
            <div className={styles.quarterList}>
              {[1, 2, 3, 4].map((q) => (
                <div
                  key={q}
                  className={`${styles.quarterItem} ${
                    selectedQuarter === q
                      ? styles.quarterSelected
                      : ""
                  }`}
                  onClick={() => handleQuarterSelect(q)}
                >
                  {q}Q
                </div>
              ))}
            </div>
          )}
        </aside>

        <div className={styles.content}>
          {/* 期・四半期タイトル */}
          {phaseNumber !== null && phaseName && (
            <div className={styles.phaseHeader}>
              <h2>
                {phaseNumber}期 {phaseName}
              </h2>
            </div>
          )}

          {/* 平均スコア */}
          <div className={styles.scoreTableWrapper}>
            <h2>平均スコア</h2>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th>スキル</th>
                  <th>ビジネス</th>
                  <th>チームマネジメント</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>
                    {skillScore !== null
                      ? skillScore.toFixed(1)
                      : "─"}
                  </td>
                  <td>
                    {businessScore !== null
                      ? businessScore.toFixed(1)
                      : "─"}
                  </td>
                  <td>
                    {teamScore !== null
                      ? teamScore.toFixed(1)
                      : "─"}
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          {/* コメント一覧 */}
          <div className={styles.commentTableWrapper}>
            <h2>コメント</h2>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th className={styles.nameCol}>名前</th>
                  <th className={styles.commentCol}>コメント</th>
                </tr>
              </thead>
              <tbody>
                {comments.length > 0 ? (
                  comments.map((c, i) => (
                    <tr key={i}>
                      <td>{c.name} さん</td>
                      <td>
                        {renderCommentSnippet(
                          c.comment,
                          c.name
                        )}
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td
                      colSpan={2}
                      className={styles.noDataMsg}
                    >
                      この四半期のコメントはまだありません。
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>

          {/* エラーメッセージ */}
          {errorMessage && (
            <p className={styles.errorMsg}>
              {errorMessage}
            </p>
          )}
        </div>
      </div>

      {/* モーダル */}
      {showModal && (
        <CommentModal
          name={modalName}
          fullComment={modalFullComment}
          onClose={closeCommentModal}
        />
      )}
    </div>
  );
};

export default PastEvaluationPage;