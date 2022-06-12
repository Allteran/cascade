package io.allteran.cascade.managerservice.service;

import io.allteran.cascade.managerservice.domain.PointOfSales;
import io.allteran.cascade.managerservice.repo.POSRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class POSService {
    private final POSRepository posRepo;

    @Autowired
    public POSService(POSRepository posRepo) {
        this.posRepo = posRepo;
    }

    public List<PointOfSales> findAll() {
        return posRepo.findAll(Sort.by("city").ascending());
    }

    public PointOfSales findById(String id) {
        if(posRepo.findById(id).isPresent()) {
            return posRepo.findById(id).get();
        } else {
            throw new RuntimeException("Point of sales with id [" + id + "] not found in DB");
        }
    }

    public PointOfSales create(PointOfSales pos) {
        return posRepo.save(pos);
    }

    public PointOfSales update(PointOfSales pos, String posFromDbId) {
        PointOfSales posFromDb = findById(posFromDbId);

        BeanUtils.copyProperties(pos, posFromDb, "id");
        return posRepo.save(posFromDb);
    }

    public void delete(String id) {
        posRepo.delete(findById(id));
    }
}
