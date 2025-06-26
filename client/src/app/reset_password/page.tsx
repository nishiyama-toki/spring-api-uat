'use client'
import { useState } from 'react'
import axios from '@/utils/axiosInstance'
import { useRouter, useSearchParams } from 'next/navigation'
import styles from './resetPassword.module.css'

export default function ResetPasswordPage() {
  const router = useRouter()
  const searchParams = useSearchParams()
  const token = searchParams.get('token')

  const [password, setPassword] = useState('')
  const [confirm, setConfirm] = useState('')
  const [error, setError] = useState('')

  const handleReset = async () => {
    // バリデーション：パスワード形式チェック
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,20}$/

    if (!passwordRegex.test(password)) {
      setError('大文字・小文字・数字・記号を含んだ8～20文字にしてください。')
      return
    }
    
    if (password !== confirm) {
      setError('パスワードが一致しません')
      return
    }

    try {
      await axios.post('/api/reset-password', {
        token,
        newPassword: password
      }, {
        headers: {
        'Content-Type': 'application/json'
        }
      })
      router.push('/reset_password/success')
    } catch {
      setError('再設定に失敗しました')
    }
  }

  return (
    <div className={styles.container}>
      <h1 className={styles.title}>パスワード再設定</h1>
      <input
        type="password"
        placeholder="新しいパスワード"
        value={password}
        onChange={e => setPassword(e.target.value)}
        className={styles.input}
        autoComplete="new-password"
      />
      <input
        type="password"
        placeholder="再入力"
        value={confirm}
        onChange={e => setConfirm(e.target.value)}
        className={styles.input}
        autoComplete="new-password"
      />
      {error && <p className={styles.error}>{error}</p>}
      <button onClick={handleReset} className={styles.button}>設定</button>
    </div>
  )
}
