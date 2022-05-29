package io.allteran.cascade.workshopservice.service;

import io.allteran.cascade.workshopservice.domain.DeviceType;
import io.allteran.cascade.workshopservice.repo.DeviceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeviceTypeService {
    private final DeviceTypeRepository deviceTypeRepo;

    @Autowired
    public DeviceTypeService(DeviceTypeRepository deviceTypeRepo) {
        this.deviceTypeRepo = deviceTypeRepo;
    }

    public List<DeviceType> findAll() {
        return deviceTypeRepo.findAll(Sort.by("name").ascending());
    }

    public DeviceType findById(String id) {
        if(deviceTypeRepo.findById(id).isPresent()) {
            return deviceTypeRepo.findById(id).get();
        } else {
            throw new RuntimeException("DeviceType with id " + id + " not found in database");
        }
    }

    public DeviceType create(DeviceType deviceType) {
        deviceType.setId(UUID.randomUUID().toString());
        return deviceTypeRepo.save(deviceType);
    }

    public DeviceType update(DeviceType deviceType, String typeFromDbId) {
        DeviceType typeFromDb = findById(typeFromDbId);
        typeFromDb.setName(deviceType.getName());
        return deviceTypeRepo.save(typeFromDb);
    }

    public void delete(String id) {
        deviceTypeRepo.delete(findById(id));
    }
}
