import { Suspense } from 'react'
import ResetPasswordClient from './ResetPasswordClient'

export default function ResetPasswordPage() {
  return (
    <Suspense fallback={<div>読み込み中...</div>}>
      <ResetPasswordClient />
    </Suspense>
  )
}
