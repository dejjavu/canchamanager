package com.example.CanchaManager.config;

import com.example.CanchaManager.model.ERole;
import com.example.CanchaManager.model.Role;
import com.example.CanchaManager.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeRoles();
    }

    private void initializeRoles() {
        createRoleIfNotFound(ERole.ROLE_ADMIN);
        createRoleIfNotFound(ERole.ROLE_USER);
    }

    private void createRoleIfNotFound(ERole roleEnum) {
        Role role = roleRepository.findByName(roleEnum)
                .orElse(null);

        if (role == null) {
            role = new Role(roleEnum);
            roleRepository.save(role);
        }
    }
}
