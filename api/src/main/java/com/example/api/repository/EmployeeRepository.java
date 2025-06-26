package com.example.api.repository;

import com.example.api.controller.TargetController.EmployeeProjection;
import com.example.api.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /** メールアドレスで 1 件取得（ログイン・管理チェック用） */
    Optional<Employee> findByEmail(String email);

    /** 一覧画面用のプロジェクション取得 */
    List<EmployeeProjection> findAllProjectedBy();
}
