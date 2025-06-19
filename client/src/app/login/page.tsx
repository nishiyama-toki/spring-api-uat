'use client'

import { useState } from 'react'
import axios from 'utils/axiosInstance'
import { useRouter } from 'next/navigation'
import styles from './login.module.css'

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

    // メール必須チェック
    if (!email) {
      setEmailError('必須項目です。')
      isValid = false
    } else if (!emailRegex.test(email)) {
      setEmailError('メールアドレスの形式が正しくありません。')
      isValid = false
    } else {
      setEmailError('')
    }

    // パスワード必須チェック
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
      const res = await axios.post('http://localhost:8080/api/login', { email, password })
      localStorage.setItem('token', res.data.token)

      if (res.data.permission === 'admin') {
        router.push('/reset_mail')
      } else {
        router.push('/home')
      }
    } catch (err) {
      setAuthError('メールアドレスまたはパスワードが正しくありません。')
    }
  }

  return (
    <div className={styles.container}>
      <div className={styles.card}>
        <div className={styles.title}>多面・自己評価</div>
        <p>設定されたEmailとPasswordを入力してください</p>

        <input
          className={styles.input}
          type="email"
          placeholder="Email"
          value={email}
          onChange={e => setEmail(e.target.value)}
        />
        {emailError && <p className={styles.error}>{emailError}</p>}

        <input
          className={styles.input}
          type="password"
          placeholder="password"
          value={password}
          onChange={e => setPassword(e.target.value)}
        />
        {passwordError && <p className={styles.error}>{passwordError}</p>}

        <button className={styles.button} onClick={handleLogin}>login</button>

        {authError && <p className={styles.authError}>{authError}</p>}

        <p className={styles.link}><a href="/reset_mail">パスワードを忘れた方はこちら</a></p>
      </div>
    </div>
  )
}
