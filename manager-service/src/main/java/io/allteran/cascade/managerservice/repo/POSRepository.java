package io.allteran.cascade.managerservice.repo;

import io.allteran.cascade.managerservice.domain.PointOfSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface POSRepository extends JpaRepository<PointOfSales, String> {
}
