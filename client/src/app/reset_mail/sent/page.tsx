"use client";

import Link from "next/link";
import styles from "../sent.module.css"; // login.module.css でもOK

export default function ResetMailSentPage() {
  return (
  <div className={styles.container}>
    <div className={styles.loginWrapper}>
      <p className={styles.icon}>✅</p>
      <p className={styles.sentMessage}>
        メールを送信しました
      </p>
      <Link href="/login">
        <button className={styles.loginButton}>ログイン画面に戻る</button>
      </Link>
    </div>
  </div>
  );
}