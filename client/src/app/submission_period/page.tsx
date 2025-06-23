'use client'

import { useState, useEffect } from 'react'
import axios from '@/utils/axiosInstance'

interface PhaseData {
  id: string
  name: string
  period_name: string
  start_date: string
  end_date: string
}

export default function SubmissionPeriod() {
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

  // 初期データ取得（DBから）
  useEffect(() => {
    const fetchData = async () => {
      try {
        // any → 明示的な型に変更
        const res = await axios.get<PhaseData[]>('/api/submission_periods')

        const formatted = res.data.map((item) => ({
          id: String(item.id),
          name: String(item.name),
          period_name: item.period_name,
          start_date: item.start_date,
          end_date: item.end_date,
        }))
        setDateList(formatted)
      } catch (err) {
        console.error('取得中にエラー:', err)
      }
    }
    fetchData()
  }, [])


  // 入力変更
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  // 編集モードに切り替え
  const handleEdit = (item: PhaseData) => {
    setForm(item)
    setIsEditMode(true)
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  // バリデーション（整数チェックと日付妥当性）
  const isValid =
    form.name &&
    !isNaN(Number(form.name)) &&
    Number(form.name) > 0 &&
    form.period_name &&
    form.start_date &&
    form.end_date &&
    new Date(form.start_date) <= new Date(form.end_date)

  // 登録 or 更新
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

      // 初期化
      setForm({ id: '', name: '', period_name: '', start_date: '', end_date: '' })
      setIsEditMode(false)
    } catch (err: any) {
      console.error(err)
      alert(err.response?.data?.message || 'エラーが発生しました')
    }
  }

  return (
    <div className="flex gap-8 p-6 max-w-6xl mx-auto">
      {/* 左：フォーム */}
      <div className="w-1/2 border-r pr-4">
        <h2 className="text-lg font-bold mb-4">
          {inEditMode ? '編集中' : '新規評価依頼登録'}
        </h2>

        <div className="mb-4">
          <label htmlFor="phase" className="block mb-1">評価期</label>
          <input
            type="text"
            id="phase"
            name="name"
            value={form.name}
            onChange={handleChange}
            placeholder="例：18"
            className="w-full border p-2 rounded"
          />
          {form.name && (isNaN(Number(form.name)) || Number(form.name) <= 0) && (
            <p className="text-red-500 text-sm mt-1">1以上の整数を入力してください</p>
          )}
        </div>

        <div className="mb-4">
          <label htmlFor="period_name" className="block mb-1">クォーター</label>
          <select
            id="period_name"
            name="period_name"
            value={form.period_name}
            onChange={handleChange}
            className="w-full border p-2 rounded"
          >
            <option value="">クォーターを選択してください</option>
            {quarters.map(q => (
              <option key={q} value={q}>{q}</option>
            ))}
          </select>
        </div>

        <div className="mb-4">
          <label htmlFor="start_date" className="block mb-1">開始日</label>
          <input
            type="date"
            id="start_date"
            name="start_date"
            value={form.start_date}
            onChange={handleChange}
            className="w-full border p-2 rounded"
          />
        </div>

        <div className="mb-4">
          <label htmlFor="end_date" className="block mb-1">終了日</label>
          <input
            type="date"
            id="end_date"
            name="end_date"
            value={form.end_date}
            onChange={handleChange}
            className="w-full border p-2 rounded"
          />
        </div>

        <button
          type="button"
          onClick={handleSubmit}
          disabled={!isValid}
          className={`w-full p-2 rounded font-bold ${isValid ? 'bg-blue-600 text-white' : 'bg-gray-300 text-gray-600'}`}
        >
          {inEditMode ? '更新' : '登録'}
        </button>
      </div>

      {/* 右：テーブル */}
      <div className="w-1/2 pl-4">
        <h2 className="text-lg font-bold mb-4">提出期間編集</h2>
        <table className="w-full table-auto border border-collapse">
          <thead>
            <tr className="bg-gray-100">
              <th className="border px-2 py-1">評価期</th>
              <th className="border px-2 py-1">クォーター</th>
              <th className="border px-2 py-1">開始日</th>
              <th className="border px-2 py-1">終了日</th>
              <th className="border px-2 py-1">操作</th>
            </tr>
          </thead>
          <tbody>
            {dateList.length > 0 ? (
              dateList.map((item, index) => (
                <tr key={index}>
                  <td className="border px-2 py-1 text-center">{item.name}期</td>
                  <td className="border px-2 py-1 text-center">{item.period_name}</td>
                  <td className="border px-2 py-1 text-center">{item.start_date}</td>
                  <td className="border px-2 py-1 text-center">{item.end_date}</td>
                  <td className="border px-2 py-1 text-center">
                    <button
                      onClick={() => handleEdit(item)}
                      className="bg-blue-100 hover:bg-blue-200 text-sm px-2 py-1 rounded"
                    >
                      編集
                    </button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={5} className="text-center py-4">データがありません</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  )
}
