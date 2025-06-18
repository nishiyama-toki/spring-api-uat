'use client'
import { useState } from 'react'
import { useRouter } from 'next/navigation'

export default function ResetMailPage() {
  const router = useRouter()
  const [email, setEmail] = useState('')
  const handleSubmit = async () => {
    // 何もせず画面遷移のみ
    router.push('/reset_mail sent')
  }

  return (
    <div style={{ maxWidth: 400, margin: "40px auto" }}>
      <h2>リセットメール送信（ダミー）</h2>
      <input type="email" value={email} onChange={e => setEmail(e.target.value)} placeholder="メールアドレス" /><br />
      <button onClick={handleSubmit}>送信</button>
    </div>
  )
}
