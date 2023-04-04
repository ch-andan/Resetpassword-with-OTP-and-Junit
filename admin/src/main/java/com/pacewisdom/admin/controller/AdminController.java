package com.pacewisdom.admin.controller;

import com.pacewisdom.admin.Entity.Role;
import com.pacewisdom.admin.Entity.User;
import com.pacewisdom.admin.dto.*;
import com.pacewisdom.admin.exception.PWSException;
import com.pacewisdom.admin.securityconfig.UserDetailsServiceImpl;
import com.pacewisdom.admin.service.AdminService;
import com.pacewisdom.admin.utility.ApiSuccess;
import com.pacewisdom.admin.utility.CommonUtils;
import com.pacewisdom.admin.utility.JwtUtil;
import io.jsonwebtoken.impl.DefaultClaims;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@RestControllerAdvice
@Builder
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private UserDetailsServiceImpl userDetailsService;






    @PostMapping("/public/signup")
    public ResponseEntity<Object> signup(@RequestBody Signupdto dto) throws PWSException {
        adminService.userSignUp(dto);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.CREATED));
    }

    @GetMapping("/users")
    @Operation(summary = "fetch user", description = "fetch users, u need Authentication key to access to this token")
    public ResponseEntity<Object> fetchAllUsers() throws PWSException {
        List<User> opt = adminService.fetchAllUser();
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, opt));
    }

    @PostMapping("/add-role")
    public ResponseEntity<Object> addRoles(@RequestBody Roledto roledto) throws PWSException {
        adminService.addrole(roledto);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.CREATED));
    }

    @GetMapping("/role/id")
    public ResponseEntity<Object> fetchRoleById(@RequestParam String id) throws PWSException {
        Optional<Role> role = adminService.fetchRoleById(id);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, role));
    }

    @PostMapping("/userrolexref")
    private ResponseEntity<Object> addUserXref(@RequestBody UserxrefDto userxrefDto) throws PWSException {
        adminService.saveOrUpdateUserXref(userxrefDto);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }

    @PutMapping("/updatepassword")
    private ResponseEntity<Object> updatePassword(@RequestBody UserpasswordDto userpasswordDto) throws PWSException {
        adminService.updateUserPassword(userpasswordDto);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }

    @PostMapping("/addmodel")
    private ResponseEntity<Object> addModel(@RequestBody ModelDto modelDto) throws PWSException {
        adminService.addModel(modelDto);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }

    @PostMapping("/addpermission")
    public ResponseEntity<Object> addPermission(@RequestBody PermissionDto permissionDto) throws PWSException {
        adminService.addPermission(permissionDto);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }

    @PostMapping("/public/authenticate")
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody Logindto logindto)
            throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    logindto.getEmail(), logindto.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        UserDetails userdetails = userDetailsService.loadUserByUsername(logindto.getEmail());
        String token = jwtUtil.generateToken(userdetails);
        return ResponseEntity.ok(new ResponseDto(token));
    }

    @PostMapping("/public/refreshtoken")
    public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
        // From the HttpRequest get the claims
        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");

        Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
        String token = jwtUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
        return ResponseEntity.ok(new ResponseDto(token));
    }

    public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
        Map<String, Object> expectedMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;
    }

    @GetMapping("get/private/user/details")
    public ResponseEntity<Object> getUserBasicInfoAfterLoginSuccess(@RequestParam  String email) throws PWSException{
        UserBasicDetailsDTO userBasicDetailsDTO = adminService.getUserBasicInfoAfterLoginSuccess(email);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, userBasicDetailsDTO));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            if(jwtUtil.isTokenBlacklisted(jwt))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalidated.");
            String userDetails = jwtUtil.getUsernameFromToken(jwt);
            // Invalidate the token
            jwtUtil.invalidateToken(jwt);
            // Clear user details from session
            request.getSession().removeAttribute("userDetails");
            return ResponseEntity.ok("Successfully logged out.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }
    }



    }


