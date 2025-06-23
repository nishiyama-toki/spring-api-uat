'use client'

import { useState, useEffect } from 'react';
import {TermQuarterSelector} from '../components/TermQuarterSelector';
import { UnsubmittedTable } from './components/UnsubmittedTable';
import styles from './UnsubmittedPage.module.css';
import axios from '@/utils/axiosInstance'; 
import { withAdminAuth } from '../hooks/useAuth';

type UnsubmittedResponse = {
  name: string;
};

function UnsubmittedPage() {
    // 選択された評価期ID(phaseId)を管理
    const [selectedPhaseId, setSelectedPhaseId] = useState<string>('');  // Reactの沼

    // 未提出者一覧データ
    const [unsubmittedEmployees, setUnsubmittedEmployees] = useState<UnsubmittedResponse[]>([]);

    // 評価期が変更された時にAPIで未提出者一覧を取得
    useEffect(() => {
        if (!selectedPhaseId) {
            setUnsubmittedEmployees([]); // 選択がクリアされたらリストもクリアする
            return;
        }

        const fetchUnsubmitted = async () => {
            try {
                const response = await axios.get('/api/unsubmitted', {
                    params: { phase_id: selectedPhaseId }
                });

                if (response.data && Array.isArray(response.data.unsubmitted_list)) {
                    setUnsubmittedEmployees(response.data.unsubmitted_list);
                } else {
                    console.error('API did not return a valid unsubmitted_list array:', response.data);
                    setUnsubmittedEmployees([]);
                }
            }   catch (error) {
                console.error('未提出者の取得に失敗しました', error);
                setUnsubmittedEmployees([]);
            }
        };

        fetchUnsubmitted();
    }, [selectedPhaseId]);

    return (
        <div className={styles.container}>
            <h1 className={styles.title}>未提出者一覧</h1>

            {/* 評価期セレクター */}
            <TermQuarterSelector 
                value={selectedPhaseId}
                onChange={(newId: string) => setSelectedPhaseId(newId)} 
            />

            {/* 未提出者リスト(UnsubmittedTableコンポーネントを呼び出す) */}
            <div className={styles.listContainer}>
                <UnsubmittedTable unsubmittedList={unsubmittedEmployees} />
            </div>
        </div>
    );
}

export default withAdminAuth(UnsubmittedPage);