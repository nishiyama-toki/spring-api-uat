'use client'

import { useState } from 'react'
import axios from 'utils/axiosInstance'
import { useRouter } from 'next/navigation'
import styles from './resetMail.module.css'

export default function ResetMailPage() {
  const router = useRouter()
  const [email, setEmail] = useState('')
  const [emailError, setEmailError] = useState('')
  const [notFoundError, setNotFoundError] = useState('')

  const validate = () => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (!email) {
      setEmailError('必須項目です。')
      return false
    } else if (!emailRegex.test(email)) {
      setEmailError('メールアドレスの形式が正しくありません。')
      return false
    }
    setEmailError('')
    return true
  }

  const handleSubmit = async () => {
    setNotFoundError('')
    if (!validate()) return

    try {
      await axios.post('http://localhost:8080/api/reset-mail', { email })
      router.push('/reset_mail/sent')
    } catch (err: any) {
      if (err.response?.status === 404) {
        setNotFoundError('未登録のメールアドレスです')
      } else {
        setNotFoundError('エラーが発生しました')
      }
    }
  }

  return (
    <div className={styles.container}>
      <div className={styles.card}>
        <div className={styles.title}>リセットメールの送信</div>
        <p>登録したメールアドレスを入力してください</p>

        <input
          className={styles.input}
          type="email"
          placeholder="メールアドレス"
          value={email}
          onChange={e => setEmail(e.target.value)}
        />
        {emailError && <p className={styles.error}>{emailError}</p>}
        {notFoundError && <p className={styles.error}>{notFoundError}</p>}

        <button className={styles.button} onClick={handleSubmit}>送信</button>
      </div>
    </div>
  )
}

