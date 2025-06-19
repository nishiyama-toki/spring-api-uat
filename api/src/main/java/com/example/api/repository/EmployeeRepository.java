package com.example.api.repository;

import com.example.api.controller.TargetController.EmployeeProjection;
import com.example.api.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    // プロジェクションを使って一部のフィールドだけ取り出す
    List<EmployeeProjection> findAllProjectedBy();

    // メールアドレスでユーザーを検索するメソッド（ログイン時に使用）
    Optional<Employee> findByEmail(String email);
}
