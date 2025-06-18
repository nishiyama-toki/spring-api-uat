
package com.example.api.repository; // このクラスが属するパッケージ（フォルダ階層）

import org.springframework.data.jpa.repository.JpaRepository; // Spring Data JPAの親インターフェース
import org.springframework.stereotype.Repository; // RepositoryであることをSpringに知らせるアノテーション

import com.example.api.entity.Employee; // 対象となるEntityクラス（employeesテーブルに対応）

// --- Repositoryインターフェース定義 ---
// JpaRepository を継承することで、DBへの基本操作（検索・登録・更新・削除）を自動で使えるようになる
@Repository // このインターフェースがリポジトリ層であることを明示（なくても動くが明示推奨）
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // 第1引数：対象のエンティティクラス
    // 第2引数：主キーの型（employeesテーブルの主キーidはInteger型）

    // --- 追加メソッド（必要なら） ---
    // 例：メールアドレスで検索する
    // Optional<Employee> findByEmail(String email);
}
