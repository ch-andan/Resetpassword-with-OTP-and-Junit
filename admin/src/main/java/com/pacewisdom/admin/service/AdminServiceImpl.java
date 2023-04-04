package com.pacewisdom.admin.service;

import com.pacewisdom.admin.Entity.*;
import com.pacewisdom.admin.dto.*;
import com.pacewisdom.admin.exception.PWSException;
import com.pacewisdom.admin.repository.*;
import com.pacewisdom.admin.utility.DateUtils;
import com.pacewisdom.admin.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private RoleRepo roleRepo;


    @Autowired
    private UserRoleXrefRepo userRoleXrefRepo;
    @Autowired
    private ModelRepo modelRepo;
    @Autowired
    private PermissionRepo permissionRepo;

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public List<Signupdto> userSignUp(Signupdto signupdto) throws PWSException {

        Optional<User> optionalUser = repo.findUserByEmail(signupdto.getEmail());
        if (optionalUser.isPresent()) {
            throw new PWSException("User Already exists with Email " + signupdto.getEmail());
        }
       User user = new User();
        String id = UUID.randomUUID().toString();
        user.setId(id);
        user.setDateOfBirth(DateUtils.getUtilDateFromString(signupdto.getDateOfBirth()));
        user.setFirstName(signupdto.getFirstName());
        user.setIsActive(true);
        user.setLastName(signupdto.getLastName());
        user.setEmail(signupdto.getEmail());
        user.setPhoneNumber(signupdto.getPhoneNumber());
        PasswordEncoder encoder = new BCryptPasswordEncoder(8);
        if (!isStrongPassword(signupdto.getPassword())) {
            throw new PWSException("Password is not strong , at least one uppercase letter, one lowercase letter, one digit, and one special character needed");
        } user.setPassword(encoder.encode(signupdto.getPassword()));

        repo.save(user);


        return Collections.singletonList(signupdto);
    }

    @Override
    public List<User> fetchAllUser() throws PWSException {
        return repo.findAll();

    }

    @Override
    public List<Roledto> addrole(Roledto roledto) throws PWSException {
        Role role = new Role();
        String id = UUID.randomUUID().toString();
        role.setId(id);
        role.setName(roledto.getName());
        role.setIsActive(true);
        roleRepo.save(role);
        return Collections.singletonList(roledto);
    }

    @Override
    public Optional<Role> fetchRoleById(String Id) throws PWSException {
        Optional<Role> optrole = roleRepo.findById(Id);
        if (optrole.isPresent()) {
            return optrole;
        } else {
            throw new PWSException("role id doesn't exists" + Id);
        }
    }

    @Override
    public void saveOrUpdateUserXref(UserxrefDto userRoleXrefDTO) throws PWSException {
        Optional<UserRoleXref> optionalUserRoleXref = userRoleXrefRepo.findById(userRoleXrefDTO.getId());
        UserRoleXref userRoleXref = null;
        if (optionalUserRoleXref.isPresent()) {
            userRoleXref = optionalUserRoleXref.get();
        } else {
            userRoleXref = new UserRoleXref();
            String id = UUID.randomUUID().toString();
            userRoleXref.setId(id);

        }
        Optional<User> optionalUser = repo.findById(userRoleXrefDTO.getUserId());
        if (optionalUser.isPresent()) {
            userRoleXref.setUser(optionalUser.get());
        } else {
            throw new PWSException("User Doest Exist");
        }

        Optional<Role> optionalRole = roleRepo.findById(userRoleXrefDTO.getRoleId());
        if (optionalRole.isPresent()) {
            userRoleXref.setRole(optionalRole.get());
        } else {
            throw new PWSException("Role Doest Exist");
        }
        userRoleXref.setIsActive(userRoleXrefDTO.getIsActive());

        userRoleXrefRepo.save(userRoleXref);

    }

    @Override
    public void updateUserXref(UserxrefDto userxrefDto) throws PWSException {
        Optional<UserRoleXref> optionalUserRoleXref = userRoleXrefRepo.findById(userxrefDto.getId());
        UserRoleXref userRoleXref = null;
        if (optionalUserRoleXref.isPresent()) ;
        {
            userRoleXref = optionalUserRoleXref.get();
        }
        Optional<User> optionalUser = repo.findById(userxrefDto.getUserId());
        if (optionalUser.isPresent()) {
            userRoleXref.setUser(optionalUser.get());
        } else {
            throw new PWSException("User id not found");
        }
        Optional<Role> optionalRole = roleRepo.findById(userxrefDto.getUserId());
        if (optionalRole.isPresent()) {
            userRoleXref.setRole(optionalRole.get());
        } else {
            throw new PWSException("role id not found");
        }
        userRoleXref.setIsActive(userxrefDto.getIsActive());


    }

    @Override
    public void updateUserPassword(UserpasswordDto userpasswordDto) throws PWSException {
        Optional<User> optionalUser = repo.findUserByEmail(userpasswordDto.getEmail());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = null;
        if (!optionalUser.isPresent()) {
            throw new PWSException("user not found with email");
        }
        user = optionalUser.get();
        if (passwordEncoder.matches(userpasswordDto.getOldPassword(), user.getPassword())) {
            if (!userpasswordDto.getOldPassword().equals(userpasswordDto.getNewPassword())) {
                if (userpasswordDto.getNewPassword().equals(userpasswordDto.getConfirmPassword())) {
                    user.setPassword(passwordEncoder.encode(userpasswordDto.getConfirmPassword()));
                    repo.save(user);
                } else {
                    throw new PWSException("new password and confirm password doesn't match");
                }
            } else {
                throw new PWSException("old password cannot be your new password");
            }

        } else {
            throw new PWSException("password not matched");
        }

    }

    @Override
    public void addModel(ModelDto modelDto) throws PWSException {
        Model model = new Model();
        String id = UUID.randomUUID().toString();
        model.setId(id);
        model.setName(modelDto.getName());
        model.setIsActive(modelDto.getIsActive());
        modelRepo.save(model);
    }

    @Override
    public void addPermission(PermissionDto permissionDto) throws PWSException {
        Permission permission = new Permission();
        String id = UUID.randomUUID().toString();
        permission.setId(id);
        permission.setIsAdd(permissionDto.getIsAdd());
        permission.setIsActive(permissionDto.getIsActive());
        permission.setIsView(permissionDto.getIsView());
        permission.setIsUpdate(permissionDto.getIsUpdate());
        permission.setIsDelete(permissionDto.getIsDelete());
        Optional<Model> optionalModel = modelRepo.findById(permissionDto.getModelId());
        if (optionalModel.isPresent()) {
            permission.setModel(optionalModel.get());
        } else {
            throw new PWSException("Model id not found");
        }
        Optional<Role> optionalRole = roleRepo.findById(permissionDto.getRoleId());
        if (optionalRole.isPresent()) {
            permission.setRole(optionalRole.get());
        }
        permissionRepo.save(permission);
    }

    @Override
    public void updateResetPasswordToken(String token, String email) throws PWSException {
        User user = repo.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            repo.save(user);
        } else {
            throw new PWSException("Could not find any customer with the email " + email);
        }
    }

    @Override
    public User getByResetPasswordToken(String token) throws PWSException {
        return repo.findByResetPasswordToken(token);
    }

    @Override
    public void updatePassword(User user, String newPassword) throws PWSException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        repo.save(user);
    }

    @Override
    public UserBasicDetailsDTO getUserBasicInfoAfterLoginSuccess(String email) throws PWSException {
        Optional<User> optionalUser = repo.findUserByEmail(email);
        if(! optionalUser.isPresent())
            throw new PWSException("User not Exist with Email : " + email);
        User user = optionalUser.get();
        UserBasicDetailsDTO userBasicDetailsDTO =new UserBasicDetailsDTO();
        userBasicDetailsDTO.setUser(user);

        List<Role> roleList =userRoleXrefRepo.findAllUserRoleByUserId(user.getId());
        userBasicDetailsDTO.setRoleList(roleList);
        List<Permission> permissionList =null;
        if(roleList.size()>0)
            permissionList = permissionRepo.getAllUserPermisonsByRoleId(roleList.get(0).getId());

        userBasicDetailsDTO.setPermissionList(permissionList);
        return userBasicDetailsDTO;
    }

    private boolean isStrongPassword(String password) {
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        // check for at least one uppercase letter, one lowercase letter, one digit, and one special character
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowercase = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (isSpecialChar(ch)) {
                hasSpecialChar = true;
            }
        }

        // check if password meets all criteria
        return password.length() >= 8 && hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

    private boolean isSpecialChar(char ch) {
        String specialChars = "!@#$%^&*()_-+=[{]};:<>|./?";
        return specialChars.contains(Character.toString(ch));
    }


}







