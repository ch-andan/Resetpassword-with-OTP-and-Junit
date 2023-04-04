package com.pacewisdom.admin.Entity;

import com.pacewisdom.admin.utility.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Model extends AuditModel implements Serializable {
    @Id
    @Column(name = "id")
   private String id;

    @Column(name="is_active")
    @ColumnDefault("TRUE")
    private Boolean isActive;

    @Column(name="name", nullable = false, unique = true)
    private String name;

}

