'use client'

import { useEffect, useState } from "react"
import axios from '@/utils/axiosInstance'
import { useSessionTimeout } from '@/hooks/useSessionTimeout';
import styles from './user_management.module.css'

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
        if (err.response?.status === 401 || err.response?.status === 403) {
          alert('⛔ 認証エラー：再ログインしてください')
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
      const res = await axios.post<User>(
        '/api/user_management_register',
        {
          ...newUser,
          permission: newUser.isAdmin ? 'ADMIN' : 'USER'
        }
      );
      setUsers(prev => [...prev, res.data])
      setNewUser({
        name: '',
        email: '',
        password: '',
        isAdmin: false,
        role: 'スペシャリスト'
      })
    } catch (e: any) {
      if (e.response?.status === 401 || e.response?.status === 403) {
        alert('⛔ 認証エラー：再ログインしてください')
      } else {
        alert('登録失敗')
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
    } catch (e: any) {
      if (e.response?.status === 401 || e.response?.status === 403) {
        alert('⛔ 認証エラー：再ログインしてください');
      } else {
        alert('保存失敗');
      }
    }
  };

  const handleDeleteConfirmed = async () => {
    if (!userToDelete) return
    try {
      await axios.delete('/api/user_management_delete', {
        data: { id: userToDelete.id }
      });
      setUsers(users.filter(u => u.id !== userToDelete.id))
      setIsModalOpen(false)
      setUserToDelete(null)
    } catch (e: any) {
      if (e.response?.status === 401 || e.response?.status === 403) {
        alert('⛔ 認証エラー：再ログインしてください')
      } else {
        alert('削除失敗')
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
              <button
                onClick={handleDeleteConfirmed}
                className={styles.buttonActive}
              >削除する</button>
              <button
                onClick={() => setIsModalOpen(false)}
                className={styles.closeButton}
              >キャンセル</button>
            </div>
          </div>
        </div>
      )}

      <div className={styles.titleBar}>
        <h1>ユーザー管理</h1>
      </div>
      <div className={styles.descriptionBox}>
        <p>管理者画面からユーザーを追加・編集・削除できます。</p>
      </div>
      <div className={styles.mainContent}>
        {/* 左：新規ユーザー登録 */}
        <section className={styles.formPane}>
          <h2 className={styles.sectionTitle}>新規ユーザー登録</h2>
          <form className={styles.formVertical} onSubmit={e => {e.preventDefault(); handleRegister();}}>
            <label className={styles.inputLabel}>
              名前
              <input
                type="text"
                value={newUser.name}
                onChange={e => setNewUser({ ...newUser, name: e.target.value })}
                className={styles.inputNum}
                required
              />
            </label>
            <label className={styles.inputLabel}>
              メール
              <input
                type="email"
                value={newUser.email}
                onChange={e => setNewUser({ ...newUser, email: e.target.value })}
                className={styles.inputNum}
                required
              />
            </label>
            <label className={styles.inputLabel}>
              パスワード
              <input
                type="password"
                value={newUser.password}
                onChange={e => setNewUser({ ...newUser, password: e.target.value })}
                className={styles.inputNum}
                required
              />
              {passwordError && <span className={styles.errorMsg}>{passwordError}</span>}
            </label>
            <label className={styles.inputLabel}>
              管理者
              <input
                type="checkbox"
                checked={Boolean(newUser.isAdmin)}
                onChange={e => setNewUser({ ...newUser, isAdmin: e.target.checked })}
                style={{ width: "20px", height: "20px" }}
              />
            </label>
            <label className={styles.inputLabel}>
              ロール
              <select
                value={newUser.role}
                onChange={e => setNewUser({ ...newUser, role: e.target.value })}
                className={styles.selectInput}
              >
                <option value="スペシャリスト">スペシャリスト</option>
                <option value="マネージャー">マネージャー</option>
                <option value="ゼネラリスト">ゼネラリスト</option>
              </select>
            </label>
            <button type="submit" className={styles.buttonActive}>登録</button>
          </form>
        </section>
        {/* 右：ユーザー一覧 */}
        <section className={styles.tablePane}>
          <h2 className={styles.sectionTitle}>ユーザー一覧</h2>
          <table className={styles.table}>
            <thead>
              <tr>
                <th>名前</th>
                <th>メールアドレス</th>
                <th>管理者権限</th>
                <th>ロール</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              {users.map((user, idx) => (
                <tr key={user.id}>
                  <td>
                    <input
                      value={user.name ?? ''}
                      onChange={e => handleChange(idx, 'name', e.target.value)}
                      className={styles.inputNum}
                    />
                  </td>
                  <td>
                    <input
                      value={user.email ?? ''}
                      onChange={e => handleChange(idx, 'email', e.target.value)}
                      className={styles.inputNum}
                    />
                  </td>
                  <td>
                    <input
                      type="checkbox"
                      checked={Boolean(user.isAdmin)}
                      onChange={e => handleChange(idx, 'isAdmin', e.target.checked)}
                      style={{ width: "20px", height: "20px" }}
                    />
                  </td>
                  <td>
                    <select
                      value={user.role ?? ''}
                      onChange={e => handleChange(idx, 'role', e.target.value)}
                      className={styles.selectInput}
                    >
                      <option value="スペシャリスト">スペシャリスト</option>
                      <option value="マネージャー">マネージャー</option>
                      <option value="ゼネラリスト">ゼネラリスト</option>
                    </select>
                  </td>
                  <td>
                    <button
                      onClick={() => handleSave(user)}
                      className={styles.editButton}
                    >変更を保存</button>
                    <button
                      onClick={() => {
                        setUserToDelete(user)
                        setIsModalOpen(true)
                      }}
                      className={styles.editButton}
                      style={{ backgroundColor: '#fecaca', color: '#222', borderColor: '#faa' }}
                    >ユーザーを削除</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </section>
      </div>
    </div>
  )
}