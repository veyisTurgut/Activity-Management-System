package yte.intern.project.manageActivities.entity;

import lombok.Getter;
import lombok.Setter;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import yte.intern.project.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * Kurum kullanıcısı.
 * TODO security sonrası sadece bu entity login yapacak sisteme,
 * TODO dış kullanıcı giriş yapmadan sadece katıl tuşuna basarak kayıt oluşturacak.
 */
@Entity
@Getter
public class Admin extends BaseEntity implements UserDetails {

    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "AUTHORITIES",
            joinColumns = @JoinColumn(name = "ADMIN_ID"),
            inverseJoinColumns = @JoinColumn(name = "AUTHORITY_ID")
    )
    private Set<Authority> authorities;


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


}

