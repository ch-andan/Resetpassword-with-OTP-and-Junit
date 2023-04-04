package com.pacewisdom.admin.service;

import com.pacewisdom.admin.Entity.Role;
import com.pacewisdom.admin.Entity.User;
import com.pacewisdom.admin.exception.PWSException;
import com.pacewisdom.admin.dto.*;

import java.util.List;
import java.util.Optional;


public interface AdminService {

    List<Signupdto> userSignUp(Signupdto signupdto) throws PWSException;

    List<User> fetchAllUser() throws PWSException;

    List<Roledto> addrole(Roledto roledto) throws PWSException;

    Optional<Role> fetchRoleById(String Id) throws PWSException;

    void saveOrUpdateUserXref(UserxrefDto userRoleXrefDTO) throws PWSException;

    void updateUserXref(UserxrefDto userxrefDto) throws PWSException;

    void updateUserPassword(UserpasswordDto userpasswordDto) throws PWSException;


    void addModel(ModelDto modelDto) throws PWSException;

    void addPermission(PermissionDto permissionDto) throws PWSException;

    public void updateResetPasswordToken(String token, String email) throws PWSException;

    public User getByResetPasswordToken(String token) throws PWSException;

    public void updatePassword(User user, String newPassword) throws PWSException;

}