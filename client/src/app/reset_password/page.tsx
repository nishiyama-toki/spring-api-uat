<<<<<<< HEAD
'use client'
import { useState } from 'react'
import axios from '@/utils/axiosInstance'
import { useRouter, useSearchParams } from 'next/navigation'
import styles from './resetPassword.module.css'
=======
// 'use client'
// import { useState } from 'react'
// import axios from '@/utils/axiosInstance'
// import { useRouter, useSearchParams } from 'next/navigation'
>>>>>>> upstream/gen

// export const dynamic = 'force-dynamic'

// export default function ResetPasswordPage() {
//   const router = useRouter()
//   const searchParams = useSearchParams()
//   const token = searchParams.get('token') // メールから渡されるトークン

//   const [password, setPassword] = useState('')
//   const [confirm, setConfirm] = useState('')
//   const [error, setError] = useState('')

//   const handleReset = async () => {
//     if (password !== confirm) {
//       setError('パスワードが一致しません')
//       return
//     }

//     try {
//       await axios.post('/api/reset-password', {
//         token,
//         newPassword: password
//       }, {
//       headers: {
//         'Content-Type': 'application/json'
//       }
//       })
//       router.push('/reset_password/success')
//     } catch {
//       setError('再設定に失敗しました')
//     }
//   }

//   return (
//     <div>
//       <h1>パスワード再設定</h1>
//       <input type="password" placeholder="新しいパスワード" value={password} onChange={e => setPassword(e.target.value)} />
//       <input type="password" placeholder="再入力" value={confirm} onChange={e => setConfirm(e.target.value)} />
//       {error && <p style={{ color: 'red' }}>{error}</p>}
//       <button onClick={handleReset}>設定</button>
//     </div>
//   )
// }

'use client'

import { Suspense, useState } from 'react'
import { useSearchParams, useRouter } from 'next/navigation'
import axios from '@/utils/axiosInstance'

function Inner() {
  const searchParams = useSearchParams()
<<<<<<< HEAD
=======
  const router = useRouter()
>>>>>>> upstream/gen
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
<<<<<<< HEAD
        newPassword: password
      }, {
        headers: {
        'Content-Type': 'application/json'
        }
=======
        newPassword: password,
>>>>>>> upstream/gen
      })
      router.push('/reset_password/success')
    } catch {
      setError('再設定に失敗しました')
    }
  }

  return (
<<<<<<< HEAD
    <div className={styles.container}>
      <h1 className={styles.title}>パスワード再設定</h1>
=======
    <div>
      <h1>パスワード再設定</h1>
>>>>>>> upstream/gen
      <input
        type="password"
        placeholder="新しいパスワード"
        value={password}
        onChange={e => setPassword(e.target.value)}
<<<<<<< HEAD
        className={styles.input}
        autoComplete="new-password"
=======
>>>>>>> upstream/gen
      />
      <input
        type="password"
        placeholder="再入力"
        value={confirm}
        onChange={e => setConfirm(e.target.value)}
<<<<<<< HEAD
        className={styles.input}
        autoComplete="new-password"
      />
      {error && <p className={styles.error}>{error}</p>}
      <button onClick={handleReset} className={styles.button}>設定</button>
    </div>
  )
}
=======
      />
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <button onClick={handleReset}>設定</button>
    </div>
  )
}

export default function Page() {
  return (
    <Suspense fallback={<div>読み込み中...</div>}>
      <Inner />
    </Suspense>
  )
}
>>>>>>> upstream/gen
