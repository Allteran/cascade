package io.allteran.cascade.workshopservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "manager-service")
public interface ManagerFeignClient {
    @RequestMapping(method = RequestMethod.GET, value = "api/v1/manage/pos-type/list")
    public Object getListOfPos();
}
