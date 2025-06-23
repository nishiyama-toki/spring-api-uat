'use client';

import React, { useEffect, useState, useMemo } from 'react';
import styles from './TermQuarterSelector.module.css'; 

// APIから取得するデータの型
type Phase = {
    phaseId: number;
    phaseNumber: number;
    name: string; // "1Q", "2Q" などが入っている
};

// 親から受け取るPropsの型
type Props = {
    value: string; // 選択中のphaseId (e.g., "1", "2")
    onChange: (selectedValue: string) => void;
};

export const TermQuarterSelector: React.FC<Props> = ({ value, onChange }) => {
    // APIから取得した全フェーズデータ
    const [allPhases, setAllPhases] = useState<Phase[]>([]);
    // 「期」のドロップダウンが開いているかどうかの状態
    const [termDropdownOpen, setTermDropdownOpen] = useState(false);
    // 選択されている「期」（18, 19など）
    const [selectedTerm, setSelectedTerm] = useState<number | null>(null);

    // 初回マウント時にAPIからデータを取得
    useEffect(() => {
        const fetchPhases = async () => {
            try {
                const response = await fetch('/api/phases');
                const raw: unknown = await response.json();

                // 👇 unknown[] にキャストして any 推論を完全排除
                const phasesArray = Array.isArray(raw) ? raw : [];

                // ✅ any を完全排除し、プロパティに安全にアクセス
                const mapped = (phasesArray as Record<string, unknown>[]).map((p): Phase => {
                    const phaseId = Number(p['phaseId']);
                    const phaseNumber = Number(p['phaseNumber']);
                    const name = String(p['name']);

                    if (Number.isNaN(phaseId) || Number.isNaN(phaseNumber) || !name) {
                        throw new Error('Invalid phase object received from API');
                    }

                    return {
                        phaseId,
                        phaseNumber,
                        name,
                    };
                });

                setAllPhases(mapped);
            } catch (err) {
                console.error(err);
            }
        };

        fetchPhases();
    }, []);

    // 親コンポーネントの value (選択中のphaseId) が変わった時に、
    // こちらのコンポーネントの selectedTerm も連動して更新する
    useEffect(() => {
        if (value) {
            const currentPhase = allPhases.find(p => String(p.phaseId) === value);
            if (currentPhase) {
                setSelectedTerm(currentPhase.phaseNumber);
            }
        } else {
            setSelectedTerm(null);
        }
    }, [value, allPhases]);

    // allPhasesからユニークな「期」のリストを生成（例: [18, 19]）
    const uniqueTerms = useMemo(() => {
        const terms = allPhases.map(p => p.phaseNumber);
        return [...new Set(terms)].sort((a, b) => b - a); // 新しい期が上に来るように降順ソート
    }, [allPhases]);

    // 選択された「期」に属する「Q」のリストを生成
    const quartersForSelectedTerm = useMemo(() => {
        if (!selectedTerm) return [];
        return allPhases
            .filter(p => p.phaseNumber === selectedTerm)
            .sort((a, b) => a.phaseId - b.phaseId);
    }, [selectedTerm, allPhases]);

    // 「期」を選択した時のハンドラ
    const handleTermSelect = (term: number) => {
        setSelectedTerm(term);
        setTermDropdownOpen(false);
        // 「期」が変わったら「Q」の選択はリセットする
        onChange(''); 
    };

    // 「Q」を選択した時のハンドラ
    const handleQuarterSelect = (phaseId: number) => {
        // 最終的に選択された phaseId を親に通知
        onChange(String(phaseId));
    };

    return (
        <div className={styles.termQuarterWrapper}>
            {/* --- 期選択ドロップダウン --- */}
            <div className={styles.dropdown}>
                <button
                    className={styles.dropdownButton}
                    onClick={() => setTermDropdownOpen(prev => !prev)}
                >
                    {selectedTerm ? `${selectedTerm}期` : "期とＱを選択"} ▼
                </button>
                {termDropdownOpen && (
                    <ul className={styles.dropdownMenu}>
                        {uniqueTerms.map((term) => (
                            <li key={term} onClick={() => handleTermSelect(term)}>
                                {term}期
                            </li>
                        ))}
                    </ul>
                )}
            </div>

            {/* --- Q（四半期）選択リスト --- */}
            {selectedTerm && (
                <div className={styles.quarterList}>
                    {quartersForSelectedTerm.map((phase) => (
                        <div
                            key={phase.phaseId}
                            className={`${styles.quarterItem} ${
                                String(phase.phaseId) === value ? styles.quarterSelected : ''
                            }`}
                            onClick={() => handleQuarterSelect(phase.phaseId)}
                        >
                            {/* バックエンドで生成された "18期 4Q" のような名前から "4Q" の部分だけを抽出 */}
                            {phase.name}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};
