'use client'

import { useEffect, useState } from "react"
import axios from '@/utils/axiosInstance'
import { isAxiosError } from 'axios'
import { useSessionTimeout } from '@/hooks/useSessionTimeout'
import styles from './user_management.module.css'

// ------------------------
// ユーザ定義
// ------------------------
type User = {
  id: number
  name: string
  email: string
  password: string
  isAdmin: boolean
  role: string
}

export default function UserManagementPage() {
  useSessionTimeout(30);

  const [users, setUsers] = useState<User[]>([])
  const [newUser, setNewUser] = useState<Omit<User, 'id'>>({
    name: '',
    email: '',
    password: '',
    isAdmin: false,
    role: 'スペシャリスト'
  })
  const [passwordError, setPasswordError] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false)
  const [userToDelete, setUserToDelete] = useState<User | null>(null)

  useEffect(() => {
    axios.get<User[]>('/api/user_management_DB')
      .then(res => setUsers(res.data))
      .catch(err => {
        console.error('取得失敗:', err)
        if (isAxiosError(err)) {
          if (err.response?.status === 401 || err.response?.status === 403) {
            alert('⛔ 認証エラー：再ログインしてください')
          }
        }
      })
  }, [])

  const validateNewUser = () => {
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,20}$/;
    let isValid = true;

    if (!newUser.password) {
      setPasswordError('必須項目です。');
      isValid = false;
    } else if (!passwordRegex.test(newUser.password)) {
      setPasswordError('大文字・小文字・数字・記号を含んだ8～20文字にしてください。');
      isValid = false;
    } else {
      setPasswordError('');
    }

    return isValid;
  }

  const handleRegister = async () => {
    if (!validateNewUser()) return;
    try {
      const res = await axios.post<User>('/api/user_management_register', {
        ...newUser,
        permission: newUser.isAdmin ? 'ADMIN' : 'USER'
      });
      setUsers(prev => [...prev, res.data])
      setNewUser({
        name: '',
        email: '',
        password: '',
        isAdmin: false,
        role: 'スペシャリスト'
      }) 
    } catch (e: unknown) {
      if (isAxiosError(e)) {
        if (e.response?.status === 401 || e.response?.status === 403) {
          alert('⛔ 認証エラー：再ログインしてください')
        } else {
          alert('登録失敗')
        }
      } else {
        alert('予期しないエラーが発生しました')
      }
    }
  }

  const handleChange = (index: number, key: keyof User, value: any) => {
    const copy = [...users]
    copy[index] = {
      ...copy[index],
      [key]: value
    }
    setUsers(copy)
  }

  const handleSave = async (user: User) => {
    try {
      await axios.put('/api/user_management_edit', {
        id: user.id,
        name: user.name,
        email: user.email,
        isAdmin: user.isAdmin,
        role: user.role,
        permission: user.isAdmin ? 'ADMIN' : 'USER'
      });
      alert('変更を保存しました');
    } catch (e: unknown) {
      if (isAxiosError(e)) {
        if (e.response?.status === 401 || e.response?.status === 403) {
          alert('⛔ 認証エラー：再ログインしてください');
        } else {
          alert('保存失敗');
        }
      } else {
        alert('予期しないエラーが発生しました');
      }
    }
  }

  const handleDeleteConfirmed = async () => {
    if (!userToDelete) return
    try {
      await axios.delete('/api/user_management_delete', {
        data: { id: userToDelete.id }
      });
      setUsers(users.filter(u => u.id !== userToDelete.id))
      setIsModalOpen(false)
      setUserToDelete(null)
    } catch (e: unknown) {
      if (isAxiosError(e)) {
        if (e.response?.status === 401 || e.response?.status === 403) {
          alert('⛔ 認証エラー：再ログインしてください')
        } else {
          alert('削除失敗')
        }
      } else {
        alert('予期しないエラーが発生しました')
      }
    }
  }

  return (
    <div className={styles.container}>
      {/* モーダル */}
      {isModalOpen && (
        <div className={styles.modalOverlay}>
          <div className={styles.modalContent}>
            <p>本当に削除しますか？</p>
            <div className={styles.modalFooter}>
              <button onClick={handleDeleteConfirmed} className={styles.buttonActive}>削除する</button>
              <button onClick={() => setIsModalOpen(false)} className={styles.closeButton}>キャンセル</button>
            </div>
          </div>
        </div>
      )}
      {/* ※ ユーザー登録フォームや一覧の表示部分は省略中。必要ならこの下に再挿入可能 */}
    </div>
  )
}