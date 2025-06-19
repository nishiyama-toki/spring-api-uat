// package com.example.api.security;

// import com.example.api.entity.Employee;
// import com.example.api.repository.EmployeeRepository;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;

// @Service
// public class CustomUserDetailsService implements UserDetailsService {

//     private final EmployeeRepository employeeRepository;

//     public CustomUserDetailsService(EmployeeRepository employeeRepository) {
//         this.employeeRepository = employeeRepository;
//     }

//     @Override
//     public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//         Employee employee = employeeRepository.findByEmail(email)
//                 .orElseThrow(() -> new UsernameNotFoundException("User not found"));

//         return new UserDetailsImpl(employee);
//     }
// }

package com.example.api.service;

import com.example.api.entity.Employee;
import com.example.api.repository.EmployeeRepository;
import com.example.api.security.UserDetailsImpl; // ← 追加（元のUserDetailsImplを使う）
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

        return new UserDetailsImpl(employee); // ← ここが重要
    }
}
