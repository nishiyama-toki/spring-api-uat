'use client'

import { useState } from 'react'
import axios from '@/utils/axiosInstance'
import { useRouter } from 'next/navigation'
import styles from './login.module.css'

export default function LoginPage() {
  const router = useRouter()

  // ----------------- 状態 -----------------
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [emailError, setEmailError] = useState('')
  const [passwordError, setPasswordError] = useState('')
  const [authError, setAuthError] = useState('')

  // ----------------- バリデーション -----------------
  const validate = () => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    const passwordRegex =
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,20}$/
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
      setPasswordError(
        '大文字・小文字・数字・記号を含んだ8～20文字にしてください。'
      )
      isValid = false
    } else {
      setPasswordError('')
    }
    return isValid
  }

  // ----------------- 送信 -----------------
  const handleLogin = async () => {
    setAuthError('')
    if (!validate()) return
    try {
      const res = await axios.post('/api/login', { email, password })
      localStorage.setItem('token', res.data.token)
      router.push(res.data.permission === 'admin' ? '/admin' : '/home')
    } catch {
      setAuthError('メールアドレスまたはパスワードが正しくありません。')
    }
  }
  
  // ----------------- JSX -----------------
  return (
    <div className={styles.container}>
      <div className={styles.loginWrapper}>
        {/* タイトル */}
        <div className={styles.titleBar}>
          <h1>自己・多面評価アプリ</h1>
        </div>
        {/* 説明文 */}
        <div className={styles.loginDescriptionBox}>
          <p>設定されたメールアドレスとパスワードを入力してください</p>
        </div>
        {/* Email */}
        <input
          className={styles.loginInput}
          type="email"
          placeholder="メールアドレス"
          value={email}
          onChange={e => setEmail(e.target.value)}
        />
        {emailError && <p className={styles.errorMsg}>{emailError}</p>}
        {/* Password */}
        <input
          className={styles.loginInput}
          type="password"
          placeholder="パスワード"
          value={password}
          onChange={e => setPassword(e.target.value)}
        />
        {passwordError && <p className={styles.errorMsg}>{passwordError}</p>}
        {/* ログインボタン */}
        <button className={styles.loginButton} onClick={handleLogin}>
          ログイン
        </button>
        {authError && <p className={styles.errorMsg}>{authError}</p>}
        {/* パスワード忘れリンク */}
        <div className={styles.loginForgotSection}>
          <a href="/reset_mail" className={styles.loginForgotLink}>
            パスワードを忘れた方はこちら
          </a>
        </div>
      </div>
    </div>
  )
}