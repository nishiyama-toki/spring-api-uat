'use client'

import React from 'react'
import { useRouter } from 'next/navigation'
import HamburgerMenu from '../components/HamburgerMenu'

export default function SubmittedPage() {
  const router = useRouter()

  const handleBack = () => {
    router.push('/evaluation-requests')
  }

  return (
    <div className="pe-container">
      {/* ハンバーガーメニュー（全画面共通） */}
      <HamburgerMenu />

      {/* タイトルバー */}
      <div className="pe-titleBar">
        <h1>評価提出</h1>
      </div>

      {/* メインコンテンツ */}
      <div className="pe-mainContent" style={{ justifyContent: 'center' }}>
        <div className="pe-content" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', marginTop: 60 }}>
          {/* チェックマーク */}
          <div
            style={{
              fontSize: '4rem',
              marginBottom: '16px',
              userSelect: 'none',
            }}
            className="submitted-icon"
          >✅</div>

          {/* メッセージ */}
          <h2 className="submitted-message" style={{ marginBottom: '20px', color: '#333' }}>送信完了しました</h2>

          {/* ボタン */}
          <button
            onClick={handleBack}
            className="submitted-button"
            style={{
              backgroundColor: '#0070f3',
              color: '#fff',
              border: 'none',
              borderRadius: 4,
              padding: '10px 32px',
              fontSize: 18,
              fontWeight: 600,
              cursor: 'pointer',
              boxShadow: '0 2px 8px rgba(0,0,0,0.06)',
              marginTop: 10,
            }}
          >
            評価依頼一覧へ
          </button>
        </div>
      </div>
    </div>
  )
}
