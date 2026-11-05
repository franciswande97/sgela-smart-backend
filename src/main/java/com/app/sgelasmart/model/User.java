package com.app.sgelasmart.model;

import com.app.sgelasmart.common.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column
    //private UserRole role = UserRole.STUDENT;
    private Role role = Role.ROLE_STUDENT;

    public User() {
    }

    public User(String email, String password, String name) {
        this(email, password, name, Role.ROLE_STUDENT);
    }

    public User(String email, String password, String name, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = (role != null) ? role : Role.ROLE_STUDENT;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //public UserRole getRole() { return role != null ? role : UserRole.STUDENT; }
    //public void setRole(UserRole role) { this.role = role; }
    public Role getRole() {
    return role != null ? role : Role.ROLE_STUDENT;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
