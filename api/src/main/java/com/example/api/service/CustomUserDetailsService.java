
package com.example.api.service;

import com.example.api.entity.Employee;
import com.example.api.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public CustomUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (Boolean.TRUE.equals(employee.getIsLocked())) {
            throw new UsernameNotFoundException("アカウントがロックされています");
        }

        // Spring SecurityのUserオブジェクトに変換（ロールはpermissionカラムから）
        return new User(
                employee.getEmail(),
                employee.getPassword(),  // ここはハッシュ値
                Collections.emptyList()  // 権限リスト。必要ならGrantedAuthorityに変換
        );
    }
}
