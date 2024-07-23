package com.example.CanchaManager.config;

import com.example.CanchaManager.security.ERole;
import com.example.CanchaManager.security.Role;
import com.example.CanchaManager.security.User;
import com.example.CanchaManager.security.RoleRepository;
import com.example.CanchaManager.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserInitializer implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Verificar si ya hay un usuario administrador
        if (userRepository.existsByUsername("administrador")) {
            return; // Si ya existe, no hacemos nada
        }

        // Crear rol ROLE_ADMIN si no existe
        Role roleAdmin = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseGet(() -> roleRepository.save(new Role(ERole.ROLE_ADMIN)));

        // Crear usuario administrador
        User adminUser = new User("administrador", "admin@example.com", passwordEncoder.encode("administrador"), "User", "Admin");
        adminUser.getRoles().add(roleAdmin); // Asignar rol ROLE_ADMIN al usuario
        adminUser.setConfirmed(true); // Marcar como confirmado (opcional)

        userRepository.save(adminUser);
    }
}
