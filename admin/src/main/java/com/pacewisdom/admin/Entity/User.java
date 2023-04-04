package com.pacewisdom.admin.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pacewisdom.admin.utility.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }) })
public class User extends AuditModel implements Serializable,UserDetails {

    private static final long serialVersionUID = 1L;


    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private String  id;

    @Column(name = "first_name", length = 50,nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50,nullable = false)
    private String lastName;

    @Column(name="dob",nullable = false)
    private Date dateOfBirth;

    @Column(name = "email", length = 100,nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number",length = 12,nullable = false)
    private String phoneNumber;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "is_active", nullable = false)
    @ColumnDefault("TRUE")
    private Boolean isActive;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "oneTimePassword")
    private String oneTimePassword;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }


}
