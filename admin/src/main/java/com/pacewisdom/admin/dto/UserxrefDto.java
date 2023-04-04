package com.pacewisdom.admin.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserxrefDto {

    private String id;

    private String userId;

    private String roleId;

    private Boolean isActive;
}
