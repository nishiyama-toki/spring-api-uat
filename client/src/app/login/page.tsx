'use client'

import { useState } from 'react'
import axios from '@/utils/axiosInstance'
import { useRouter } from 'next/navigation'
import styles from './login.module.css' // ← ここを統一

export default function LoginPage() {
  const router = useRouter()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [emailError, setEmailError] = useState('')
  const [passwordError, setPasswordError] = useState('')
  const [authError, setAuthError] = useState('')

  const validate = () => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,20}$/
    let isValid = true

    if (!email) {
      setEmailError('必須項目です。')
      isValid = false
    } else if (!emailRegex.test(email)) {
      setEmailError('メールアドレスの形式が正しくありません。')
      isValid = false
    } else {
      setEmailError('')
    }

    if (!password) {
      setPasswordError('必須項目です。')
      isValid = false
    } else if (!passwordRegex.test(password)) {
      setPasswordError('大文字・小文字・数字・記号を含んだ8～20文字にしてください。')
      isValid = false
    } else {
      setPasswordError('')
    }

    return isValid
  }

  const handleLogin = async () => {
    setAuthError('')
    if (!validate()) return

    try {
      const res = await axios.post('/api/login', { email, password })
      localStorage.setItem('token', res.data.token)

      if (res.data.permission === 'admin') {
        router.push('/admin')
      } else {
        router.push('/home')
      }
    } catch (err) {
      setAuthError('メールアドレスまたはパスワードが正しくありません。')
    }
  }

  return (
    <div className={styles.container}>
      <div className={styles.scoreTableWrapper} style={{ maxWidth: 400, margin: '64px auto', padding: 32 }}>
        <div className={styles.titleBar}>
          <h1>多面・自己評価</h1>
        </div>
        <div className={styles.descriptionBox} style={{ marginTop: 16, marginBottom: 24 }}>
          <p>設定されたEmailとPasswordを入力してください</p>
        </div>

        <input
          className={styles.termDropdownButton}
          type="email"
          placeholder="Email"
          value={email}
          onChange={e => setEmail(e.target.value)}
          style={{ marginBottom: 8 }}
        />
        {emailError && <p className={styles.errorMsg}>{emailError}</p>}

        <input
          className={styles.termDropdownButton}
          type="password"
          placeholder="Password"
          value={password}
          onChange={e => setPassword(e.target.value)}
          style={{ marginBottom: 8 }}
        />
        {passwordError && <p className={styles.errorMsg}>{passwordError}</p>}

        <button
          className={styles.snippetButton}
          onClick={handleLogin}
          style={{
            width: '100%',
            marginTop: 8,
            marginBottom: 8,
            background: '#0070f3',
            color: 'white',
            fontWeight: 'bold',
            padding: 10,
            borderRadius: 4,
            border: 'none',
            cursor: 'pointer'
          }}
        >
          ログイン
        </button>

        {authError && <p className={styles.errorMsg}>{authError}</p>}

        <div style={{ marginTop: 12 }}>
          <a href="/reset_mail" className={styles.snippetButton} style={{ color: '#0070f3', textDecoration: 'underline' }}>
            パスワードを忘れた方はこちら
          </a>
        </div>
      </div>
    </div>
  )
}
