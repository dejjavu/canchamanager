package com.example.CanchaManager.repository;

import com.example.CanchaManager.model.Caja;
import com.example.CanchaManager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CajaRepository extends JpaRepository<Caja, Long> {
    Optional<Caja> findByUsuario(User usuario);

    Optional<Caja> findByUsuarioAndAbierta(User usuario, boolean abierta);


}

