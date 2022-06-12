package io.allteran.cascade.managerservice.service;

import io.allteran.cascade.managerservice.domain.Employee;
import io.allteran.cascade.managerservice.domain.Role;
import io.allteran.cascade.managerservice.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepo, PasswordEncoder passwordEncoder) {
        this.employeeRepo = employeeRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Employee> findAll() {
        return employeeRepo.findAll(Sort.by("creationDate").descending());
    }

    public Employee findById(String id) {
        if(employeeRepo.findById(id).isPresent()) {
            return employeeRepo.findById(id).get();
        } else {
            throw new RuntimeException("Employee with such id [" + id + "] not found in DB");
        }
    }

    public Employee findByPhone(String phone) {
        if(employeeRepo.findEmployeeByPhone(phone) != null && !employeeRepo.findEmployeeByPhone(phone).getId().isEmpty()) {
            return employeeRepo.findEmployeeByPhone(phone);
        } else {
            throw new RuntimeException("User with such phone [" + phone + "] not found in DB");
        }
    }

    public Employee create(Employee user) {
        user.setCreationDate(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(Role.EMPLOYEE));

        return user;
    }

    public Employee createAdmin(Employee admin) {
        admin.setCreationDate(LocalDateTime.now());
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(Role.EMPLOYEE);
        roles.add(Role.ADMIN);
        roles.add(Role.ENGINEER);
        roles.add(Role.HEAD_ENGINEER);
        roles.add(Role.DIRECTOR);
        roles.add(Role.MANAGER);
        admin.setRoles(roles);

        return admin;
    }

}
