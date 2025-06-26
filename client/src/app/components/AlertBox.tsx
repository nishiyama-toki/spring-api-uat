'use client';

import styles from './AlertBox.module.css'; // ← 追加

interface Alert {
  message: string;
  date: string;
  type: string; // warning/info/error
}

interface NotSubmittedUser {
  name: string;
  email: string;
  alert_message: string;
}

interface AlertBoxProps {
  alerts: Alert[];
  notSubmitted?: NotSubmittedUser[];
  isAdmin?: boolean;
}

export default function AlertBox({ alerts, notSubmitted, isAdmin }: AlertBoxProps) {
  return (
    <div className={styles.alertWrapper}>
      <h3 className={styles.alertTitle}>アラート</h3>
      <ul>
        {alerts.map((alert, i) => (
          <li key={i} className={styles.alertItem}>
            {alert.message}（{alert.date}）
            <span className={styles.alertType}>[{alert.type}]</span>
          </li>
        ))}
      </ul>

      {isAdmin && notSubmitted && notSubmitted.length > 0 && (
        <div className={styles.notSubmittedBox}>
          <h4 className={styles.notSubmittedTitle}>未入力者一覧（{notSubmitted.length}名）</h4>
          <ul className={styles.list}>
            {notSubmitted.map((u, i) => (
              <li key={i} className={styles.notSubmittedItem}>
                {u.name}（{u.email}） - {u.alert_message}
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}
