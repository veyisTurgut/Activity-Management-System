package yte.intern.project.manageActivities.entity;

import lombok.Data;
//import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import yte.intern.project.common.entity.BaseEntity;
import yte.intern.project.manageActivities.entity.Admin;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
@Data
public class Authority extends BaseEntity implements GrantedAuthority {

    @ManyToMany(mappedBy = "authorities")
    private Set<Admin> adminEntities;

    private String authority;
}

