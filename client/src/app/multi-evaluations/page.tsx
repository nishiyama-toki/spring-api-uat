//page.tsx
import { Suspense } from 'react'
import MultiEvaluations from './MultiEvaluations'


// `useSearchParams` を使うClient ComponentをSuspenseでラップするページコンポーネント
export default function MultiEvaluationsPage() {
  return (
    <Suspense fallback={<div>読み込み中...</div>}>
      <MultiEvaluations />
    </Suspense>
  )
}
