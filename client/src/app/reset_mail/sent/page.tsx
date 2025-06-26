"use client";

import Link from "next/link";
import styles from "./sent.module.css";

export default function ResetSentPage() {
  return (
    <div className={styles.container}>
      <div className={styles.box}>
        <h1 className={styles.heading}>メールを送信しました</h1>
      </div>
      <div className={styles.buttonBox}>
        <Link href="/login">
          <button className={styles.button}>ログイン画面に戻る</button>
        </Link>
      </div>
    </div>
  );
}
