'use client'

import { useEffect, useState } from 'react'
import axios from '@/utils/axiosInstance'; // 共通のaxiosインスタンスをデフォルトインポート
import { isAxiosError } from 'axios'; // <-- isAxiosError は 'axios' ライブラリから直接インポート
import { useSearchParams } from 'next/navigation'; // URLパラメータ取得用

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

export default function MultiEvaluations() {
  // -------------------------
  // URLパラメータの取得
  // -------------------------
  const searchParams = useSearchParams();
  const phaseNumber = parseInt(searchParams.get('phase') || '0', 10); // フェーズ番号 (必須)
  const quarterName = searchParams.get('quarter') || ''; // クォーター名 (必須)
  // targetIdFromUrl, targetNameFromUrl はURLから読み取るが、この画面では主に選択UIを使用するため、直接は使わないことが多い
  // const targetIdFromUrl = parseInt(searchParams.get('targetId') || '0', 10);
  // const targetNameFromUrl = searchParams.get('targetName') || '';

  // -------------------------
  // 状態管理
  // -------------------------
  const [targets, setTargets] = useState<Target[]>([]) // 評価可能な対象者リスト
  const [evaluations, setEvaluations] = useState<Evaluation[]>([]) // 現在入力中の評価データ（単一のターゲット用）
  const [pageHeading, setPageHeading] = useState('多面評価'); // ページ見出し
  const [selectedTargetId, setSelectedTargetId] = useState<number | null>(null); // ユーザーが選択した評価対象者ID

  // -------------------------
  // 画面初期表示時：評価可能な対象者一覧を取得
  // -------------------------
  useEffect(() => {
    // 見出しの設定
    if (phaseNumber > 0 && quarterName) {
      setPageHeading(`${phaseNumber}期 ${quarterName} 多面評価`); // 見出しから個人名を削除
    } else {
      setPageHeading('多面評価');
    }

    // フェーズ番号が有効な場合のみAPIを呼び出す
    if (phaseNumber > 0) {
      // 🔽 /api/multi-evaluations/targets を呼び出して「評価可能な全対象者」一覧を取得
      // target_idパラメータはここでは渡さない。バックエンドは評価者とphaseに基づいて全対象候補を返す想定。
      axios
        .get(`/api/multi-evaluations/targets`, {
            params: { // phaseパラメータのみを渡す (バックエンドでevaluatorIdとphaseIdで絞り込む)
                phase: phaseNumber,
                // target_id: targetIdFromUrl, // ここはコメントアウトまたは削除
            }
        })
        .then((res) => {
          const data: Target[] = res.data
          console.log('取得した評価対象候補:', data)
          setTargets(data)
          // 評価対象者が複数いる場合、ここでデフォルトで最初のターゲットを選択することも可能
          // if (data.length > 0) {
          //   setSelectedTargetId(data[0].id);
          // }
        })
        .catch((err) => {
          console.error('評価対象者の取得に失敗しました', err)
          // isAxiosError を明示的にインポートしたので、直接呼び出す
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
              // 選択されたターゲットの既存評価データをロード
              if (selectedTarget.evaluation) {
                  setEvaluations([{
                      target_id: selectedTarget.id,
                      skill_score: selectedTarget.evaluation.skill_score ?? null,
                      business_score: selectedTarget.evaluation.business_score ?? null,
                      team_score: selectedTarget.evaluation.team_score ?? null,
                      comment: selectedTarget.evaluation.comment ?? '',
                  }]);
              } else {
                  // 新規評価の場合、フォームを初期化
                  setEvaluations([{
                      target_id: selectedTarget.id,
                      skill_score: null, business_score: null, team_score: null, comment: ''
                  }]);
              }
          }
      } else {
          setEvaluations([]); // 選択が解除されたらフォームをクリア
      }
  }, [selectedTargetId, targets]);


  // -------------------------
  // 入力変更時：数値 or コメント欄を更新
  // -------------------------
  const handleChange = (
    // indexは常に0になるはず（evaluations配列は常に1要素だから）
    index: number,
    field: 'skill_score' | 'business_score' | 'team_score' | 'comment',
    value: string
  ) => {
    if (evaluations.length === 0) return; // 評価対象が選択されていない場合

    const currentEvaluation = { ...evaluations[0] }; // 現在入力中の評価を取得

    if (field === 'comment') {
      currentEvaluation[field] = value.slice(0, 1000); // コメントはmaxLengthに合わせて255->1000
    } else {
      const parsed = parseFloat(value);
      if (!isNaN(parsed)) {
        currentEvaluation[field] = parsed;
      } else {
        currentEvaluation[field] = null;
      }
    }
    setEvaluations([currentEvaluation]); // 常に配列の0番目を更新してstateにセット
  }

  // -------------------------
  // 送信ボタンクリック時：POST送信処理
  // -------------------------
  const handleSubmit = async () => {
    if (!selectedTargetId || evaluations.length === 0 || !evaluations[0].target_id) {
        alert('評価対象者を選択し、入力内容を確認してください。');
        return;
    }

    try {
        // 送信する評価データは現在選択されている1つのターゲットに対するもの
        const evaluationToSend: Evaluation = {
            target_id: evaluations[0].target_id, // evaluations[0].target_idを使用
            skill_score: evaluations[0].skill_score,
            business_score: evaluations[0].business_score,
            team_score: evaluations[0].team_score,
            comment: evaluations[0].comment,
        };

        // 🔽 axiosInstance を使えば、トークンや credentials は内部で自動的に付与される
        const res = await axios.post('/api/multi-evaluations', {
            phase_id: phaseNumber, // URLから取得したphaseNumberを使用
            evaluations: [evaluationToSend], // 送信はリスト形式なので、1要素のリストとして渡す
        })

        // 🔍 ステータスコードチェック（axios は自動で 4xx/5xx を throw する）
        if (!res || res.status >= 400) {
            alert('送信に失敗しました');
            return;
        }

        // ✅ 正常に送信された場合
        alert(res.data?.message || '送信完了');
        // 成功後、フォームをクリアするか、別ページにリダイレクトする
        // setEvaluations([]);
        // setSelectedTargetId(null);
        // router.push('/submitted'); // 例: 送信完了ページへ遷移
    } catch (err: any) {
        // ❌ サーバーエラー or ネットワークエラーなど
        if (isAxiosError(err) && err.response?.data?.message) { // <-- isAxiosError を直接使用
            alert(err.response.data.message);
        } else {
            alert('サーバーに接続できませんでした');
        }
    }
  }

  // -------------------------
  // HTML
  // -------------------------
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
              setSelectedTargetId(id > 0 ? id : null); // 0以下の値はnullにする
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

      {/* 選択されたターゲットがあり、evaluationsにデータがある場合のみフォームを表示 */}
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
                maxLength={1000} // HTMLのmaxLength属性を追加
              />
              <small className="text-sm text-gray-500">
                {evaluations[0].comment?.length ?? 0}/1000 {/* null安全なアクセス */}
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