package io.allteran.cascade.managerservice.service;

import io.allteran.cascade.managerservice.domain.Employee;
import io.allteran.cascade.managerservice.domain.Organization;
import io.allteran.cascade.managerservice.domain.Role;
import io.allteran.cascade.managerservice.exception.NotFoundException;
import io.allteran.cascade.managerservice.exception.UserFieldException;
import io.allteran.cascade.managerservice.repo.EmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class EmployeeService {
    private static final LocalDateTime DEFAULT_DATE = LocalDateTime.of(1,1,1,1,1);
    private final EmployeeRepository employeeRepo;
    private final OrganizationService orgService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    @Value("${role.employee}")
    private String ROLE_EMPLOYEE_ID;
    @Value("${role.admin}")
    private String ROLE_ADMIN_ID;
    @Value("${role.engineer}")
    private String ROLE_ENGINEER_ID;
    @Value("${role.head-engineer}")
    private String ROLE_HEAD_ENGINEER_ID;
    @Value("${role.manager}")
    private String ROLE_MANAGER_ID;
    @Value("${role.director}")
    private String ROLE_DIRECTOR_ID;
    @Value("${organization.default}")
    private String ORGANIZATION_DEFAULT_ID;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepo, OrganizationService orgService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.employeeRepo = employeeRepo;
        this.orgService = orgService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Employee> findAll() {
        return employeeRepo.findAll(Sort.by("creationDate").descending());
    }

    public List<Employee> findAllById(List<String> idList) {
        return employeeRepo.findAllById(idList);
    }

    public List<Employee> findAllByRoles(List<Role> roles) {
        Set<Employee> empSet = new HashSet<>(employeeRepo.findByRolesIn(roles));
        return new ArrayList<>(empSet);
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPasswordConfirm("");
        if(user.getRoles().isEmpty()) {
            user.setRoles(Collections.singleton(roleService.findById(ROLE_EMPLOYEE_ID)));
        }
        user.setHireDate(user.getCreationDate());
        user.setDismissalDate(DEFAULT_DATE);

        return employeeRepo.save(user);
    }

    public Employee update(Employee userFromDb, Employee user) {
        //TODO: using passwordEncoder check if typed passwords matches
        boolean isPasswordsMatches = passwordEncoder.matches(user.getPassword(), userFromDb.getPassword());
        if(user.getNewPassword() != null && user.getPasswordConfirm() != null &&!user.getNewPassword().isEmpty()) {
            if(isPasswordsMatches) {
                if(user.getNewPassword().equals(user.getPasswordConfirm())) {
                    userFromDb.setPassword(passwordEncoder.encode(user.getNewPassword()));
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

    public Employee createAdmin() {
        Employee admin = new Employee();

        admin.setFirstName("Admin");
        admin.setLastName("Administrator");
        admin.setPhone("79021335276");
        admin.setActive(true);

        admin.setCreationDate(LocalDateTime.now());
        //TODO: encode user password
        admin.setPassword(passwordEncoder.encode("123456789"));
        admin.setHireDate(LocalDateTime.now());
        admin.setDismissalDate(DEFAULT_DATE);
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.findById(ROLE_EMPLOYEE_ID));
        roles.add(roleService.findById(ROLE_ADMIN_ID));
        roles.add(roleService.findById(ROLE_ENGINEER_ID));
        roles.add(roleService.findById(ROLE_HEAD_ENGINEER_ID));
        roles.add(roleService.findById(ROLE_DIRECTOR_ID));
        roles.add(roleService.findById(ROLE_MANAGER_ID));

        admin.setRoles(roles);

        Organization org = orgService.findById(ORGANIZATION_DEFAULT_ID);
        admin.setOrganization(org);


        return employeeRepo.save(admin);
    }
    
    public Employee updateRaw(Employee user, String userFromDbId) {
        Employee userFromDb = findById(userFromDbId);
        userFromDb.setPassword(passwordEncoder.encode(user.getNewPassword()));
        BeanUtils.copyProperties(user, userFromDb, "id", "password", "newPassword", "passwordConfirm");

        return employeeRepo.save(userFromDb);
    }

    public Employee createEngineer() {
        Employee engineer = new Employee();
        engineer.setFirstName("Simple");
        engineer.setLastName("Engineer");
        engineer.setPhone("79522222222");
        engineer.setActive(true);
        engineer.setCreationDate(LocalDateTime.now());
        engineer.setHireDate(LocalDateTime.now());
        engineer.setDismissalDate(DEFAULT_DATE);
        engineer.setPassword(passwordEncoder.encode("11111111"));
        engineer.setRoles(Collections.singleton(roleService.findById(ROLE_ENGINEER_ID)));

        Organization org = orgService.findById(ORGANIZATION_DEFAULT_ID);
        engineer.setOrganization(org);

        return employeeRepo.save(engineer);
    }

    public Employee createHeadEngineer() {
        Employee headEngineer = new Employee();
        headEngineer.setFirstName("Head");
        headEngineer.setLastName("Engineer");
        headEngineer.setPhone("79000000000");
        headEngineer.setActive(true);
        headEngineer.setCreationDate(LocalDateTime.now());
        headEngineer.setHireDate(LocalDateTime.now());
        headEngineer.setDismissalDate(DEFAULT_DATE);
        headEngineer.setPassword(passwordEncoder.encode("22222222"));

        headEngineer.setRoles(Collections.singleton(roleService.findById(ROLE_HEAD_ENGINEER_ID)));

        Organization org = orgService.findById(ORGANIZATION_DEFAULT_ID);
        headEngineer.setOrganization(org);

        return employeeRepo.save(headEngineer);
    }

    public void delete(String id) {
        employeeRepo.delete(findById(id));
    }
}
