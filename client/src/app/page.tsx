'use client'
import { useEffect } from 'react'
import { useRouter } from 'next/navigation'

export default function Home() {
  const router = useRouter()

  useEffect(() => {
    router.replace('/login') // 自動で /login に遷移
  }, [])

  return null // 画面表示なしでリダイレクト
}
