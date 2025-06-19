// 従業員検索/認証用
package com.example.api.repository;

import com.example.api.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Employeeエンティティに対するリポジトリ（DBアクセス）を定義
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    // メールアドレスでユーザーを検索するメソッド（ログイン時に使用）
    Optional<Employee> findByEmail(String email);
}
