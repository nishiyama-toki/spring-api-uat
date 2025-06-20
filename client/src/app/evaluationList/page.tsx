import Link from 'next/link';
import styles from './EvaluationList.module.css';

export default function EvaluationListPage() {
  // テスト用の期（必要に応じて変更してください）
  const phase = 18;
  const quarters = [1, 2, 3, 4];

  return (
    <div className={styles.container}>
      <h1 className={styles.title}>評価一覧（リンクテスト用）</h1>
      <ul className={styles.list}>
        {quarters.map((q) => (
          <li key={q} className={styles.item}>
            <Link
              href={`/selfEvaluation?phase=${phase}&quarter=${q}`}
              className={styles.link}
            >
              {phase}期 {q}Q の自己評価画面へ
            </Link>
          </li>
        ))}
      </ul>
    </div>
  );
}