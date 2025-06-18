'use client'
import { useState } from 'react'
import { useRouter } from 'next/navigation'

export default function LoginPage() {
  const router = useRouter()
  const [email, setEmail] = useState('')
  const [passwd, setPasswd] = useState('')
  const [error, setError] = useState('')

  const handleLogin = () => {
    // 仮の判定（本番API時はここをfetch/axiosで書き換え）
    if (email === 'admin@example.com') {
      localStorage.setItem('permission', 'admin')
      router.push('/admin')
    } else {
      localStorage.setItem('permission', 'user')
      router.push('/home')
    }
  }

  return (
    <div style={{ maxWidth: 400, margin: "40px auto" }}>
      <h2>ログイン（ダミー）</h2>
      <input type="email" value={email} onChange={e => setEmail(e.target.value)} placeholder="Email" /><br />
      <input type="password" value={passwd} onChange={e => setPasswd(e.target.value)} placeholder="Password" /><br />
      <button onClick={handleLogin}>Login</button>
      {error && <div style={{ color: 'red' }}>{error}</div>}
    </div>
  )
}
