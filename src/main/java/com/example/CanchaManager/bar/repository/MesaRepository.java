package com.example.CanchaManager.bar.repository;

import com.example.CanchaManager.bar.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {



}
