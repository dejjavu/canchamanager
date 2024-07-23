package com.example.CanchaManager.cancha.controller;

import com.example.CanchaManager.cancha.service.CanchaService;
import com.example.CanchaManager.cancha.model.Cancha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/canchas")
public class CanchaController {

    @Autowired
    private CanchaService canchaService;

    @GetMapping
    public List<Cancha> getAllCanchas() {
        return canchaService.getAllCanchas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cancha> getCanchaById(@PathVariable("id") Long id) {
        Optional<Cancha> cancha = canchaService.getCanchaById(id);
        return cancha.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createOrUpdateCancha(@RequestBody Cancha cancha) {
        Cancha updated = canchaService.saveOrUpdateCancha(cancha);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cancha creada o actualizada con éxito");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCancha(@PathVariable("id") Long id) {
        canchaService.deleteCancha(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<String> toggleEstadoCancha(@PathVariable Long id) {
        Cancha cancha = canchaService.cambiarEstadoCancha(id);

        // Puedes devolver algún mensaje o información adicional si lo necesitas
        String mensaje = "Estado de la cancha cambiado exitosamente a: " + (cancha.isEstado() ? "HABILITADA" : "DESHABILITADA");

        return ResponseEntity.ok(mensaje);
    }



}
