'use client';

import { useEffect, useState } from 'react';
import AlertBox from '../components/AlertBox';

interface Alert {
  message: string;
  date: string;
  type: string;
}

interface Phase {
  name: string;
  start_date: string;
  end_date: string;
  self_eval_due: string;
  peer_eval_due: string;
}

export default function UserHome() {
  const [overview, setOverview] = useState('');
  const [alerts, setAlerts] = useState<Alert[]>([]);
  const [userName, setUserName] = useState('');
  const [currentPhase, setCurrentPhase] = useState<Phase | null>(null);

  useEffect(() => {
    fetch('/api/home')
      .then(res => res.json())
      .then((data: {
        overview: string;
        user_name: string;
        current_phase: Phase;
        alert_list?: Alert[];
      }) => {
        setOverview(data.overview);
        setAlerts(data.alert_list ?? []);
        setUserName(data.user_name);
        setCurrentPhase(data.current_phase);
      });
  }, []);

  return (
    <div className="pe-container">
      <div className="pe-titleBar">
        <h1>{overview}</h1>
      </div>
      <div className="pe-descriptionBox">
        <div>ユーザー名：{userName}（一般ユーザー）</div>
        <div>現在の評価フェーズ: {currentPhase?.name}</div>
        <div className="text-sm text-gray-500">
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
