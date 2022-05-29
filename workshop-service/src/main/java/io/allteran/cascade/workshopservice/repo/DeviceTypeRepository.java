package io.allteran.cascade.workshopservice.repo;

import io.allteran.cascade.workshopservice.domain.DeviceType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceTypeRepository extends MongoRepository<DeviceType, String> {
}
