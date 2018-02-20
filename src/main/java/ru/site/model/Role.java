package ru.site.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private long roleId;

    @Column(name = "role")
    private String role;

    @ManyToMany(mappedBy = "roles")
    private Set<Users> users;

    public Role() {
    }

    public Role(String s) {
        role = s;
    }

    public long getRoleId() {
        return roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

}
