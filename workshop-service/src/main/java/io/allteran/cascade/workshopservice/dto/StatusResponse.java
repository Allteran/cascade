package io.allteran.cascade.workshopservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusResponse {
    private String message;
    private List<StatusDTO> statusList;
}
