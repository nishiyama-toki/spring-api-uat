'use client';

import React, { useState } from "react";

export default function PastEvaluationPage() {
  // ダミーデータ
  const termList = ["18"];
  const dummyData = {
    1: {
      phase_number: 18,
      name: "第1四半期",
      skillScore: 4.2,
      businessScore: 4.0,
      teamScore: 3.8,
      comments: [
        { name: "佐藤太郎", comment: "チーム貢献が素晴らしい。" },
        { name: "山田花子", comment: "課題への取り組みが的確でした。" },
        { name: "長文太郎", comment: "これはとても長いコメントのテストです。".repeat(10) },
      ],
    },
    2: {
      phase_number: 18,
      name: "第2四半期",
      skillScore: 3.5,
      businessScore: 3.6,
      teamScore: 3.7,
      comments: [],
    },
    // 必要なら他のクォーターも追加
  };

  // state
  const [selectedTerm, setSelectedTerm] = useState<string | null>(null);
  const [termDropdownOpen, setTermDropdownOpen] = useState(false);
  const [selectedQuarter, setSelectedQuarter] = useState<number | null>(null);

  const [phaseNumber, setPhaseNumber] = useState<number | null>(null);
  const [phaseName, setPhaseName] = useState<string | null>(null);

  const [skillScore, setSkillScore] = useState<number | null>(null);
  const [businessScore, setBusinessScore] = useState<number | null>(null);
  const [teamScore, setTeamScore] = useState<number | null>(null);

  const [comments, setComments] = useState<{ name: string; comment: string }[]>([]);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const [showModal, setShowModal] = useState(false);
  const [modalName, setModalName] = useState("");
  const [modalFullComment, setModalFullComment] = useState("");

  // handlers
  const toggleTermDropdown = () => setTermDropdownOpen((open) => !open);

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

  const handleQuarterSelect = (q: number) => {
    setSelectedQuarter(q);
    setErrorMessage(null);

    // ダミーデータで設定
    const d = dummyData[q as 1 | 2];
    if (d) {
      setPhaseNumber(d.phase_number);
      setPhaseName(d.name);
      setSkillScore(d.skillScore);
      setBusinessScore(d.businessScore);
      setTeamScore(d.teamScore);
      setComments(d.comments);
    } else {
      setPhaseNumber(null);
      setPhaseName(null);
      setSkillScore(null);
      setBusinessScore(null);
      setTeamScore(null);
      setComments([]);
      setErrorMessage("データがありません。");
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
          className="pe-snippetButton"
          onClick={() => openCommentModal(name, text)}
        >
          続きを読む
        </button>
      </>
    );
  };

  return (
          <div className="pe-container">
            <div className="pe-titleBar">
              <h1>過去評価履歴</h1>
            </div>
            <div className="pe-descriptionBox">
        <p>
          【操作方法】期を選択→四半期を選択すると、その期・四半期の評価平均値とコメント一覧が表示されます。
        </p>
        <p>
          <strong>1. スペシャリスト</strong><br />
          等級判定において、等級基準書のスキル面、およびビジネス面について両方とも満たすことを条件とする。
        </p>
        <p>
          <strong>2. マネージャー</strong><br />
          等級判定において、等級基準書のビジネス面、およびチームマネジメント面について両方とも満たすことを条件とする。
        </p>
        <p>
          <strong>3. ゼネラリスト</strong><br />
          等級判定において、等級基準書のスキル面、ビジネス面、およびチームマネジメント面の全てを満たすことを条件とする。
        </p>
      </div>
      <div className="pe-mainContent">
        <aside className="pe-sidebar">
          <button
            className="pe-termDropdownButton"
            onClick={toggleTermDropdown}
          >
            {selectedTerm ? `${selectedTerm}期 ▼` : "期を選択 ▼"}
          </button>
          {termDropdownOpen && (
            <ul className="pe-dropdownMenu">
              {termList.map((t) => (
                <li key={t}>
                  <div
                    className={`pe-dropdownItem ${
                      selectedTerm === t ? "pe-dropdownItemSelected" : ""
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
            <div className="pe-quarterList">
              {[1, 2, 3, 4].map((q) => (
                <div
                  key={q}
                  className={`pe-quarterItem ${
                    selectedQuarter === q ? "pe-quarterSelected" : ""
                  }`}
                  onClick={() => handleQuarterSelect(q)}
                >
                  {q}Q
                </div>
              ))}
            </div>
          )}
        </aside>
        <div className="pe-content">
          {phaseNumber !== null && phaseName && (
            <div className="pe-phaseHeader">
              <h2>
                {phaseNumber}期 {phaseName}
              </h2>
            </div>
          )}
          <div className="pe-scoreTableWrapper">
            <h2>平均スコア</h2>
            <table className="pe-table">
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
          <div className="pe-commentTableWrapper">
            <h2>コメント</h2>
            <table className="pe-table">
              <thead>
                <tr>
                  <th className="pe-nameCol">名前</th>
                  <th className="pe-commentCol">コメント</th>
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
                      className="pe-noDataMsg"
                    >
                      この四半期のコメントはまだありません。
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
          {errorMessage && (
            <p className="pe-errorMsg">
              {errorMessage}
            </p>
          )}
        </div>
      </div>
      {showModal && (
        <div className="pe-modalOverlay">
          <div className="pe-modalContent">
            <h2>{modalName} さんのコメント</h2>
            <p className="pe-modalText">{modalFullComment}</p>
            <div className="pe-modalFooter">
              <button className="pe-closeButton" onClick={closeCommentModal}>
                閉じる
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
