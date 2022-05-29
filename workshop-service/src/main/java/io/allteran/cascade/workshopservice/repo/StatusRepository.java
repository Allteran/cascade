package io.allteran.cascade.workshopservice.repo;

import io.allteran.cascade.workshopservice.domain.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends MongoRepository<Status, String> {

}
