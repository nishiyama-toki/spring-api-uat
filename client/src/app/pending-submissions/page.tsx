'use client'

import { useState, useEffect } from 'react';

export default function UnsubmittedPage() {
  const [selectedPhaseId, setSelectedPhaseId] = useState<string>('');
  const [unsubmittedEmployees, setUnsubmittedEmployees] = useState<{ name: string }[]>([]);

  useEffect(() => {
    if (!selectedPhaseId) {
      setUnsubmittedEmployees([]);
      return;
    }
    setUnsubmittedEmployees([
      { name: "未提出 太郎" },
      { name: "未提出 花子" }
    ]);
  }, [selectedPhaseId]);

  return (
    <div className="pe-container">
      <div className="pe-titleBar">
        <h1>未提出者一覧（ダミー）</h1>
      </div>
      <div className="pe-descriptionBox">
        <p>期・四半期を選択すると、その期・Qの未提出者が表示されます。</p>
      </div>
      <div style={{ marginTop: 16 }}>
        <select
          className="pe-termDropdownButton"
          value={selectedPhaseId}
          onChange={e => setSelectedPhaseId(e.target.value)}
        >
          <option value="">期・Qを選択</option>
          <option value="1">2024Q1</option>
        </select>
      </div>
      <div style={{ marginTop: 16 }}>
        <ul>
          {unsubmittedEmployees.length === 0 && (
            <li className="pe-noDataMsg">未提出者はいません。</li>
          )}
          {unsubmittedEmployees.map((emp, i) => (
            <li key={i} className="pe-quarterItem">{emp.name}</li>
          ))}
        </ul>
      </div>
    </div>
  );
}
