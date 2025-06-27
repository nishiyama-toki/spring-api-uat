'use client'

import { useState } from 'react'
import axios from '@/utils/axiosInstance'
import { useRouter } from 'next/navigation'
import styles from './resetMail.module.css' // ←login.module.cssでもOK

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
      await axios.post('/api/reset-mail', { email })
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
      <div className={styles.loginWrapper}>
        {/* タイトルバー（同じ構造） */}
        <div className={styles.titleBar}>
          <h1>パスワード再設定用メールの送信</h1>
        </div>
        <div className={styles.loginDescriptionBox}>
          <p>設定されたメールアドレスを入力してください</p>
        </div>
        <input
          className={styles.loginInput}
          type="email"
          placeholder="メールアドレス"
          value={email}
          onChange={e => setEmail(e.target.value)}
        />
        {emailError && <p className={styles.errorMsg}>{emailError}</p>}
        {notFoundError && <p className={styles.errorMsg}>{notFoundError}</p>}
        <button className={styles.loginButton} onClick={handleSubmit}>送信</button>
      </div>
    </div>
  )
}