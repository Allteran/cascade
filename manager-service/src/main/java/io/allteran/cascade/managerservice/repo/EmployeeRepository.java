package io.allteran.cascade.managerservice.repo;

import io.allteran.cascade.managerservice.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Employee findEmployeeByPhone(String phone);
    @Query("select e from Employee e where e.id in :idList")
    List<Employee> findAllById(List<String> idList);
}
