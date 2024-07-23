package com.example.CanchaManager.cancha.service;

import com.example.CanchaManager.caja.model.Precio;
import com.example.CanchaManager.config.tiposEnum;
import com.example.CanchaManager.caja.repository.PrecioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrecioService {

    @Autowired
    private PrecioRepository precioRepository;

    public Precio getPrecioById(Long id) {
        return precioRepository.findById(id).orElse(null);
    }

    public Precio getPrecioByTipo(tiposEnum tiposEnum) {
        return precioRepository.findByTipoPrecio(tiposEnum).orElse(null);
    }

    public Precio savePrecio(Precio precio) {
        return precioRepository.save(precio);
    }

    public void deletePrecio(Long id) {
        precioRepository.deleteById(id);
    }

    public Double obtenerPrecioPorTipo(tiposEnum tiposEnum) {
        Optional<Precio> precio = precioRepository.findByTipoPrecio(tiposEnum);
        return precio != null ? precio.get().getMonto() : null;
    }


}
