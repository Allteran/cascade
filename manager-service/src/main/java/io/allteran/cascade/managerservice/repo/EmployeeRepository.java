package io.allteran.cascade.managerservice.repo;

import io.allteran.cascade.managerservice.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Employee findEmployeeByPhone(String phone);
}
