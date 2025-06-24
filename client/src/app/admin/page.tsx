'use client';

import { useEffect, useState } from 'react';
import AlertBox from '../components/AlertBox';
import axios from 'utils/axiosInstance';  // ← 追加
import { useSessionTimeout } from '@/hooks/useSessionTimeout';

interface NotSubmittedUser {
  name: string;
  email: string;
  alert_message: string;
}

interface Phase {
  name: string;
  start_date: string;
  end_date: string;
  self_eval_due: string;
  peer_eval_due: string;
}

export default function AdminHome() {
  const [overview, setOverview] = useState('');
  const [notSubmitted, setNotSubmitted] = useState<NotSubmittedUser[]>([]);
  const [userName, setUserName] = useState('');
  const [currentPhase, setCurrentPhase] = useState<Phase | null>(null);

  useSessionTimeout(30);

  useEffect(() => {
    axios.get('/api/home')    // ← fetch から axios に
      .then(res => {
        const data = res.data;
        setOverview(data.overview);
        setUserName(data.user_name);
        setCurrentPhase(data.current_phase);
        setNotSubmitted(data.not_submitted_list ?? []);
      });
  }, []);

  return (
    <div className="pe-container">
      <div className="pe-titleBar">
        <h1>{overview}</h1>
      </div>
      <div className="pe-descriptionBox">
        <div>ユーザー名：{userName}（管理者）</div>
        <div>現在の評価フェーズ: {currentPhase?.name}</div>
        <div className="text-sm text-gray-500">
          フェーズ期間: {currentPhase?.start_date} ～ {currentPhase?.end_date}<br />
          自己評価期限: {currentPhase?.self_eval_due} / 多面評価期限: {currentPhase?.peer_eval_due}
        </div>
      </div>
      {notSubmitted.length > 0 && (
        <AlertBox alerts={[]} notSubmitted={notSubmitted} isAdmin={true} />
      )}
    </div>
  );
}
