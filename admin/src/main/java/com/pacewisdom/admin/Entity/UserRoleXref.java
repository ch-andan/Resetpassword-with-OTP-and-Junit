package com.pacewisdom.admin.Entity;

import com.pacewisdom.admin.utility.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRoleXref extends AuditModel implements Serializable {
    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="role_id")
    private Role role;

    @Column(name="is_active",nullable=false)
    @ColumnDefault("TRUE")
    private Boolean isActive;
}
