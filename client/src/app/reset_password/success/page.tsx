"use client";

import Link from "next/link";
import styles from "./success.module.css";

export default function ResetSuccessPage() {
  return (
    <div className={styles.container}>
      <div className={styles.loginWrapper}>
        {/* :チェックマーク_緑:アイコン */}
        <p className={styles.icon}>:チェックマーク_緑:</p>
        {/* メッセージ */}
        <p className={styles.sentMessage}>パスワードの再設定が完了しました</p>
        {/* ログイン画面に戻るボタン */}
        <Link href="/login">
          <button className={styles.loginButton}>ログイン画面に戻る</button>
        </Link>
      </div>
    </div>
  );
}