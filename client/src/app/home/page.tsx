'use client';

import { useEffect, useState } from 'react';
import AlertBox from '../components/AlertBox';
import axios from '@/utils/axiosInstance'; // ← 追加

// アラート情報の型
interface Alert {
  message: string;
  date: string;
  type: string;
}

// 現在の評価フェーズ情報の型
interface Phase {
  name: string;
  start_date: string;
  end_date: string;
  self_eval_due: string;
  peer_eval_due: string;
}

// ユーザーホーム画面のコンポーネント
export default function UserHome() {
  const [overview, setOverview] = useState(''); // ページ概要（例：「〇〇期 自己評価」など）
  const [alerts, setAlerts] = useState<Alert[]>([]); // アラート一覧
  const [userName, setUserName] = useState(''); // ユーザー名
  const [currentPhase, setCurrentPhase] = useState<Phase | null>(null); // 現在のフェーズ情報

  // 初回レンダリング時にAPIからデータ取得
  useEffect(() => {
    axios.get('/api/home')
      .then(res => {
        const data = res.data;
        setOverview(data.overview);
        setAlerts(data.alert_list ?? []);
        setUserName(data.user_name);
        setCurrentPhase(data.current_phase);
      });
  }, []);

  return (
    <div className={styles.container}>
      <div className={styles.titleBar}>
        <h1>{overview}</h1>
      </div>

      <div className={styles.descriptionBox}>
        <div>ユーザー名：{userName}（一般ユーザー）</div>
        <div>現在の評価フェーズ: {currentPhase?.name}</div>
        <div className={styles.subText}>
          フェーズ期間: {currentPhase?.start_date} ～ {currentPhase?.end_date}<br />
          自己評価期限: {currentPhase?.self_eval_due} / 多面評価期限: {currentPhase?.peer_eval_due}
        </div>
      </div>

      {alerts.length > 0 && (
        <AlertBox alerts={alerts} />
      )}
    </div>
  );
}
