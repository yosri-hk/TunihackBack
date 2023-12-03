package com.example.tunihack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemandeSangRequest {
    private String ville;
    private String bloodType;
    private String hospital;
    private String status;

}
