package ru.site.model;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class Users implements UserDetails {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private long id;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns =
    @JoinColumn(name = "user_id"), inverseJoinColumns =
    @JoinColumn(name = "role_id"))
    private Set<Role> roles;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "REGISTRATION_CODE")
    private RegistrationCode registrationCode;
    private String password;
    private String username;
    private String email;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;


    protected Users() {
    }

    public Users(Set<Role> userRoles, String username, String email, String password, RegistrationCode code) {
        this.roles = userRoles;
        this.registrationCode = code;
        this.email = email;
        this.password = password;
        this.username = username;
        accountNonExpired = true;
        accountNonLocked = true;
        credentialsNonExpired = true;
        enabled = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole()))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public RegistrationCode getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(RegistrationCode registrationCode) {
        this.registrationCode = registrationCode;
    }
}
