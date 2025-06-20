package com.example.api.service;

import com.example.api.entity.Employee;
import com.example.api.repository.EmployeeRepository;
import com.example.api.security.UserDetailsImpl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public CustomUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // DBからemailで検索
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // ロックされている場合は例外
        if (Boolean.TRUE.equals(employee.getIsLocked())) {
            throw new UsernameNotFoundException("アカウントがロックされています");
        }

        // Spring Securityで使うUserDetailsImplに変換して返す
        return new UserDetailsImpl(employee);
    }
}
