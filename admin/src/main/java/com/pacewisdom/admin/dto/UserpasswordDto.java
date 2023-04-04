package com.pacewisdom.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserpasswordDto {

    private String email;

    private String oldPassword;

    private String newPassword;

    private String confirmPassword;
}
