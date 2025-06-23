'use client'
import { useEffect } from 'react'
import { useRouter } from 'next/navigation'

export default function Home() {
  const router = useRouter()

  useEffect(() => {
    router.replace('/login') // 自動で /login に遷移
  }, [router]) // ← router を依存配列に追加

  return null // 画面表示なしでリダイレクト
}
