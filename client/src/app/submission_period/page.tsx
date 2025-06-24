'use client';

import { useState, useEffect } from 'react';
import axios from '@/utils/axiosInstance';
import styles from './PastEvaluation.module.css';

interface PhaseData {
  id: string;
  name: string;
  period_name: string;
  start_date: string;
  end_date: string;
}

export default function SubmissionPeriod() {
  const [form, setForm] = useState<PhaseData>({
    id: '',
    name: '',
    period_name: '',
    start_date: '',
    end_date: '',
  });
  const [inEditMode, setIsEditMode] = useState(false);
  const [dateList, setDateList] = useState<PhaseData[]>([]);
  const quarters = ['1', '2', '3', '4'];

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await axios.get('/api/submission_periods');
        const formatted = res.data.map((item: any) => ({
          id: String(item.id),
          name: String(item.phaseNumber),
          period_name: item.periodName,
          start_date: item.startDate,
          end_date: item.endDate,
        }));
        setDateList(formatted);
      } catch (err) {
        console.error('取得中にエラー:', err);
      }
    };
    fetchData();
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleEdit = (item: PhaseData) => {
    setForm(item);
    setIsEditMode(true);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const isValid =
    form.name &&
    !isNaN(Number(form.name)) &&
    Number(form.name) > 0 &&
    form.period_name &&
    form.start_date &&
    form.end_date &&
    new Date(form.start_date) <= new Date(form.end_date);

  const handleSubmit = async () => {
    const endpoint = inEditMode ? '/api/submission_period_edit' : '/api/submission_period';
    const method = inEditMode ? 'put' : 'post';
    const payload = {
      id: form.id || null,
      phaseNumber: Number(form.name),
      periodName: form.period_name,
      startDate: form.start_date,
      endDate: form.end_date,
    };

    try {
      const res = await axios({ url: endpoint, method, data: payload });
      alert(inEditMode ? '編集が完了しました' : '登録が完了しました');

      if (inEditMode) {
        setDateList(prev =>
          prev.map(item => (item.id === form.id ? { ...form } : item))
        );
      } else {
        const newId = res.data.id || String(Date.now());
        setDateList(prev => [...prev, { ...form, id: newId }]);
      }

      setForm({ id: '', name: '', period_name: '', start_date: '', end_date: '' });
      setIsEditMode(false);
    } catch (err: any) {
      console.error(err);
      alert(err.response?.data?.message || 'エラーが発生しました');
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.titleBar}>
        <h1>提出期間編集</h1>
      </div>
      <div className={styles.descriptionBox}>
        <p>
          【操作方法】新規登録または右の編集ボタンで提出期間を編集できます。
        </p>
      </div>
      <div className={styles.mainContent}>
        {/* 左：フォーム */}
        <aside className={styles.sidebar}>
          <div className={styles.scoreTableWrapper}>
            <h2 style={{ marginBottom: 16 }}>{inEditMode ? '編集中' : '新規評価依頼登録'}</h2>
            <div style={{ marginBottom: 16 }}>
              <label>評価期</label>
              <input
                type="text"
                name="name"
                value={form.name}
                onChange={handleChange}
                placeholder="例：18"
                className={styles.termDropdownButton}
                style={{ marginTop: 4, marginBottom: 8 }}
              />
              {form.name && (isNaN(Number(form.name)) || Number(form.name) <= 0) && (
                <p className={styles.errorMsg}>1以上の整数を入力してください</p>
              )}
            </div>
            <div style={{ marginBottom: 16 }}>
              <label>クォーター</label>
              <select
                name="period_name"
                value={form.period_name}
                onChange={handleChange}
                className={styles.termDropdownButton}
                style={{ marginTop: 4, marginBottom: 8 }}
              >
                <option value="">クォーターを選択してください</option>
                {quarters.map(q => (
                  <option key={q} value={q}>{q}</option>
                ))}
              </select>
            </div>
            <div style={{ marginBottom: 16 }}>
              <label>開始日</label>
              <input
                type="date"
                name="start_date"
                value={form.start_date}
                onChange={handleChange}
                className={styles.termDropdownButton}
                style={{ marginTop: 4, marginBottom: 8 }}
              />
            </div>
            <div style={{ marginBottom: 16 }}>
              <label>終了日</label>
              <input
                type="date"
                name="end_date"
                value={form.end_date}
                onChange={handleChange}
                className={styles.termDropdownButton}
                style={{ marginTop: 4, marginBottom: 8 }}
              />
            </div>
            <button
              type="button"
              onClick={handleSubmit}
              disabled={!isValid}
              className={styles.termDropdownButton}
              style={{
                backgroundColor: isValid ? '#0070f3' : '#ccc',
                color: isValid ? 'white' : '#888',
                fontWeight: 'bold',
                cursor: isValid ? 'pointer' : 'not-allowed',
              }}
            >
              {inEditMode ? '更新' : '登録'}
            </button>
          </div>
        </aside>

        {/* 右：テーブル */}
        <div className={styles.content}>
          <div className={styles.scoreTableWrapper}>
            <h2>提出期間一覧</h2>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th>評価期</th>
                  <th>クォーター</th>
                  <th>開始日</th>
                  <th>終了日</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                {dateList.length > 0 ? (
                  dateList.map((item, index) => (
                    <tr key={index}>
                      <td>{item.name}期</td>
                      <td>{item.period_name}</td>
                      <td>{item.start_date}</td>
                      <td>{item.end_date}</td>
                      <td>
                        <button
                          onClick={() => handleEdit(item)}
                          className={styles.snippetButton}
                        >
                          編集
                        </button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan={5} className={styles.noDataMsg}>データがありません</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}
