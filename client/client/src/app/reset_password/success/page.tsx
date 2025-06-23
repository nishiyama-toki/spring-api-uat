"use client";

import Link from "next/link";
import styles from "./success.module.css";

export default function ResetSuccessPage() {
  return (
    <div className={styles.container}>
      <div className={styles.box}>
        <h1 className={styles.heading}>パスワードの再設定を完了しました</h1>
      </div>
      <div className={styles.buttonBox}>
        <Link href="/login">
          <button className={styles.button}>ログイン画面に戻る</button>
        </Link>
      </div>
    </div>
  );
}
