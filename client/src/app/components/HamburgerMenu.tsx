'use client';

import { useState, useEffect } from 'react'
import { usePathname } from 'next/navigation'
import { jwtDecode } from 'jwt-decode'  // ← 修正済み（named import）
import styles from './HamburgerMenu.module.css'


interface JwtPayload {
  permission?: string  // ← permission クレームを使って判定（"admin" or "user"）
}

export default function HamburgerMenu() {
  const [isAdmin, setIsAdmin] = useState(false)
  const pathname = usePathname()

  useEffect(() => {
    const isAdminStr = typeof window !== "undefined" ? localStorage.getItem('is_admin') : null
    setIsAdmin(isAdminStr === 'true')
  }, [pathname])

  const [open, setOpen] = useState(false)

  // 除外したいパス
  const hiddenPaths = [
    '/login',
    '/reset_password',
    '/reset_password/success',
    '/reset_mail',
    '/reset_mail/sent',
  ];

  if (hiddenPaths.includes(pathname)) {
    return null;
  }

  // 「ホーム」だけ isAdmin でリンク先を切り替え
  const menuItems = [
    { name: 'ホーム', href: isAdmin ? '/admin' : '/home' },
    { name: '評価提出依頼', href: '/evaluation_requests' },
    { name: '過去評価履歴', href: '/evaluation' },
    { name: '等級基準書', href: '/grade-guidelines.pdf', target: '_blank' },
    { name: '人事評価', href: '/personnel-evaluation.pdf', target: '_blank' },
    { name: '社員等級', href: '/employee-grades.pdf', target: '_blank' },
  ];

  const adminItems = [
    { name: '評価期間設定', href: '/submission_period' },
    { name: 'ユーザー管理', href: '/user-management' },
    { name: '全社員評価確認', href: '/all-evaluations' },
    { name: '未提出者確認', href: '/pending-submissions' },
  ]

  return (
    <>
      <button
        onClick={() => setOpen(!open)}
        className={styles.hamburgerBtn}
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
          className={styles.menuOverlay}
          onClick={() => setOpen(false)}
          aria-label="Close Menu Overlay"
        />
      )}

      <nav className={`menu-panel${open ? " open" : ""}`}>
        <ul>
          {menuItems.map((item) => (
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
            adminItems.map((item) => (
              <li key={item.href} className={styles.admin}>
                <a href={item.href} onClick={() => setOpen(false)}>
                  {item.name}
                </a>
              </li>
            ))}
        </ul>
      </nav>
    </>
  );
}
