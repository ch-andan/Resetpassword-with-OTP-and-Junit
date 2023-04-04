package com.pacewisdom.admin.Entity;

import com.pacewisdom.admin.utility.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role extends AuditModel implements Serializable {


    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "id")
    private String id;


    @Column(length=50,unique=true, nullable = false)
    private String name;

    @Column(name = "is_active")
    @ColumnDefault("TRUE")
    private  Boolean IsActive;

}

