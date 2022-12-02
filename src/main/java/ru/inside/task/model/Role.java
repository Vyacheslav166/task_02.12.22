package ru.inside.task.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

/**
 * Role.
 */

@Entity
@Data
@NoArgsConstructor
@Table(name = "o_role")
public class Role implements GrantedAuthority {
    /**
     * Id role.
     */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Role's name.
     */
    @Column(nullable=false, unique = true)
    private String name;

    /**
     * Role's short name.
     */
    @Column(nullable=false, unique = true)
    private String slug;

    /**
     * Role's description.
     */
    @Column
    private String description;

    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role(Long id, String name, String slug, String description) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
    }

    @Override
    public String getAuthority() {
        return getName();
    }
}
