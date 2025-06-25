// client/src/app/multi-evaluations/MultiEvaluationsClient.tsx

'use client'

import { useEffect, useState } from 'react'
//import axiosInstance from '../../utils/axiosInstance'

import axios from '@/utils/axiosInstance'; // 共通のaxiosインスタンスをデフォルトインポート
import { isAxiosError } from 'axios'; // <-- isAxiosError は 'axios' ライブラリから直接インポート
import { useSearchParams } from 'next/navigation'; // URLパラメータ取得用
import { useSessionTimeout } from '@/hooks/useSessionTimeout';

// -------------------------
// 型定義
// -------------------------

// 評価対象者データ（GET /api/multi-evaluations/targets で受け取る構成）
type Target = {
  id: number
  name: string
  role: string // Employeeエンティティのpermissionフィールドに対応
  evaluation?: { // 既存の評価データがあれば含まれる
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

export default function MultiEvaluationsClient() {
  // -------------------------
  // URLパラメータの取得
  // -------------------------
  const searchParams = useSearchParams();
  const phaseNumber = parseInt(searchParams.get('phase') || '0', 10); // フェーズ番号 (必須)
  const quarterName = searchParams.get('quarter') || ''; // クォーター名 (必須)

  // -------------------------
  // 状態管理
  // -------------------------
  const [targets, setTargets] = useState<Target[]>([]) // 評価可能な対象者リスト
  const [evaluations, setEvaluations] = useState<Evaluation[]>([]) // 現在入力中の評価データ（単一のターゲット用）
  const [pageHeading, setPageHeading] = useState('多面評価'); // ページ見出し
  const [selectedTargetId, setSelectedTargetId] = useState<number | null>(null); // ユーザーが選択した評価対象者ID

  // -------------------------
  // useSessionTimeout カスタムフックの呼び出し (ここを追加)
  // -------------------------
  useSessionTimeout(30); // <-- JWT有効期限が30分の場合

  // -------------------------
  // 画面初期表示時：評価可能な対象者一覧を取得
  // -------------------------
  useEffect(() => {
    if (phaseNumber > 0 && quarterName) {
      setPageHeading(`${phaseNumber}期 ${quarterName} 多面評価`);
    } else {
      setPageHeading('多面評価');
    }

    if (phaseNumber > 0) {
      axios
        .get(`/api/multi-evaluations/targets`, {
          params: {
            phase: phaseNumber,
          }
        })
        .then((res) => {
          const data: Target[] = res.data
          console.log('取得した評価対象候補:', data)
          setTargets(data)
        })
        .catch((err) => {
          console.error('評価対象者の取得に失敗しました', err)
          if (isAxiosError(err) && err.response?.data?.message) {
            alert(`⛔ エラー: ${err.response.data.message}`);
          } else {
            alert('⛔ 評価対象者の取得エラー：サーバーに接続できませんでした');
          }
        })
    }
  }, [phaseNumber, quarterName])

  // selectedTargetId または targets が変更されたときに、フォームの入力値を更新
  useEffect(() => {
    if (selectedTargetId && targets.length > 0) {
      const selectedTarget = targets.find(t => t.id === selectedTargetId);
      if (selectedTarget) {
        if (selectedTarget.evaluation) {
          setEvaluations([{
            target_id: selectedTarget.id,
            skill_score: selectedTarget.evaluation.skill_score ?? null,
            business_score: selectedTarget.evaluation.business_score ?? null,
            team_score: selectedTarget.evaluation.team_score ?? null,
            comment: selectedTarget.evaluation.comment ?? '',
          }]);
        } else {
          setEvaluations([{
            target_id: selectedTarget.id,
            skill_score: null, business_score: null, team_score: null, comment: ''
          }]);
        }
      }
    } else {
      setEvaluations([]);
    }
  }, [selectedTargetId, targets]);

  const handleChange = (
    index: number,
    field: 'skill_score' | 'business_score' | 'team_score' | 'comment',
    value: string
  ) => {
    if (evaluations.length === 0) return;
    const currentEvaluation = { ...evaluations[0] };

    if (field === 'comment') {
      currentEvaluation[field] = value.slice(0, 1000);
    } else {
      const parsed = parseFloat(value);
      currentEvaluation[field] = isNaN(parsed) ? null : parsed;
    }
    setEvaluations([currentEvaluation]);
  }

  const handleSubmit = async () => {
    if (!selectedTargetId || evaluations.length === 0 || !evaluations[0].target_id) {
      alert('評価対象者を選択し、入力内容を確認してください。');
      return;
    }

    try {
      const evaluationToSend: Evaluation = { ...evaluations[0] };

      const res = await axios.post('/api/multi-evaluations', {
        phase_id: phaseNumber,
        evaluations: [evaluationToSend],
      })

      if (!res || res.status >= 400) {
        alert('送信に失敗しました');
        return;
      }

      alert(res.data?.message || '送信完了');
    } catch (err: any) {
      if (isAxiosError(err) && err.response?.data?.message) {
        alert(err.response.data.message);
      } else {
        alert('サーバーに接続できませんでした');
      }
    }
  }

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-6">{pageHeading}</h1>

      {/* 評価対象者を選択するUI */}
      {targets.length > 0 ? (
        <div className="mb-4">
          <label htmlFor="target-select" className="block text-lg font-medium text-gray-700">
            評価対象者を選択:
          </label>
          <select
            id="target-select"
            value={selectedTargetId || ''}
            onChange={(e) => {
              const id = parseInt(e.target.value, 10);
              setSelectedTargetId(id > 0 ? id : null);
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
      ) : (
        <p>評価可能な対象者が見つかりません。</p>
      )}

      {/* 評価フォーム */}
      {selectedTargetId && evaluations.length > 0 && (
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
                maxLength={1000}
              />
              <small className="text-sm text-gray-500">
                {evaluations[0].comment?.length ?? 0}/1000
              </small>
            </label>
          </div>
        </div>
      )}

      {selectedTargetId && (
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
