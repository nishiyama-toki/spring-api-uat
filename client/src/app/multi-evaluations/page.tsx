'use client'

import { useEffect, useState } from 'react'
import axios from '@/utils/axiosInstance';
import { useSearchParams } from 'next/navigation';

// -------------------------
// 型定義
// -------------------------

// 評価対象者データ（GET /api/multi-evaluations/targets で受け取る構成）
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
  // URLパラメータの取得
  // -------------------------
  const searchParams = useSearchParams();
  const phaseNumber = parseInt(searchParams.get('phase') || '0', 10);
  const quarterName = searchParams.get('quarter') || '';
  // targetIdFromUrl, targetNameFromUrl はURLから読み取るが、この画面では主に選択UIを使用
  // const targetIdFromUrl = parseInt(searchParams.get('targetId') || '0', 10);
  // const targetNameFromUrl = searchParams.get('targetName') || '';

  // -------------------------
  // 状態管理
  // -------------------------
  const [targets, setTargets] = useState<Target[]>([])
  const [evaluations, setEvaluations] = useState<Evaluation[]>([])
  const [pageHeading, setPageHeading] = useState('多面評価');
  const [selectedTargetId, setSelectedTargetId] = useState<number | null>(null); // <-- 選択された評価対象者ID

  // -------------------------
  // 画面初期表示時：評価対象者一覧を取得
  // -------------------------
  useEffect(() => {
    // URLパラメータから見出しを生成
    if (phaseNumber > 0 && quarterName) {
      setPageHeading(`${phaseNumber}期 ${quarterName} 多面評価`); // <- 見出しから個人名を削除
    } else {
      setPageHeading('多面評価');
    }

    // 🔽 axios（utils/axiosInstance.ts）を使って「評価可能な全対象者」一覧を取得
    // URLから取得したtargetIdFromUrlはここでは渡さない
    axios
      .get(`/api/multi-evaluations/targets`, {
          params: { // phaseパラメータのみを渡す (バックエンドでevaluatorIdとphaseIdで絞り込む)
              phase: phaseNumber,
              // target_id: targetIdFromUrl, // <-- ここをコメントアウトまたは削除！
          }
      })
      .then((res) => {
        const data: Target[] = res.data
        console.log('targets:', data)
        setTargets(data)
        // 評価対象者が複数いる場合、ここで選択肢を提供するUIが必要
        // 例: if (data.length > 0) setSelectedTargetId(data[0].id); // 最初のターゲットをデフォルトで選択
        // 既存の評価を初期ロードする場合はここでも考慮
      })
      .catch((err) => {
        console.error(err)
        alert('⛔ 認証エラー：再ログインしてください')
      })
  }, [phaseNumber, quarterName])

  // 選択された対象者が変わったときに既存評価をロード
  useEffect(() => {
      if (selectedTargetId && targets.length > 0) {
          const selectedTarget = targets.find(t => t.id === selectedTargetId);
          if (selectedTarget && selectedTarget.evaluation) {
              setEvaluations([{
                  target_id: selectedTarget.id,
                  skill_score: selectedTarget.evaluation.skill_score ?? null,
                  business_score: selectedTarget.evaluation.business_score ?? null,
                  team_score: selectedTarget.evaluation.team_score ?? null,
                  comment: selectedTarget.evaluation.comment ?? '',
              }]);
          } else {
              // 新規評価の場合
              setEvaluations([{
                  target_id: selectedTargetId,
                  skill_score: null, business_score: null, team_score: null, comment: ''
              }]);
          }
      } else {
          setEvaluations([]); // 選択が解除されたらフォームをクリア
      }
  }, [selectedTargetId, targets]);


  // -------------------------
  // 入力変更時：数値 or コメント欄を更新
  // -------------------------
  const handleChange = (
    index: number, // 常に0になるはず (単一フォームのため)
    field: 'skill_score' | 'business_score' | 'team_score' | 'comment',
    value: string
  ) => {
    // evaluationsは通常1要素なのでevaluations[0]を更新
    if (evaluations.length === 0) return; // 評価対象が選択されていない場合
    const currentEvaluation = { ...evaluations[0] };

    if (field === 'comment') {
      currentEvaluation[field] = value.slice(0, 255);
    } else {
      const parsed = parseFloat(value);
      if (!isNaN(parsed)) {
        currentEvaluation[field] = parsed;
      } else {
        currentEvaluation[field] = null;
      }
    }
    setEvaluations([currentEvaluation]); // 常に配列の0番目を更新
  }

  // -------------------------
  // 送信ボタンクリック時：POST送信処理
  // -------------------------
  const handleSubmit = async () => {
    if (!selectedTargetId || evaluations.length === 0) {
        alert('評価対象者を選択してください。');
        return;
    }
    try {
        // 送信する評価データは現在選択されている1つのターゲットに対するもの
        const evaluationToSend = {
            ...evaluations[0],
            target_id: selectedTargetId // 選択されたtargetIdを確実にペイロードに含める
        };

        const res = await axios.post('/api/multi-evaluations', {
            phase_id: phaseNumber, // URLから取得したphaseNumberを使用
            evaluations: [evaluationToSend], // 送信はリスト形式なので、1要素のリストとして渡す
        })

        if (!res || res.status >= 400) {
            alert('送信に失敗しました');
            return;
        }

        alert(res.data?.message || '送信完了');
    } catch (err: any) {
        if (err.response?.data?.message) {
            alert(err.response.data.message);
        } else {
            alert('サーバーに接続できませんでした');
        }
    }
  }

  // -------------------------
  // HTML (多面評価対象者の選択UIを追加)
  // -------------------------
  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-6">{pageHeading}</h1>

      {targets.length > 0 && (
        <div className="mb-4">
          <label htmlFor="target-select" className="block text-lg font-medium text-gray-700">
            評価対象者を選択:
          </label>
          <select
            id="target-select"
            value={selectedTargetId || ''}
            onChange={(e) => {
              const id = parseInt(e.target.value, 10);
              setSelectedTargetId(id);
            }}
            className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm rounded-md"
          >
            <option value="">-- 選択してください --</option>
            {targets.map((target) => (
              <option key={target.id} value={target.id}>
                {target.name} ({target.role})
              </option>
            ))}
          </select>
        </div>
      )}

      {selectedTargetId && targets.length > 0 && evaluations.length > 0 && ( // 選択されたターゲットがあり、evaluationsにデータがある場合のみフォームを表示
        <div className="mb-6 border-b pb-4">
          <p className="font-semibold">
            {targets.find(t => t.id === selectedTargetId)?.name}（{targets.find(t => t.id === selectedTargetId)?.role}）の評価
          </p>

          <div className="mt-2 space-y-2">
            <label>
              スキル：
              <input
                type="number"
                step="0.1"
                min="1.0"
                max="5.0"
                value={evaluations[0].skill_score ?? ''}
                onChange={(e) => handleChange(0, 'skill_score', e.target.value)}
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
                value={evaluations[0].business_score ?? ''}
                onChange={(e) => handleChange(0, 'business_score', e.target.value)}
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
                value={evaluations[0].team_score ?? ''}
                onChange={(e) => handleChange(0, 'team_score', e.target.value)}
                className="ml-2 border px-2 py-1 w-24"
              />
            </label>
            <br />

            <label>
              コメント：
              <textarea
                value={evaluations[0].comment || ''}
                onChange={(e) => handleChange(0, 'comment', e.target.value)}
                className="block border w-full p-2 mt-1"
              />
              <small className="text-sm text-gray-500">
                {evaluations[0].comment.length}/1000
              </small>
            </label>
          </div>
        </div>
      )}

      {selectedTargetId && ( // 選択されたターゲットがある場合にのみ送信ボタンを表示
        <button
          onClick={handleSubmit}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
        >
          送信
        </button>
      )}
    </div>
  )
}