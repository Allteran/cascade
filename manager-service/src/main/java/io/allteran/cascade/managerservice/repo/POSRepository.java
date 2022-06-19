package io.allteran.cascade.managerservice.repo;

import io.allteran.cascade.managerservice.domain.PointOfSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface POSRepository extends JpaRepository<PointOfSales, String> {
    @Query("select p from PointOfSales p where p.id in :idList")
    List<PointOfSales> findAllById(List<String> idList);
}
