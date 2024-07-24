package com.example.CanchaManager.cancha.repository;

import com.example.CanchaManager.cancha.model.Cancha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CanchaRepository extends JpaRepository<Cancha, Long> {

    Boolean existsByIdAndEstado(Long id, String estado);

}
