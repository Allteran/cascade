package io.allteran.cascade.workshopservice.service;

import io.allteran.cascade.workshopservice.domain.Status;
import io.allteran.cascade.workshopservice.repo.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StatusService {
    private final StatusRepository statusRepo;

    @Autowired
    public StatusService(StatusRepository statusRepo) {
        this.statusRepo = statusRepo;
    }

    public List<Status> findAll() {
        return statusRepo.findAll(Sort.by("name").ascending());
    }

    public Status findById(String id) {
        if(statusRepo.findById(id).isPresent()) {
            return statusRepo.findById(id).get();
        } else {
            throw new RuntimeException("Status with id " + id + " not found in database");
        }
    }

    public Status create(Status status) {
        status.setId(UUID.randomUUID().toString());
        return statusRepo.save(status);
    }

    public Status update(Status status, String statusFromDbId) {
        Status statusFromDb = findById(statusFromDbId);
        statusFromDb.setName(status.getName());
        return statusRepo.save(statusFromDb);
    }

    public void delete(String id) {
        statusRepo.delete(findById(id));
    }
}