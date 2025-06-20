'use client'

import { useEffect, useState } from 'react'
import axiosInstance from '../../utils/axiosInstance'



// -------------------------
// 型定義
// -------------------------

// 評価対象者データ（GET /api/targets で受け取る構成）
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

// 評価データ（POSTで送る構成に準拠）
type Evaluation = {
  target_id: number
  skill_score: number | null
  business_score: number | null
  team_score: number | null
  comment: string
}

export default function MultiEvaluations() {
  // -------------------------
  // 状態管理
  // -------------------------
  const [targets, setTargets] = useState<Target[]>([])
  const [evaluations, setEvaluations] = useState<Evaluation[]>([])

  // -------------------------
  // 画面初期表示時：評価対象者一覧を取得
  // -------------------------
  useEffect(() => {
    // 🔁 axiosInstance（utils/axiosInstance.ts）を使って対象者一覧を取得
    axiosInstance
      .get('/api/multi-evaluations/targets')
      .then((res) => {
        const data: Target[] = res.data
        console.log('targets:', data)

        const initialEvaluations = data.map((t) => ({
          target_id: t.id,
          skill_score: t.evaluation?.skill_score ?? null,
          business_score: t.evaluation?.business_score ?? null,
          team_score: t.evaluation?.team_score ?? null,
          comment: t.evaluation?.comment ?? '',
        }))
        console.log('evaluations:', initialEvaluations)

        setTargets(data)
        setEvaluations(initialEvaluations)
      })
      .catch((err) => {
        console.error(err)
        alert('⛔ 認証エラー：再ログインしてください')
      })
  }, [])

  // -------------------------
  // 入力変更時：数値 or コメント欄を更新
  // -------------------------
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
      if (!isNaN(parsed)) {
        updated[index][field] = parsed
      } else {
        updated[index][field] = null
      }
    }

    setEvaluations(updated)
  }

  // -------------------------
  // 送信ボタンクリック時：POST送信処理
  // -------------------------
  const handleSubmit = async () => {
  try {
    // 🔽 axiosInstance を使えば、トークンや credentials は内部で自動的に付与される
    const res = await axiosInstance.post('/api/multi-evaluations', {
      phase_id: 1,
      evaluations,
    })

    // 🔍 ステータスコードチェック（axios は自動で 4xx/5xx を throw する）
    if (!res || res.status >= 400) {
      alert('送信に失敗しました')
      return
    }

    // ✅ 正常に送信された場合
    alert(res.data?.message || '送信完了')
  } catch (err: any) {
    // ❌ サーバーエラー or ネットワークエラーなど
    if (err.response?.data?.message) {
      alert(err.response.data.message)
    } else {
      alert('サーバーに接続できませんでした')
    }
  }
}
  
  // -------------------------
  // HTML
  // -------------------------
  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-6">多面評価</h1>

      {targets.length > 0 && evaluations.length === targets.length && targets.map((t, i) => (
        <div key={t.id} className="mb-6 border-b pb-4">
          <p className="font-semibold">
            {t.name}（{t.role}）
          </p>

          <div className="mt-2 space-y-2">
            <label>
              スキル：
              <input
                type="number"
                step="0.1"
                min="1.0"
                max="5.0"
                value={evaluations[i].skill_score ?? ''}
                onChange={(e) => handleChange(i, 'skill_score', e.target.value)}
                className="ml-2 border px-2 py-1 w-24"
              />
            </label>
            <br />

            <label>
              ビジネス：
              <input
                type="number"
                step="0.1"
                min="1.0"
                max="5.0"
                value={evaluations[i].business_score ?? ''}
                onChange={(e) => handleChange(i, 'business_score', e.target.value)}
                className="ml-2 border px-2 py-1 w-24"
              />
            </label>
            <br />

            <label>
              チームマネジメント：
              <input
                type="number"
                step="0.1"
                min="1.0"
                max="5.0"
                value={evaluations[i].team_score ?? ''}
                onChange={(e) => handleChange(i, 'team_score', e.target.value)}
                className="ml-2 border px-2 py-1 w-24"
              />
            </label>
            <br />

            <label>
              コメント：
              <textarea
                value={evaluations[i].comment || ''}
                onChange={(e) => handleChange(i, 'comment', e.target.value)}
                className="block border w-full p-2 mt-1"
              />
              <small className="text-sm text-gray-500">
                {evaluations[i].comment.length}/1000
              </small>
            </label>
          </div>
        </div>
      ))}

      <button
        onClick={handleSubmit}
        className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
      >
        送信
      </button>
    </div>
  )
}
