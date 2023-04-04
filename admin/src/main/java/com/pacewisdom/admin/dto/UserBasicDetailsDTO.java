package com.pacewisdom.admin.dto;
import com.pacewisdom.admin.Entity.Permission;
import com.pacewisdom.admin.Entity.Role;
import com.pacewisdom.admin.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBasicDetailsDTO {


    private User user;

    private List<Role> roleList;


    private List<Permission> permissionList;



}

