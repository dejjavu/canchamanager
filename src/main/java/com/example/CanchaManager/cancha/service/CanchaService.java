package com.example.CanchaManager.cancha.service;

import com.example.CanchaManager.cancha.model.Cancha;
import com.example.CanchaManager.cancha.repository.CanchaRepository;
import com.example.CanchaManager.cancha.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class CanchaService {

    private final CanchaRepository canchaRepository;
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    public CanchaService(CanchaRepository canchaRepository) {
        this.canchaRepository = canchaRepository;
    }


    public List<Cancha> getAllCanchas() {
        return canchaRepository.findAll();
    }

    public Optional<Cancha> getCanchaById(Long id) {
        return canchaRepository.findById(id);
    }

    public Cancha saveOrUpdateCancha(Cancha cancha) {
        return canchaRepository.save(cancha);
    }

    public void deleteCancha(Long id) {
        canchaRepository.deleteById(id);
    }

    public Cancha cambiarEstadoCancha(Long id) {
        Optional<Cancha> canchaOpt = canchaRepository.findById(id);
        if (canchaOpt.isPresent()) {
            Cancha cancha = canchaOpt.get();
            LocalDate fechaActual = LocalDate.now();
            LocalTime horaActual = LocalTime.now();

            if ("HABILITADA".equals(cancha.getEstado())) {
                cancha.setEstado("DESHABILITADA");
                canchaRepository.save(cancha);

                // Deshabilitar reservas futuras
                reservaRepository.deshabilitarReservasFuturas(cancha.getId(), fechaActual, horaActual);

                return cancha;
            } else if ("DESHABILITADA".equals(cancha.getEstado())) {
                cancha.setEstado("HABILITADA");
                canchaRepository.save(cancha);

                // Habilitar reservas futuras
                reservaRepository.habilitarReservasFuturas(cancha.getId(), fechaActual, horaActual);

                return cancha;
            }
        }
        throw new RuntimeException("Cancha no encontrada");
    }
}

