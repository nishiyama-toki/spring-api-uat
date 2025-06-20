'use client';

import React, { useState } from 'react';

// ユーザー型
interface User {
  id: number;
  name: string;
  email: string;
  password?: string;
  isAdmin?: boolean;
  role: string;
}

export default function UserManagement() {
  // 仮の初期データ
  const [users, setUsers] = useState<User[]>([
    { id: 1, name: '山田 太郎', email: 'taro@example.com', isAdmin: true, role: 'マネージャー' },
    { id: 2, name: '佐藤 花子', email: 'hanako@example.com', isAdmin: false, role: 'スペシャリスト' },
    { id: 3, name: 'John Smith', email: 'john@example.com', isAdmin: false, role: 'ゼネラリスト' },
  ]);
  const [newUser, setNewUser] = useState<User>({
    id: 0,
    name: '',
    email: '',
    password: '',
    isAdmin: false,
    role: 'スペシャリスト'
  });
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [userToDelete, setUserToDelete] = useState<User | null>(null);

  // 新規登録
  const handleRegister = () => {
    if (!newUser.name || !newUser.email || !newUser.password) return;
    const nextId = Math.max(...users.map(u => u.id), 0) + 1;
    setUsers([...users, { ...newUser, id: nextId }]);
    setNewUser({ id: 0, name: '', email: '', password: '', isAdmin: false, role: 'スペシャリスト' });
  };

  // 編集保存
  const handleSave = (user: User) => {
    setUsers(users.map(u => (u.id === user.id ? user : u)));
  };

  // テーブルの編集操作
  const handleChange = (idx: number, field: keyof User, value: any) => {
    setUsers(users =>
      users.map((u, i) => (i === idx ? { ...u, [field]: value } : u))
    );
  };

  // 削除確定
  const handleDeleteConfirmed = () => {
    if (userToDelete) {
      setUsers(users.filter(u => u.id !== userToDelete.id));
      setUserToDelete(null);
      setIsModalOpen(false);
    }
  };

  return (
    <div className="pe-container">
      {/* 削除モーダル */}
      {isModalOpen && (
        <div className="pe-modalOverlay" style={{ zIndex: 1050 }}>
          <div className="pe-modalContent" style={{ maxWidth: 340 }}>
            <p style={{ fontWeight: 600, fontSize: 16 }}>本当に削除しますか？</p>
            <div style={{ display: 'flex', gap: 20, marginTop: 18 }}>
              <button
                onClick={handleDeleteConfirmed}
                className="pe-closeButton"
                style={{ background: '#e3342f', color: '#fff', minWidth: 80 }}
              >削除する</button>
              <button
                onClick={() => setIsModalOpen(false)}
                className="pe-closeButton"
                style={{ background: '#ccc', color: '#333', minWidth: 80 }}
              >キャンセル</button>
            </div>
          </div>
        </div>
      )}

      {/* 登録フォーム */}
      <div className="pe-titleBar" style={{ marginBottom: 32 }}>
        <h1>新規ユーザー登録</h1>
      </div>
      <div style={{
        display: 'flex', flexDirection: 'row', gap: 24, marginBottom: 32,
        alignItems: 'flex-start'
      }}>
        <div style={{ minWidth: 320, flex: 1 }}>
          <div className="pe-inputGroup" style={{ marginBottom: 10 }}>
            <input
              type="text"
              placeholder="名前"
              value={newUser.name}
              onChange={e => setNewUser({ ...newUser, name: e.target.value })}
              className="pe-input"
              style={{ width: '100%', padding: 8, borderRadius: 4, border: '1px solid #ccc' }}
            />
          </div>
          <div className="pe-inputGroup" style={{ marginBottom: 10 }}>
            <input
              type="email"
              placeholder="メール"
              value={newUser.email}
              onChange={e => setNewUser({ ...newUser, email: e.target.value })}
              className="pe-input"
              style={{ width: '100%', padding: 8, borderRadius: 4, border: '1px solid #ccc' }}
            />
          </div>
          <div className="pe-inputGroup" style={{ marginBottom: 10 }}>
            <input
              type="password"
              placeholder="パスワード"
              value={newUser.password}
              onChange={e => setNewUser({ ...newUser, password: e.target.value })}
              className="pe-input"
              style={{ width: '100%', padding: 8, borderRadius: 4, border: '1px solid #ccc' }}
            />
          </div>
          <label className="pe-inputGroup" style={{ marginBottom: 10, display: 'flex', alignItems: 'center', gap: 6 }}>
            <input
              type="checkbox"
              checked={Boolean(newUser.isAdmin)}
              onChange={e => setNewUser({ ...newUser, isAdmin: e.target.checked })}
            />
            管理者
          </label>
          <div className="pe-inputGroup" style={{ marginBottom: 12 }}>
            <select
              value={newUser.role}
              onChange={e => setNewUser({ ...newUser, role: e.target.value })}
              className="pe-input"
              style={{ width: '100%', padding: 8, borderRadius: 4, border: '1px solid #ccc' }}
            >
              <option value="スペシャリスト">スペシャリスト</option>
              <option value="マネージャー">マネージャー</option>
              <option value="ゼネラリスト">ゼネラリスト</option>
            </select>
          </div>
          <button
            onClick={handleRegister}
            className="pe-closeButton"
            style={{
              background: '#0070f3', color: '#fff', fontWeight: 700, width: '100%', fontSize: 16
            }}
          >
            登録
          </button>
        </div>
      </div>

      {/* ユーザー一覧テーブル */}
      <div style={{ marginBottom: 8 }}>
        <h2 style={{ fontSize: 20, fontWeight: 700, marginBottom: 10 }}>ユーザー一覧</h2>
        <div className="pe-scoreTableWrapper">
          <table className="pe-table" style={{ minWidth: 680 }}>
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
                      className="pe-input"
                      style={{ width: '100%' }}
                    />
                  </td>
                  <td>
                    <input
                      value={user.email ?? ''}
                      onChange={e => handleChange(idx, 'email', e.target.value)}
                      className="pe-input"
                      style={{ width: '100%' }}
                    />
                  </td>
                  <td style={{ textAlign: 'center' }}>
                    <input
                      type="checkbox"
                      checked={Boolean(user.isAdmin)}
                      onChange={e => handleChange(idx, 'isAdmin', e.target.checked)}
                    />
                  </td>
                  <td>
                    <select
                      value={user.role ?? ''}
                      onChange={e => handleChange(idx, 'role', e.target.value)}
                      className="pe-input"
                      style={{ width: '100%' }}
                    >
                      <option value="スペシャリスト">スペシャリスト</option>
                      <option value="マネージャー">マネージャー</option>
                      <option value="ゼネラリスト">ゼネラリスト</option>
                    </select>
                  </td>
                  <td style={{ textAlign: 'center' }}>
                    <button
                      onClick={() => handleSave(user)}
                      className="pe-snippetButton"
                      style={{ background: '#e6f4ff', marginRight: 6, fontWeight: 600 }}
                    >
                      変更を保存
                    </button>
                    <button
                      onClick={() => {
                        setUserToDelete(user);
                        setIsModalOpen(true);
                      }}
                      className="pe-snippetButton"
                      style={{ background: '#fed7d7', color: '#e3342f', fontWeight: 600 }}
                    >
                      ユーザーを削除
                    </button>
                  </td>
                </tr>
              ))}
              {users.length === 0 && (
                <tr>
                  <td colSpan={5} className="pe-noDataMsg">ユーザーがいません</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
