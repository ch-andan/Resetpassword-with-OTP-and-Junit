package com.pacewisdom.admin.repository;

import com.pacewisdom.admin.Entity.Role;
import com.pacewisdom.admin.Entity.UserRoleXref;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleXrefRepo extends JpaRepository<UserRoleXref,String> {

    @Query("select o.role from UserRoleXref o where o.user.id= :id and o.role.IsActive=TRUE")
    List<Role> findAllUserRoleByUserId(String id);
}
