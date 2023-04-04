package com.pacewisdom.admin.Entity;

import com.pacewisdom.admin.utility.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Permission extends AuditModel implements Serializable {


    @Id
    @Column(name = "id")
    private String id;

    @Column(name="is_active",nullable = false)
    @ColumnDefault("TRUE")
    private Boolean isActive;

    @Column(name="is_view",nullable = false)
    @ColumnDefault("TRUE")
    private Boolean isView;

    @Column(name="is_add",nullable = false)
    @ColumnDefault("TRUE")
    private Boolean isAdd;

    @Column(name="is_update",nullable = false)
    @ColumnDefault("TRUE")
    private Boolean isUpdate;

    @Column(name="is_delete",nullable = false)
    @ColumnDefault("TRUE")
    private Boolean isDelete;

    @ManyToOne
    @JoinColumn(name="model_id")
    private Model model;

    @ManyToOne
    @JoinColumn(name="role_id")
    private Role role;



}

