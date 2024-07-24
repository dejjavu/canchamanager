package com.example.CanchaManager.caja.repository;

import com.example.CanchaManager.caja.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    List<Transaccion> findByFecha(LocalDate fecha);

}
