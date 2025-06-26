'use client'

import { useState, useEffect } from 'react'
import axios from '@/utils/axiosInstance'
import { useSessionTimeout } from '@/hooks/useSessionTimeout'
import styles from './PastEvaluation.module.css'

interface PhaseData {
  id: string
  name: string
  period_name: string
  start_date: string
  end_date: string
}

export default function SubmissionPeriod() {
  useSessionTimeout(30);

  const [form, setForm] = useState<PhaseData>({
    id: '',
    name: '',
    period_name: '',
    start_date: '',
    end_date: '',
  })
  const [inEditMode, setIsEditMode] = useState(false)
  const [dateList, setDateList] = useState<PhaseData[]>([])
  const quarters = ['1', '2', '3', '4']

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await axios.get('/api/submission_periods')
        const formatted = res.data.map((item: any) => ({
          id: String(item.id),
          name: String(item.phaseNumber),
          period_name: item.periodName,
          start_date: item.startDate,
          end_date: item.endDate,
        }))
        setDateList(formatted)
      } catch (err) {
        console.error('取得中にエラー:', err)
      }
    }
    fetchData()
  }, [])

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  const handleEdit = (item: PhaseData) => {
    setForm(item)
    setIsEditMode(true)
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  const isValid =
    form.name &&
    !isNaN(Number(form.name)) &&
    Number(form.name) > 0 &&
    form.period_name &&
    form.start_date &&
    form.end_date &&
    new Date(form.start_date) <= new Date(form.end_date)

  const handleSubmit = async () => {
    const endpoint = inEditMode ? '/api/submission_period_edit' : '/api/submission_period'
    const method = inEditMode ? 'put' : 'post'
    const payload = {
      id: form.id || null,
      phaseNumber: Number(form.name),
      periodName: form.period_name,
      startDate: form.start_date,
      endDate: form.end_date,
    }

    try {
      const res = await axios({ url: endpoint, method, data: payload })
      alert(inEditMode ? '編集が完了しました' : '登録が完了しました')

      if (inEditMode) {
        setDateList(prev =>
          prev.map(item => (item.id === form.id ? { ...form } : item))
        )
      } else {
        const newId = res.data.id || String(Date.now())
        setDateList(prev => [...prev, { ...form, id: newId }])
      }

      setForm({ id: '', name: '', period_name: '', start_date: '', end_date: '' })
      setIsEditMode(false)
    } catch (err: any) {
      console.error(err)
      alert(err.response?.data?.message || 'エラーが発生しました')
    }
  }

  return (
    <div className={styles.container}>
      <div className={styles.mainContent}>
        {/* 左カラム：フォーム */}
        <div className={`${styles.content} ${styles.formPane}`}>
          <div className={styles.titleBar}>
            <h1>{inEditMode ? '編集中' : '新規評価依頼登録'}</h1>
          </div>
          <form
            className={styles.formVertical}
            onSubmit={e => { e.preventDefault(); handleSubmit(); }}
            autoComplete="off"
          >
            <div>
              <label className={styles.inputLabel} htmlFor="phase">
                評価期
              </label>
              <input
                type="text"
                id="phase"
                name="name"
                value={form.name}
                onChange={handleChange}
                placeholder="例：18"
                className={styles.inputNum}
                autoComplete="off"
              />
              {form.name && (isNaN(Number(form.name)) || Number(form.name) <= 0) && (
                <p className={styles.errorMsg}>1以上の整数を入力してください</p>
              )}
            </div>

            <div>
              <label className={styles.inputLabel} htmlFor="period_name">
                クォーター
              </label>
              <select
                id="period_name"
                name="period_name"
                value={form.period_name}
                onChange={handleChange}
                className={styles.selectInput}
              >
                <option value="">クォーターを選択してください</option>
                {quarters.map(q => (
                  <option key={q} value={q}>{q}</option>
                ))}
              </select>
            </div>

            <div>
              <label className={styles.inputLabel} htmlFor="start_date">
                開始日
              </label>
              <input
                type="date"
                id="start_date"
                name="start_date"
                value={form.start_date}
                onChange={handleChange}
                className={styles.dateInput}
              />
            </div>

            <div>
              <label className={styles.inputLabel} htmlFor="end_date">
                終了日
              </label>
              <input
                type="date"
                id="end_date"
                name="end_date"
                value={form.end_date}
                onChange={handleChange}
                className={styles.dateInput}
              />
            </div>

            <button
              type="submit"
              disabled={!isValid}
              className={`${isValid ? styles.buttonActive : styles.buttonInactive} ${styles.submitButton}`}
            >
              {inEditMode ? '更新' : '登録'}
            </button>
          </form>
        </div>

        {/* 右カラム：テーブル */}
        <div className={`${styles.content} ${styles.tablePane}`}>
          <div className={styles.titleBar}>
            <h1>提出期間編集</h1>
          </div>
          <div className={styles.scoreTableWrapper}>
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
                          className={styles.editButton}
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
  )
}