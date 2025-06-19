
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
        // DBからemailで検索し、ロックされていないことも確認
        return employeeRepository.findByEmail(email)
                .filter(emp -> !Boolean.TRUE.equals(emp.getIsLocked()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found or locked"));
    }
}
