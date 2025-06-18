package com.example.api.repository;

import com.example.api.controller.TargetController.EmployeeProjection;
import com.example.api.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    // プロジェクションを使って一部のフィールドだけ取り出す
    List<EmployeeProjection> findAllProjectedBy();
}
