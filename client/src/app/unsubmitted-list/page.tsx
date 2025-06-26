'use client'

import { useState, useEffect } from 'react';
import { TermQuarterSelector } from '../components/TermQuarterSelector';
import { UnsubmittedTable } from './components/UnsubmittedTable';
import styles from './UnsubmittedPage.module.css';
import axios from '@/utils/axiosInstance';
import { useSessionTimeout } from '@/hooks/useSessionTimeout';

type UnsubmittedResponse = {
  name: string;
};

export default function UnsubmittedPage() {
  useSessionTimeout(30); // JWT有効期限が30分の場合
  const [selectedPhaseId, setSelectedPhaseId] = useState<string>('');
  const [unsubmittedEmployees, setUnsubmittedEmployees] = useState<UnsubmittedResponse[]>([]);

  useEffect(() => {
    if (!selectedPhaseId) {
      setUnsubmittedEmployees([]);
      return;
    }

    const fetchUnsubmitted = async () => {
      try {
        const response = await axios.get('/api/unsubmitted', {
          params: { phase_id: selectedPhaseId },
        });

        if (response.data && Array.isArray(response.data.unsubmitted_list)) {
          setUnsubmittedEmployees(response.data.unsubmitted_list);
        } else {
          console.error('API did not return a valid unsubmitted_list array:', response.data);
          setUnsubmittedEmployees([]);
        }
      } catch (error) {
        console.error('未提出者の取得に失敗しました', error);
        setUnsubmittedEmployees([]);
      }
    };

    fetchUnsubmitted();
  }, [selectedPhaseId]);

  return (
    <div className={styles.container}>
      {/* 見出しは .titleBar と .titleBar h1 を活用 */}
      <div className={styles.titleBar}>
        <h1>未提出者一覧</h1>
      </div>

      {/* セレクター部分。囲い不要ならこのままでもOK */}
      <div style={{ margin: "16px 0" }}>
        <TermQuarterSelector
          value={selectedPhaseId}
          onChange={(newId: string) => setSelectedPhaseId(newId)}
        />
      </div>

      {/* 未提出リスト表示エリア */}
      <div className={styles.listContainer}>
        <UnsubmittedTable unsubmittedList={unsubmittedEmployees} />
      </div>
    </div>
  );
}