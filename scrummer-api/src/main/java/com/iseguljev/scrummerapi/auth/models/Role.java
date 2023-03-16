package com.iseguljev.scrummerapi.auth.models;

import com.iseguljev.scrummerapi.auth.models.Privilege;
import com.iseguljev.scrummerapi.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NonNull
    private String name;

    @ManyToMany(mappedBy = "roles")
    Collection<User> users;

    @ManyToMany
    @JoinTable(
            name = "role_privilege",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id")
    )
    Collection<Privilege> privileges;
}
