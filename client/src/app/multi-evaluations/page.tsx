'use client'

import { useEffect, useState } from 'react'
import axiosInstance from '../../utils/axiosInstance'
import styles from './multi-evaluations.module.css'

// 型定義
type Target = {
  id: number
  name: string
  role: string
  evaluation?: {
    skill_score?: number
    business_score?: number
    team_score?: number
    comment?: string
  }
}

type Evaluation = {
  target_id: number
  skill_score: number | null
  business_score: number | null
  team_score: number | null
  comment: string
}

export default function MultiEvaluations() {
  const [targets, setTargets] = useState<Target[]>([])
  const [evaluations, setEvaluations] = useState<Evaluation[]>([])

  useEffect(() => {
    axiosInstance
      .get('/api/multi-evaluations/targets')
      .then((res) => {
        const data: Target[] = res.data
        const initialEvaluations = data.map((t) => ({
          target_id: t.id,
          skill_score: t.evaluation?.skill_score ?? null,
          business_score: t.evaluation?.business_score ?? null,
          team_score: t.evaluation?.team_score ?? null,
          comment: t.evaluation?.comment ?? '',
        }))
        setTargets(data)
        setEvaluations(initialEvaluations)
      })
      .catch((err) => {
        console.error(err)
        alert('⛔ 認証エラー：再ログインしてください')
      })
  }, [])

  const handleChange = (
    index: number,
    field: 'skill_score' | 'business_score' | 'team_score' | 'comment',
    value: string
  ) => {
    const updated = [...evaluations]
    if (field === 'comment') {
      updated[index][field] = value.slice(0, 255)
    } else {
      const parsed = parseFloat(value)
      updated[index][field] = isNaN(parsed) ? null : parsed
    }
    setEvaluations(updated)
  }

  const handleSubmit = async () => {
    try {
      const res = await axiosInstance.post('/api/multi-evaluations', {
        phase_id: 1,
        evaluations,
      })
      if (!res || res.status >= 400) {
        alert('送信に失敗しました')
        return
      }
      alert(res.data?.message || '送信完了')
    } catch (err: any) {
      alert(err.response?.data?.message || 'サーバーに接続できませんでした')
    }
  }

  return (
    <div className={styles.container}>
      <h1 className={styles.title}>多面評価</h1>

      {targets.length > 0 && evaluations.length === targets.length && targets.map((t, i) => (
        <div key={t.id} className={styles.evaluationBlock}>
          <p className={styles.name}>
            {t.name}（{t.role}）
          </p>

          <div className={styles.inputGroup}>
            <label>
              スキル：
              <input
                type="number"
                step="0.1"
                min="1.0"
                max="5.0"
                value={evaluations[i].skill_score ?? ''}
                onChange={(e) => handleChange(i, 'skill_score', e.target.value)}
                className={styles.input}
              />
            </label>

            <label>
              ビジネス：
              <input
                type="number"
                step="0.1"
                min="1.0"
                max="5.0"
                value={evaluations[i].business_score ?? ''}
                onChange={(e) => handleChange(i, 'business_score', e.target.value)}
                className={styles.input}
              />
            </label>

            <label>
              チームマネジメント：
              <input
                type="number"
                step="0.1"
                min="1.0"
                max="5.0"
                value={evaluations[i].team_score ?? ''}
                onChange={(e) => handleChange(i, 'team_score', e.target.value)}
                className={styles.input}
              />
            </label>

            <label>
              コメント：
              <textarea
                value={evaluations[i].comment || ''}
                onChange={(e) => handleChange(i, 'comment', e.target.value)}
                className={styles.textarea}
              />
              <small className={styles.note}>
                {evaluations[i].comment.length}/1000
              </small>
            </label>
          </div>
        </div>
      ))}

      <button onClick={handleSubmit} className={styles.button}>
        送信
      </button>
    </div>
  )
}
