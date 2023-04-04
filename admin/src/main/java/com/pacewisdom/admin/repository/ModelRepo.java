package com.pacewisdom.admin.repository;

import com.pacewisdom.admin.Entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRepo extends JpaRepository<Model,String> {
}
