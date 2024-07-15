package com.example.CanchaManager.service;

import com.example.CanchaManager.model.Cancha;
import com.example.CanchaManager.repository.CanchaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CanchaService {

    private final CanchaRepository canchaRepository;

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
        Optional<Cancha> optionalCancha = canchaRepository.findById(id);
        if (optionalCancha.isPresent()) {
            Cancha cancha = optionalCancha.get();
            // Cambiar el estado opuesto
            boolean nuevoEstado = !cancha.isEstado(); // Cambiar entre true y false

            cancha.setEstado(nuevoEstado);
            return canchaRepository.save(cancha);
        } else {
            throw new RuntimeException("No se encontró la cancha con ID: " + id);
        }
    }
}

