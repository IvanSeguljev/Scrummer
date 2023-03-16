package com.iseguljev.scrummerapi.auth.repositories;

import com.iseguljev.scrummerapi.auth.models.Privilege;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrivilegeRepository extends CrudRepository<Privilege,Long> {
    Optional<Privilege> findByName(String name);
}
