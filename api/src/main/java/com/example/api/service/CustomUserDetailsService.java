package com.example.api.service;

import com.example.api.entity.Employee;
import com.example.api.repository.EmployeeRepository;
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
        Long userId = Long.parseLong(email);

        Employee employee = employeeRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        if (Boolean.TRUE.equals(employee.getIsLocked())) {
            throw new UsernameNotFoundException("アカウントがロックされています");
        }

        return employee;
    }

    // JwtAuthenticationFilterなどでID検索が必要な場合
    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (Boolean.TRUE.equals(employee.getIsLocked())) {
            throw new UsernameNotFoundException("アカウントがロックされています");
        }

        return employee;
    }
}