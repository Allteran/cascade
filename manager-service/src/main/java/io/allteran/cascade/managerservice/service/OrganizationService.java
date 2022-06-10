package io.allteran.cascade.managerservice.service;

import io.allteran.cascade.managerservice.domain.Organization;
import io.allteran.cascade.managerservice.repo.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {
    private final OrganizationRepository organizationRepo;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepo) {
        this.organizationRepo = organizationRepo;
    }

    public List<Organization> findAll() {
        return organizationRepo.findAll(Sort.by("name").ascending());
    }

    public Organization findById(String id) {
        if(organizationRepo.findById(id).isPresent()) {
            return organizationRepo.findById(id).get();
        } else {
            throw new RuntimeException("Organization with id " + id + " not found in database");
        }
    }

    public Organization create(Organization organization) {
        return organizationRepo.save(organization);
    }

    public Organization update(Organization organization, String organizationFromDbId) {
        Organization organizationFromDb = findById(organizationFromDbId);
        organizationFromDb.setName(organization.getName());
        organizationFromDb.setInn(organization.getInn());

        return organizationRepo.save(organization);
    }

    public void delete(String id) {
        organizationRepo.delete(findById(id));
    }
}
