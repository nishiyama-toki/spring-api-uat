'use client'

import { useEffect, useState } from "react"
import axios from '@/utils/axiosInstance' //トークン自動付与のaxiosインスタンス
import { useSessionTimeout } from '@/hooks/useSessionTimeout';

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
// ------------------------
// 登録ユーザー一覧
// ------------------------
  const [users, setUsers ] = useState<User[]>([])

// ------------------------
// 新規登録フォーム用state(idは自動採番なので不要)
// ------------------------
  const [newUser, setNewUser] = useState<Omit<User, 'id'>>({
    name: '',
    email: '',
    password: '',
    isAdmin: false,
    role: 'スペシャリスト'
  })

  // ▼▼▼ 追加 ▼▼▼
  // ------------------------
  // 新規登録フォームのバリデーションエラー用state
  // ------------------------
  const [passwordError, setPasswordError] = useState('');
  // ▲▲▲ 追加 ▲▲▲


// ------------------------
// モーダル表示制御用 
// ------------------------
  const [isModalOpen, setIsModalOpen] = useState(false)
  const [userToDelete, setUserToDelete] = useState<User | null>(null)

// ------------------------
// ユーザー一覧取得（axiosInstanceによりトークン自動付与）
// ------------------------
useEffect(() => {
  axios.get<User[]>('/api/user_management_DB')
    .then(res => setUsers(res.data)) //表示のためのstate格納
    .catch(err => {
      console.error('取得失敗:' , err)
      if (err.response?.status === 401 || err.response?.status === 403) {
        alert('⛔ 認証エラー：再ログインしてください')
      }
    })
}, [])

// ▼▼▼ 追加 ▼▼▼
  // ------------------------
  // 新規ユーザー登録時のバリデーション関数
  // ------------------------
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
      setPasswordError(''); // エラーがない場合はクリア
    }

    // TODO: 必要であれば名前やEmailのバリデーションもここに追加
    
    return isValid;
  }
  // ▲▲▲ 追加 ▲▲▲

// ------------------------
// 新規ユーザー登録処理（axiosInstance使用）
// ------------------------
const handleRegister = async () => {
   // ▼▼▼ 変更 ▼▼▼：登録処理の前にバリデーションを実行
    if (!validateNewUser()) {
      return; // バリデーションが通らなければ処理を中断
    }
    // ▲▲▲ 変更 ▲▲▲

  try {
    const res = await axios.post<User>(
      '/api/user_management_register',
      {
        ...newUser,
        permission: newUser.isAdmin ? 'ADMIN' : 'USER' // ✅ 追加！
      }
    );
    setUsers(prev => [...prev, res.data]) // prev（前のstate）を使って一覧に追加
    setNewUser({                         // newUser を初期状態に戻す
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

// ------------------------
// 編集
// ------------------------
  const handleChange = (index: number, key: keyof User, value: any) => {
    const copy = [...users]                      //users をコピー（直接変更NGのため）
    copy[index] = {                              //編集対象のユーザーだけ変更
      ...copy[index],                            //既存のプロパティを展開
      [key]: value                               //指定されたキーだけ上書き
    }
    setUsers(copy)                               // state を更新 → 画面が再描画される
  }

// ------------------------
// 編集を保存(PUT)
// ------------------------
const handleSave = async (user: User) => {
  try {
    await axios.put('/api/user_management_edit',
      {
        id: user.id,
        name: user.name,
        email: user.email,
        isAdmin: user.isAdmin,  // ← 明示的に1個ずつ渡す
        role: user.role,
        permission: user.isAdmin ? 'ADMIN' : 'USER' // ✅ 追加！
      }
    );
    alert('変更を保存しました');
  } catch (e: any) {
    if (e.response?.status === 401 || e.response?.status === 403) {
      alert('⛔ 認証エラー：再ログインしてください');
    } else {
      alert('保存失敗');
    }
  }
};



// ------------------------
// 削除処理（モーダルから実行） 
// ------------------------
const handleDeleteConfirmed = async () => {
  if (!userToDelete) return
  try {
    await axios.delete('/api/user_management_delete', {
      data: { id: userToDelete.id }
    });
    setUsers(users.filter(u => u.id !== userToDelete.id))
    setIsModalOpen(false) //モーダルを閉じる
    setUserToDelete(null)
  } catch (e: any) {
    if (e.response?.status === 401 || e.response?.status === 403) {
      alert('⛔ 認証エラー：再ログインしてください')
    } else {
      alert('削除失敗')
    }
  }
}

// ------------------------
// HTML（略）
// ------------------------

// 以下はHTML部分が続くため、省略していますが処理には変更を加えていません。
// axiosの差し替えと不要なトークン取得・ヘッダー設定を削除したのが主な変更点です。



// ------------------------
// HTML 
// ------------------------  

  return(
   <div className="p-6">
      {/* モーダル部分  */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
          <div className="bg-white p-4 rounded shadow-md">
            <p>本当に削除しますか？</p>
            <div className="flex gap-4 mt-4">
              <button
                onClick={handleDeleteConfirmed}
                className="bg-red-500 text-white px-4 py-1 rounded"
              >削除する</button>
              <button
                onClick={() => setIsModalOpen(false)}
                className="px-4 py-1"
              >キャンセル</button>
            </div>
          </div>
        </div>
      )} 

      {/* 登録フォーム */}
      <h1 className="text-2xl font-bold mb-4">新規ユーザー登録</h1>
      <div className="flex flex-col gap-4 mb-8 items-start">
        <input
          type="text"
          placeholder="名前"
          value={newUser.name}
          onChange={e => setNewUser({ ...newUser, name: e.target.value })}
          className="border px-2 py-1"
        />
        <input
          type="email"
          placeholder="メール"
          value={newUser.email}
          onChange={e => setNewUser({ ...newUser, email: e.target.value })}
          className="border px-2 py-1"
        />
        <input
          type="password"
          placeholder="パスワード"
          value={newUser.password}
          onChange={e => setNewUser({ ...newUser, password: e.target.value })}
          className="border px-2 py-1"
        />
        <label className="flex items-center gap-1">
          <input
            type="checkbox"
            checked={Boolean(newUser.isAdmin)} //undefined対策を追加
            onChange={e => setNewUser({ ...newUser, isAdmin: e.target.checked })}
          />
          管理者
        </label>
        <select
          value={newUser.role}
          onChange={e => setNewUser({ ...newUser, role: e.target.value })}
          className="border px-2 py-1"
        >
          <option value="スペシャリスト">スペシャリスト</option>
          <option value="マネージャー">マネージャー</option>
          <option value="ゼネラリスト">ゼネラリスト</option>
        </select>
        <button onClick={handleRegister} className="bg-blue-500 text-white px-4 py-1 rounded">登録</button>
      </div>

      {/* ユーザー一覧テーブル */}
      <h2 className="text-xl font-semibold mb-2">ユーザー一覧</h2>
      <table className="w-full border text-left">
        <thead>
          <tr className="bg-gray-100">
            <th className="border px-2 py-1">名前</th>
            <th className="border px-2 py-1">メールアドレス</th>
            <th className="border px-2 py-1">管理者権限</th>
            <th className="border px-2 py-1">ロール</th>
            <th className="border px-2 py-1">操作</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user, idx) => (
            <tr key={user.id}>
              <td className="border px-2 py-1">
                <input
                  value={user.name ?? ''} //undefined対策
                  onChange={e => handleChange(idx, 'name', e.target.value)}
                  className="w-full"
                />
              </td>
              <td className="border px-2 py-1">
                <input
                  value={user.email ?? ''} //undefined対策
                  onChange={e => handleChange(idx, 'email', e.target.value)}
                  className="w-full"
                />
              </td>
              <td className="border px-2 py-1 text-center">
                <input
                  type="checkbox"
                  checked={Boolean(user.isAdmin)} //undefined対策追加
                  onChange={e => handleChange(idx, 'isAdmin', e.target.checked)}
                />
              </td>
              <td className="border px-2 py-1">
                <select
                  value={user.role ?? ''} // undefined対策
                  onChange={e => handleChange(idx, 'role', e.target.value)}
                  className="w-full"
                >
                  <option value="スペシャリスト">スペシャリスト</option>
                  <option value="マネージャー">マネージャー</option>
                  <option value="ゼネラリスト">ゼネラリスト</option>
                </select>
              </td>
              <td className="border px-2 py-1 flex gap-2">
                <button
                  onClick={() => handleSave(user)} //編集保存処理を呼び出すように修正
                  className="bg-blue-200 px-2 py-1 rounded"
                >変更を保存</button>
                <button
                  onClick={() => {
                    setUserToDelete(user)     //モーダル表示用に対象ユーザーをセット
                    setIsModalOpen(true)     //モーダルを表示
                  }}
                  className="bg-red-200 px-2 py-1 rounded"
                >ユーザーを削除</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
