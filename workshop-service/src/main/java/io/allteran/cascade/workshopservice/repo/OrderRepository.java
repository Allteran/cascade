package io.allteran.cascade.workshopservice.repo;
import io.allteran.cascade.workshopservice.domain.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
}
