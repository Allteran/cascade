package io.allteran.cascade.managerservice.repo;

import io.allteran.cascade.managerservice.domain.POSType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface POSTypeRepository extends JpaRepository<POSType, String> {
}
