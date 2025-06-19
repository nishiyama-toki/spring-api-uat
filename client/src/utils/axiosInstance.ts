// utils/axiosInstance.ts
import axios from 'axios'

const instance = axios.create({
  baseURL: 'http://localhost:8080',
})

// リクエストごとにトークンをセット（localStorageから）
instance.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
   // /api/login のときは Authorization ヘッダーを付けない
  if (token && config.url !== '/api/login') {
    config.headers.Authorization = `Bearer ${token}` // Bearer を付ける
  }
  return config
})

// レスポンスで新トークンがあれば保存（TokenRefreshFilter対応）
instance.interceptors.response.use(response => {
  const newToken = response.headers['authorization']
  if (newToken) {
    localStorage.setItem('token', newToken)
  }
  return response
})

export default instance
