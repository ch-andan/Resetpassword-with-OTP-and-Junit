package com.pacewisdom.admin.repository;

import com.pacewisdom.admin.Entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepo extends JpaRepository<Permission,String> {
    @Query("select o from Permission o where o.role.id= :roleId")
    List<Permission> getAllUserPermisonsByRoleId(String roleId);
}
