package io.allteran.cascade.managerservice.service;

import io.allteran.cascade.managerservice.domain.Role;
import io.allteran.cascade.managerservice.exception.NotFoundException;
import io.allteran.cascade.managerservice.repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepo;

    @Autowired
    public RoleService(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    public List<Role> findAll() {
        return roleRepo.findAll(Sort.by("name").ascending());
    }

    public Role findById(String id) {
        if(roleRepo.findById(id).isPresent()) {
            return roleRepo.findById(id).get();
        } else {
            throw new NotFoundException("Role with ID [" + id + "] not found in database");
        }
    }

    public Role create(Role role) {
        return roleRepo.save(role);
    }

    public Role update(Role role, String roleFromDbId) {
        Role roleFromDb = findById(roleFromDbId);
        roleFromDb.setName(role.getName());

        return roleRepo.save(roleFromDb);
    }

    public void delete(String id) {
        roleRepo.delete(findById(id));
    }
}
