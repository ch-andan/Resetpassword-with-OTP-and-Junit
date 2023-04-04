package com.pacewisdom.admin.dto;
import com.pacewisdom.admin.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDto {


    private Boolean isActive;


    private Boolean isView;

    private Boolean isAdd;

    private Boolean isUpdate;


    private Boolean isDelete;


    private String modelId;


    private String roleId;

}
