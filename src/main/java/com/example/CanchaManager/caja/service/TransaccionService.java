package com.example.CanchaManager.caja.service;

import com.example.CanchaManager.caja.model.Transaccion;
import com.example.CanchaManager.caja.model.TransaccionDTO;
import com.example.CanchaManager.caja.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    public List<TransaccionDTO> getAllTransacciones() {
        List<Transaccion> transacciones = transaccionRepository.findAll();
        return transacciones.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<TransaccionDTO> getTransaccionesByFecha(LocalDate fecha) {
        List<Transaccion> transacciones = transaccionRepository.findByFecha(fecha);
        return transacciones.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private TransaccionDTO convertToDTO(Transaccion transaccion) {
        TransaccionDTO dto = new TransaccionDTO();
        dto.setId(transaccion.getId());
        dto.setMonto(transaccion.getMonto());
        dto.setFecha(transaccion.getFecha());
        dto.setHora(transaccion.getHora());
        dto.setTipo(transaccion.getTipo());
        dto.setDescripcion(transaccion.getDescripcion());
        dto.setMetodoIngreso(transaccion.getMetodoIngreso());
        dto.setUsuarioId(transaccion.getUsuario().getId());
        if (transaccion.getReserva() != null) {
            dto.setReservaId(transaccion.getReserva().getId());
        }
        if (transaccion.getMesa() != null) {
            dto.setMesaId(transaccion.getMesa().getId());
        }
        return dto;
    }
}