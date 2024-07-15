package com.example.CanchaManager.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservaDTO {


    private Long idReserva;
    private Long canchaId;
    private String nombreCliente;
    private String apellidoCliente;
    private String numeroCliente;
    private LocalDate fechaReserva;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private boolean pagado;
    private boolean deshabilitada;
    private Double monto;
    private final String usuario;

    // Constructor que convierte una Reserva en un ReservaDTO
    public ReservaDTO(Reserva reserva) {
        this.idReserva = reserva.getId();
        this.canchaId = reserva.getCancha().getId();
        this.nombreCliente = reserva.getNombreCliente();
        this.apellidoCliente = reserva.getApellidoCliente();
        this.numeroCliente = reserva.getNumeroCliente();
        this.fechaReserva = reserva.getFechaReserva();
        this.horaInicio = reserva.getHoraInicio();
        this.horaFin = reserva.getHoraFin();
        this.pagado = reserva.isPagado();
        this.monto = reserva.getMonto();
        this.usuario = reserva.getUsuario().getUsername();
    }
}
