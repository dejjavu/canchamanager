package com.example.CanchaManager.caja.repository;

import com.example.CanchaManager.caja.model.Precio;
import com.example.CanchaManager.config.tiposEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrecioRepository extends JpaRepository<Precio, Long> {

    Optional<Precio> findByTipoPrecio(tiposEnum tiposEnum);
}
