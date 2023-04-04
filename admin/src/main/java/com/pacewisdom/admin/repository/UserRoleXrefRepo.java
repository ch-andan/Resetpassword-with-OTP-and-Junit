package com.pacewisdom.admin.repository;

import com.pacewisdom.admin.Entity.UserRoleXref;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleXrefRepo extends JpaRepository<UserRoleXref,String> {
}
