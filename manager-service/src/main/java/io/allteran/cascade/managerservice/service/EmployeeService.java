package io.allteran.cascade.managerservice.service;

import io.allteran.cascade.managerservice.domain.Employee;
import io.allteran.cascade.managerservice.domain.Role;
import io.allteran.cascade.managerservice.exception.NotFoundException;
import io.allteran.cascade.managerservice.exception.UserFieldException;
import io.allteran.cascade.managerservice.repo.EmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EmployeeService {
    private static final LocalDateTime DEFAULT_DATE = LocalDateTime.of(1,1,1,1,1);
    private final EmployeeRepository employeeRepo;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public List<Employee> findAll() {
        return employeeRepo.findAll(Sort.by("creationDate").descending());
    }

    public Employee findById(String id) {
        if(employeeRepo.findById(id).isPresent()) {
            return employeeRepo.findById(id).get();
        } else {
            throw new NotFoundException("Employee with such id [" + id + "] not found in DB");
        }
    }

    public Employee findByPhone(String phone) {
        if(employeeRepo.findEmployeeByPhone(phone) != null && !employeeRepo.findEmployeeByPhone(phone).getId().isEmpty()) {
            return employeeRepo.findEmployeeByPhone(phone);
        } else {
            throw new NotFoundException("User with such phone [" + phone + "] not found in DB");
        }
    }

    public Employee create(Employee user) {
        if(!user.getPassword().equals(user.getPasswordConfirm())) {
            throw new UserFieldException("Passwords don't match");
        }
        /**
         * Next line uses regex to check if input string matches with special regex pattern
         * \ - means that it's gonna be regular expresion, so it waits for the pattern
         * \^ - means that it will starts from new line
         * ?(79) - means that at the begining gonna be two numbers "79"
         * \d - means that next text is digits
         * {9} - means that it gonna be only 9 number of digits, no more or less
         */
        if (!user.getPhone().matches("\\^?(79)\\d{9}")) {
            throw new UserFieldException("Phone number should have format 79xxxxxxxxx");
        }
        user.setCreationDate(LocalDateTime.now());
        //TODO: encode user password
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(Role.EMPLOYEE));
        user.setHireDate(user.getCreationDate());
        user.setDismissalDate(DEFAULT_DATE);

        return user;
    }

    public Employee update(Employee userFromDb, Employee user) {
        //TODO: using passwordEncoder check if typed passwords matches
        boolean isPasswordsMatches = userFromDb.getPassword().equals(user.getPassword());
        if(user.getNewPassword() != null && user.getPasswordConfirm() != null &&!user.getNewPassword().isEmpty()) {
            if(isPasswordsMatches) {
                if(user.getNewPassword().equals(user.getPasswordConfirm())) {
                    //TODO: dont forget to encode
                    userFromDb.setPassword(user.getNewPassword());
                } else {
                    throw new UserFieldException("New password doesnt match to password from confirm field");
                }
            } else {
                throw new UserFieldException("Entered password doesn't match to exiting");
            }
        } else {
            throw new UserFieldException("One of field is null or empty");
        }

        BeanUtils.copyProperties(user, userFromDb, "id", "password", "newPassword", "passwordConfirm");
        return employeeRepo.save(userFromDb);
    }

    public Employee createAdmin(Employee admin) {
        admin.setCreationDate(LocalDateTime.now());
        //TODO: encode user password
//        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
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

    public void delete(String id) {
        employeeRepo.delete(findById(id));
    }
}
