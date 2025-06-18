package com.example.api.service;

import com.example.api.entity.Employee;
import com.example.api.repository.EmployeeRepository;
import com.example.api.security.CustomUserDetails;
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
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (Boolean.TRUE.equals(employee.getIsLocked())) {
            throw new UsernameNotFoundException("アカウントがロックされています");
        }

        return new CustomUserDetails(employee); // ← ここで自作クラスを返す
    }

    // 追加！これがJwtAuthenticationFilter用
    public UserDetails loadUserById(int id) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (Boolean.TRUE.equals(employee.getIsLocked())) {
            throw new UsernameNotFoundException("アカウントがロックされています");
        }

        return new CustomUserDetails(employee);
    }
}
