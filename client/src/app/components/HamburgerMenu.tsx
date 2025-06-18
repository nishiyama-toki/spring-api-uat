'use client'

import { useState, useEffect } from 'react'
import { usePathname } from 'next/navigation'

export default function HamburgerMenu() {
  const [isAdmin, setIsAdmin] = useState(false)
  const pathname = usePathname()

  useEffect(() => {
    const permission = typeof window !== "undefined" ? localStorage.getItem('permission') : null
    setIsAdmin(permission === 'admin')
  }, [pathname])

  const [open, setOpen] = useState(false)

  const menuItems = [
    { name: 'ホーム', href: '/home' },
    { name: '評価提出依頼', href: '/selfEvaluation' },
    { name: '過去評価履歴', href: '/past-evaluations' },
    { name: '等級基準書', href: '/grade-guidelines.pdf', target: '_blank' },
    { name: '人事評価', href: '/personnel-evaluation.pdf', target: '_blank' },
    { name: '社員等級', href: '/employee-grades.pdf', target: '_blank' },
  ]

  const adminItems = [
    { name: '評価期間設定', href: '/submission_period' }, ///evaluation-period-setting を変更
    { name: 'ユーザー管理', href: '/user-management' },
    { name: '全社員評価確認', href: '/all-evaluations' },
    { name: '未提出者確認', href: '/pending-submissions' },
    
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

      <nav className={`menu-panel${open ? " open" : ""}`}>
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
