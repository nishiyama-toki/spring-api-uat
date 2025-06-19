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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Employee> employee = employeeRepository.findByEmail(email);
        if (employee.isEmpty()) {
            throw new UsernameNotFoundException("Not found");
        }
        return new CustomUserDetails(employee.get());
    }


    Employee emp = opt
        .filter(e -> !Boolean.TRUE.equals(e.getIsLocked()))
        .orElseThrow(() -> new UsernameNotFoundException("User not found or locked"));

    return User.withUsername(emp.getEmail())
               .password(emp.getPassword())
               .roles(emp.getPermission())
               .build();
}


