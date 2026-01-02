package com.shoham.chat_app.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "chat_user")
public class User
        extends BaseEntity
        implements UserDetails
{
    @Column(nullable = false, updatable = false, unique = true)
    private String username;

    private String name;

    private String password;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private Set<Group> groups = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return List.of();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode()
    {
        return getClass().hashCode();
    }
}
