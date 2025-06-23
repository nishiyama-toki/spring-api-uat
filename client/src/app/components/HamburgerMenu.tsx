'use client'

import { useState, useEffect } from 'react'
import { usePathname } from 'next/navigation'
import { jwtDecode } from 'jwt-decode'  // ← 修正済み（named import）

interface JwtPayload {
  role?: string   // ← role を使って判定（例: "admin"）
}

export default function HamburgerMenu() {
  const [isAdmin, setIsAdmin] = useState(false)
  const [open, setOpen] = useState(false)
  const pathname = usePathname()

  useEffect(() => {
    const token = typeof window !== 'undefined' ? localStorage.getItem('token') : null

    if (token) {
      try {
        const decoded = jwtDecode<JwtPayload>(token)

        // ここで admin 判定（role を使う）
        if (decoded.role === 'admin') {
          setIsAdmin(true)
        } else {
          setIsAdmin(false)
        }
      } catch (error) {
        console.error('トークン解析に失敗しました:', error)
        setIsAdmin(false)
      }
    } else {
      setIsAdmin(false)
    }
  }, [pathname])

  const hiddenPaths = [
    '/login',
    '/reset_password',
    '/reset_password/success',
    '/reset_mail',
    '/reset_mail/sent'
  ]

  if (hiddenPaths.some(p => pathname === p)) {
    return null
  }

  const menuItems = [
    { name: 'ホーム', href: isAdmin ? '/admin' : '/home' },
    { name: '評価提出依頼', href: '/evaluation_requests' },
    { name: '過去評価履歴', href: '/evaluation' },
    { name: '等級基準書', href: '/grade-guidelines.pdf', target: '_blank' },
    { name: '人事評価', href: '/personnel-evaluation.pdf', target: '_blank' },
    { name: '社員等級', href: '/employee-grades.pdf', target: '_blank' },
  ]

  const adminItems = [
    { name: '評価期間設定', href: '/submission_period' },
    { name: 'ユーザー管理', href: '/user_management' },
    { name: '全社員評価確認', href: '/all-evaluations' },
    { name: '未提出者確認', href: '/unsubmitted-list' },
  ]

  return (
    <>
      <button
        onClick={() => setOpen(!open)}
        className="hamburger-btn"
        aria-label="Toggle Menu"
      >
        <svg width="28" height="28" fill="none" viewBox="0 0 24 24">
          <rect x="4" y="6" width="16" height="2" fill="currentColor" />
          <rect x="4" y="11" width="16" height="2" fill="currentColor" />
          <rect x="4" y="16" width="16" height="2" fill="currentColor" />
        </svg>
      </button>

      {open && (
        <div
          className="menu-overlay"
          onClick={() => setOpen(false)}
          aria-label="Close Menu Overlay"
        />
      )}

      <nav className={`menu-panel${open ? ' open' : ''}`}>
        <ul>
          {menuItems.map(item => (
            <li key={item.href}>
              <a
                href={item.href}
                target={item.target}
                rel={item.target ? 'noopener noreferrer' : undefined}
                onClick={() => setOpen(false)}
              >
                {item.name}
              </a>
            </li>
          ))}
          {isAdmin &&
            adminItems.map(item => (
              <li key={item.href} className="admin">
                <a
                  href={item.href}
                  onClick={() => setOpen(false)}
                >
                  {item.name}
                </a>
              </li>
            ))}
        </ul>
      </nav>
    </>
  )
}
