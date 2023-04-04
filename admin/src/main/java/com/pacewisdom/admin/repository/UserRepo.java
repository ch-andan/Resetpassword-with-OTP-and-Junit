package com.pacewisdom.admin.repository;

import com.pacewisdom.admin.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,String> {

    @Query("select o from User o where o.email = :email")
    Optional<User> findUserByEmail(String email);


    @Query("SELECT c FROM User c WHERE c.email = ?1")
    public User findByEmail(String email);
    public User findByResetPasswordToken(String token);



}
