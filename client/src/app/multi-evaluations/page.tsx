// client/src/app/multi-evaluations/page.tsx

import { Suspense } from 'react'
import MultiEvaluationsClient from './MultiEvaluationsClient'

// `useSearchParams` を使うClient ComponentをSuspenseでラップするページコンポーネント
export default function MultiEvaluationsPage() {
  return (
    <Suspense fallback={<div>読み込み中...</div>}>
      <MultiEvaluationsClient />
    </Suspense>
  )
}
