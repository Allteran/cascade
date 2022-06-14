package io.allteran.cascade.workshopservice.controller;

import io.allteran.cascade.workshopservice.client.ManagerFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v0/test")
public class TestController {
    private final ManagerFeignClient managerFeignClient;

    @Autowired
    public TestController(ManagerFeignClient managerFeignClient) {
        this.managerFeignClient = managerFeignClient;
    }

    @GetMapping("/pos-list")
    public Object getPosList() {
        return managerFeignClient.getListOfPos();
    }
}
