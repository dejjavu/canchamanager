package com.example.CanchaManager.caja.controller;

import com.example.CanchaManager.caja.model.TransaccionDTO;
import com.example.CanchaManager.caja.service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;

    @GetMapping
    public ResponseEntity<List<TransaccionDTO>> getAllTransacciones() {
        List<TransaccionDTO> transacciones = transaccionService.getAllTransacciones();
        return ResponseEntity.ok(transacciones);
    }

    @GetMapping("/")
    public ResponseEntity<List<TransaccionDTO>> getTransaccionesByFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<TransaccionDTO> transacciones = transaccionService.getTransaccionesByFecha(fecha);
        return ResponseEntity.ok(transacciones);
    }
}
