package com.iseguljev.scrummerapi.data.auth;

import com.iseguljev.scrummerapi.auth.models.Privilege;
import com.iseguljev.scrummerapi.auth.models.Role;
import com.iseguljev.scrummerapi.auth.repositories.PrivilegeRepository;
import com.iseguljev.scrummerapi.auth.repositories.RoleRepository;
import com.iseguljev.scrummerapi.user.User;
import com.iseguljev.scrummerapi.user.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.slf4j.SLF4JLoggerContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private Logger logger = LoggerFactory.getLogger(DataLoader.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    private boolean isSetup = false;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // User related privileges
        Privilege userWrite = createPrivilegeIfNotFound("USER_WRITE");
        Privilege userRead = createPrivilegeIfNotFound("USER_READ");
        Privilege userEdit = createPrivilegeIfNotFound("USER_EDIT");
        Privilege userDelete = createPrivilegeIfNotFound("USER_DELETE");

        List<Privilege> adminPrivileges = Arrays.asList(userWrite,userRead,userEdit,userDelete);
        List<Privilege> userPrivileges = Arrays.asList(userRead);

        Role adminRole = createRoleIfNotFound("ADMIN_ROLE",adminPrivileges);
        Role userRole = createRoleIfNotFound("USER_ROLE",userPrivileges);

        User admin = User.builder().
                firstName("Ivan").
                lastName("Seguljev").
                email("ivan.seguljev@yahoo.com").
                enabled(true).
                roles(Arrays.asList(adminRole)).
                password("123").
                build();

        User user = User.builder().
                firstName("User").
                lastName("LastName").
                email("user@gmail.com").
                enabled(true).
                roles(Arrays.asList(userRole)).
                password("123").
                build();

        userRepository.save(admin);
        userRepository.save(user);
        isSetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String privilegeName){
        logger.info("inserting privilege if not exist '{}'.", privilegeName);
        Optional<Privilege> privilegeOptional = privilegeRepository.findByName(privilegeName);
        if(privilegeOptional.isEmpty()){
            Privilege privilege = new Privilege(privilegeName);
            privilegeRepository.save(privilege);
            logger.info("privilege '{}' was successfully inserted.", privilege.getName());
            return privilege;
        }else{
            logger.info("privilege '{}' already exists.", privilegeName);
            return privilegeOptional.get();
        }

    }

    @Transactional
    Role createRoleIfNotFound(String roleName, Collection<Privilege> privileges){
        logger.info("inserting role if not exist '{}'.", roleName);
        Optional<Role> roleOptional = roleRepository.findByName(roleName);
        if(roleOptional.isEmpty()){
            Role role = new Role(roleName);
            role.setPrivileges(privileges);
            roleRepository.save(role);
            logger.info("role '{}' was successfully inserted.", role.getName());
            return role;
        } else {
            logger.info("role '{}' already exists.", roleName);
            return roleOptional.get();
        }
    }
}
