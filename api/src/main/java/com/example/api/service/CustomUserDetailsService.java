
package com.example.api.service;

import com.example.api.entity.Employee;
import com.example.api.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public CustomUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String key) throws UsernameNotFoundException {
        Optional<Employee> opt;

        try {
            // 数字なら ID 検索
            Long id = Long.parseLong(key);
            opt = employeeRepository.findById(id);
        } catch (NumberFormatException ex) {
            // それ以外は email 検索
            opt = employeeRepository.findByEmail(key);
        }

        Employee emp = opt
            .filter(e -> !Boolean.TRUE.equals(e.getIsLocked()))
            .orElseThrow(() -> new UsernameNotFoundException("User not found or locked"));

        return User.withUsername(emp.getEmail())
                   .password(emp.getPassword())
                   .roles(emp.getPermission())
                   .build();
    }
}
