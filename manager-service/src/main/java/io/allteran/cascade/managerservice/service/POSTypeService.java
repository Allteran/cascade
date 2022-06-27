package io.allteran.cascade.managerservice.service;

import io.allteran.cascade.managerservice.domain.POSType;
import io.allteran.cascade.managerservice.exception.NotFoundException;
import io.allteran.cascade.managerservice.repo.POSTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class POSTypeService {
    private final POSTypeRepository posTypeRepo;

    @Autowired
    public POSTypeService(POSTypeRepository posTypeRepo) {
        this.posTypeRepo = posTypeRepo;
    }

    public List<POSType> findAll() {
        return posTypeRepo.findAll(Sort.by("name").ascending());
    }

    public POSType findById(String id) {
        if(posTypeRepo.findById(id).isPresent()) {
            return posTypeRepo.findById(id).get();
        } else {
            throw new NotFoundException("POSType with id [" + id + "] not found in DB");
        }
    }

    public POSType create(POSType type) {
        return posTypeRepo.save(type);
    }

    public POSType update(POSType type, String typeFromDbId) {
        POSType typeFromDb = findById(typeFromDbId);
        typeFromDb.setName(type.getName());

        return posTypeRepo.save(typeFromDb);
    }

    public void delete(String id) {
        posTypeRepo.delete(findById(id));
    }
}
