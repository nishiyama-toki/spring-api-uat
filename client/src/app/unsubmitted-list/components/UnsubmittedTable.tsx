import styles from './UnsubmittedTable.module.css';

type UnsubmittedUser = {
    name: string;
};

type Props = {
    unsubmittedList: UnsubmittedUser[];
};

export const UnsubmittedTable = ({ unsubmittedList }: Props) => {
    // 未提出者がいなければメッセージを表示
    if (unsubmittedList.length === 0) {
        return <p className={styles.noDataMsg}>全社員が提出済みです</p>;
    }

    return (
        <table className={styles.table}>
            <thead>
                <tr>
                    <th>氏名</th>
                </tr>
            </thead>
            <tbody>
                {unsubmittedList.map((user) => (
                    <tr key={user.name}>
                        <td>{user.name}</td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
};