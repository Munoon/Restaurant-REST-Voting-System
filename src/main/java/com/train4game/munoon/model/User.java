package com.train4game.munoon.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@NamedQueries({
        @NamedQuery(name = User.DELETE, query = "DELETE FROM User u WHERE u.id=:id"),
        @NamedQuery(name = User.BY_EMAIL, query = "SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email=:email"),
        @NamedQuery(name = User.GET_ALL, query = "SELECT u FROM User u LEFT JOIN FETCH u.roles ORDER BY u.name, u.email")
})
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "emil", name = "users_unique_email_idx"))
public class User extends AbstractNamedEntity {
    public final static String DELETE = "User.delete";
    public final static String BY_EMAIL = "User.getByEmail";
    public final static String GET_ALL = "User.getAll";

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotBlank
    @Size(max = 200)
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(max = 200)
    private String password;

    @Column(name = "registered", nullable = false, columnDefinition = "TIMESTAMP DEFAULT now()")
    @NotNull
    private LocalDateTime registered;

    @Column(name = "enabled", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean enabled;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Roles> roles;

    public User() {
    }

    public User(User u) {
        this(u.getId(), u.getName(), u.getEmail(), u.getPassword(), u.getRegistered(), u.isEnabled(), u.getRoles());
    }

    public User(Integer id, String name, String email, String password, LocalDateTime registered, boolean enabled, Set<Roles> roles) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.registered = registered;
        this.enabled = enabled;
        this.roles = roles;
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

    public LocalDateTime getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDateTime registered) {
        this.registered = registered;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Set<Roles> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", registered=" + registered +
                ", enabled=" + enabled +
                ", roles=" + roles +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
