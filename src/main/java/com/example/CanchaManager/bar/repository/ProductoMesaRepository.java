package com.example.CanchaManager.bar.repository;

import com.example.CanchaManager.bar.model.ProductoMesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoMesaRepository extends JpaRepository<ProductoMesa, Long> {}
