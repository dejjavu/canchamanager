package com.example.CanchaManager.repository;

import com.example.CanchaManager.model.Cancha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CanchaRepository extends JpaRepository<Cancha, Long> {


}
