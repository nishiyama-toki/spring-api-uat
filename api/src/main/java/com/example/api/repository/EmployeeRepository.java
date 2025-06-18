package com.example.api.repository;

import com.example.api.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // このファイルは基本的に空でOKです。
    // JpaRepositoryを継承することで、基本的なDB操作（findByIdなど）が自動的に使えるようになります。
}
