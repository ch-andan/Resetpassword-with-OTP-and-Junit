package com.pacewisdom.admin.Entity;

import com.pacewisdom.admin.utility.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Skill extends AuditModel implements Serializable {
    @Id
    @Column(name = "id")
    private String id;


    @Column(name = "name" , length=50,unique=true, nullable = false)
    private String name;

    @Column(name = "is_active")
    @ColumnDefault("TRUE")
    private  Boolean IsActive;
}
