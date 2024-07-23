package com.example.CanchaManager.caja.repository;

import com.example.CanchaManager.caja.model.Caja;
import com.example.CanchaManager.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CajaRepository extends JpaRepository<Caja, Long> {
    Optional<Caja> findByUsuario(User usuario);

    Optional<Caja> findByUsuarioAndAbiertaTrue(User usuario);



}

