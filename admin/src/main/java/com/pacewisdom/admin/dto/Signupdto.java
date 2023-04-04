package com.pacewisdom.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Signupdto {


    private String firstName;

    private String lastName;

    private String dateOfBirth;

    private String email;

    private String phoneNumber;

    private String password;



}
