'use client'

import { useEffect, useState } from 'react'
import styles from './multi-evaluations.module.css'
import axios from '@/utils/axiosInstance'
import { isAxiosError } from 'axios'
import { useRouter, useSearchParams } from 'next/navigation'
import { useSessionTimeout } from '@/hooks/useSessionTimeout'

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

type PhaseResponse = {
  phaseId: number
  phaseNumber: number
  name: string
  startDate: string
  endDate: string
  closed: boolean
}

export default function MultiEvaluations() {
  const router = useRouter()
  const searchParams = useSearchParams()

  const phaseNumber = parseInt(searchParams.get('phase') || '0', 10)
  const quarterName = searchParams.get('quarter') || ''

  const [phaseLabel, setPhaseLabel] = useState('多面評価')
  const [targets, setTargets] = useState<Target[]>([])
  const [selectedTargetId, setSelectedTargetId] = useState<number | null>(null)
  const [evaluations, setEvaluations] = useState<Evaluation[]>([])

  useSessionTimeout(30)

  useEffect(() => {
    if (phaseNumber > 0) {
      axios.get<PhaseResponse>(`/api/phases/${phaseNumber}`)
        .then(res => {
          const p = res.data
          setPhaseLabel(`${p.phaseNumber}期 ${p.name} 多面評価`)
        })
        .catch(() => {
          if (phaseNumber && quarterName) {
            setPhaseLabel(`${phaseNumber}期 ${quarterName} 多面評価`)
          } else {
            setPhaseLabel('多面評価')
          }
        })
    }
  }, [phaseNumber, quarterName])

  useEffect(() => {
    if (phaseNumber > 0) {
      axios
        .get<Target[]>('/api/multi-evaluations/targets', { params: { phase_id: phaseNumber } })
        .then(res => setTargets(res.data))
        .catch(err => {
          console.error('評価対象者の取得に失敗', err)
          if (isAxiosError(err) && err.response?.data?.message) {
            alert(`エラー: ${err.response.data.message}`)
          } else {
            alert('評価対象者の取得エラー：サーバーに接続できませんでした')
          }
        })
    }
  }, [phaseNumber])

  useEffect(() => {
    if (!selectedTargetId) {
      setEvaluations([])
      return
    }
    const t = targets.find(tt => tt.id === selectedTargetId)
    if (!t) return

    setEvaluations([{
      target_id: t.id,
      skill_score: t.evaluation?.skill_score ?? null,
      business_score: t.evaluation?.business_score ?? null,
      team_score: t.evaluation?.team_score ?? null,
      comment: t.evaluation?.comment ?? '',
    }])
  }, [selectedTargetId, targets])

  const handleChange = (
    _idx: number,
    field: 'skill_score' | 'business_score' | 'team_score' | 'comment',
    value: string
  ) => {
    if (!evaluations.length) return
    const cur = { ...evaluations[0] }

    if (field === 'comment') {
      cur.comment = value.slice(0, 1000)
    } else {
      const n = parseFloat(value)
      cur[field] = isNaN(n) ? null : n
    }

    setEvaluations([cur])
  }

  const handleSubmit = async () => {
    if (!selectedTargetId || !evaluations.length) {
      alert('評価対象者を選択してください')
      return
    }

    try {
      await axios.post('/api/multi-evaluations', {
        phase_id: phaseNumber,
        evaluations: evaluations,
      })
      alert('送信完了')
      router.push('/submitted')
    } catch (err) {
      if (isAxiosError(err) && err.response?.data?.message) {
        alert(err.response.data.message)
      } else {
        alert('サーバーに接続できませんでした')
      }
    }
  }

  return (
    <div className={styles.container}>
      <div className={styles.titleBar}>
        <h1>{phaseLabel}</h1>
      </div>

      <div className={styles.mainContent}>
        <div className={styles.content}>
          {targets.length > 0 ? (
            <div>
              <label htmlFor="target-select">評価対象者を選択:</label>
              <select
                id="target-select"
                value={selectedTargetId ?? ''}
                onChange={e => {
                  const id = parseInt(e.target.value, 10)
                  setSelectedTargetId(id > 0 ? id : null)
                }}
              >
                <option value="">-- 選択してください --</option>
                {targets.map(t => (
                  <option key={t.id} value={t.id}>
                    {t.name} ({t.role})
                  </option>
                ))}
              </select>
            </div>
          ) : (
            <p>評価可能な対象者が見つかりません。</p>
          )}

          {selectedTargetId && evaluations.length > 0 && (
            <div>
              <p>
                {targets.find(t => t.id === selectedTargetId)?.name}（
                {targets.find(t => t.id === selectedTargetId)?.role}）の評価
              </p>

              <div className={styles.inputGroup}>
                <label>
                  スキル：
                  <input
                    type="number"
                    step="0.1"
                    min="1.0"
                    max="5.0"
                    value={evaluations[0].skill_score ?? ''}
                    onChange={e => handleChange(0, 'skill_score', e.target.value)}
                  />
                </label>

                <label>
                  ビジネス：
                  <input
                    type="number"
                    step="0.1"
                    min="1.0"
                    max="5.0"
                    value={evaluations[0].business_score ?? ''}
                    onChange={e => handleChange(0, 'business_score', e.target.value)}
                  />
                </label>

                <label>
                  チームマネジメント：
                  <input
                    type="number"
                    step="0.1"
                    min="1.0"
                    max="5.0"
                    value={evaluations[0].team_score ?? ''}
                    onChange={e => handleChange(0, 'team_score', e.target.value)}
                  />
                </label>

                <label>
                  コメント：
                  <textarea
                    value={evaluations[0].comment || ''}
                    onChange={e => handleChange(0, 'comment', e.target.value)}
                    maxLength={1000}
                  />
                  <small>
                    {evaluations[0].comment?.length ?? 0}/1000
                  </small>
                </label>
              </div>
            </div>
          )}

          {selectedTargetId && (
            <button onClick={handleSubmit} className={styles.submitButton}>
              送信
            </button>
          )}
        </div>
      </div>
    </div>
  )
}
