
'use client';

import React, { useEffect, useState } from 'react';

export default function EvaluationFormPage() {
  const [heading, setHeading] = useState<string>('');

  useEffect(() => {
    const storedHeading = localStorage.getItem('heading');
    if (storedHeading) {
      setHeading(storedHeading);
    }
  }, []);

  return (
    <main>
      <h1>{heading || '見出しがありません'}</h1>
      <p>これは評価フォームの画面です。</p>
    </main>
  );
}
