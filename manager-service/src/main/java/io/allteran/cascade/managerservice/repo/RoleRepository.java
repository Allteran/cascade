package io.allteran.cascade.managerservice.repo;

import io.allteran.cascade.managerservice.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
