package com.iseguljev.scrummerapi.auth.repositories;

import com.iseguljev.scrummerapi.auth.models.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role,Long> {
    Optional<Role> findByName(String roleName);
}
