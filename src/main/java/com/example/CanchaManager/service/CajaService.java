package com.example.CanchaManager.service;

import com.example.CanchaManager.model.*;
import com.example.CanchaManager.repository.CajaRepository;
import com.example.CanchaManager.repository.ReservaRepository;
import com.example.CanchaManager.repository.TransaccionRepository;
import com.example.CanchaManager.service.Exception.CajaException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class CajaService {

    private static final Logger log = LogManager.getLogger(CajaService.class);
    @Autowired
CajaRepository cajaRepository;
    @Autowired
TransaccionRepository transaccionRepository;
    @Autowired
ReservaRepository reservaRepository;




    @Transactional
    public Caja abrirCaja(User user, Double saldoInicial) {
        try {
            Optional<Caja> cajaAbiertaOptional = cajaRepository.findByUsuario(user);

            if (cajaAbiertaOptional.isPresent()) {
                log.info("Intento fallido de abrir una nueva caja para el usuario {} porque ya hay una caja abierta",
                        user.getUsername());
                throw new CajaException("Ya existe una caja abierta para este usuario");
            }

            Caja caja = new Caja();
            caja.setUsuario(user);
            caja.setSaldoInicial(saldoInicial);
            caja.setSaldoFinal(saldoInicial);
            caja.setFechaApertura(LocalDateTime.now());
            caja.setAbierta(true);

            Caja cajaGuardada = cajaRepository.save(caja);

            log.info("Se abrió una nueva caja para el usuario: {}", user.getUsername());

            return cajaGuardada;
        } catch (CajaException e) {
            log.error("Error al abrir caja para el usuario {}: {}", user.getUsername(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error general al abrir caja para el usuario {}: {}", user.getUsername(), e.getMessage());
            throw new RuntimeException("Error al abrir caja: " + e.getMessage());
        }
    }


    @Transactional
    public Caja cerrarCaja(User user) {
        try {
            Optional<Caja> cajaOptional = cajaRepository.findByUsuario(user);
            if (cajaOptional.isPresent()) {
                Caja caja = cajaOptional.get();
                caja.setAbierta(false);
                cajaRepository.save(caja);
                return caja;
            } else {
                throw new RuntimeException("No se encontró una caja abierta para el usuario con ID: " + user.getId());
            }
        } catch (RuntimeException e) {
            log.error("Error al cerrar caja para el usuario {}: {}", user.getUsername(), e.getMessage());
            throw new RuntimeException("Error al cerrar caja: " + e.getMessage());
        }
    }

    @Transactional
    public Transaccion registrarIngreso(Double monto, String descripcion, User user, tiposEnum tipoIngreso) {
        try {
            Optional<Caja> cajaOptional = cajaRepository.findByUsuario(user);

            if (cajaOptional.isPresent()) {
                Caja caja = cajaOptional.get();
                Transaccion transaccion = new Transaccion();
                transaccion.setMonto(monto);
                transaccion.setFecha(LocalDate.now());
                transaccion.setHora(LocalTime.now());
                transaccion.setTipo("INGRESO");
                transaccion.setMetodoIngreso(String.valueOf(tipoIngreso));
                transaccion.setDescripcion(descripcion);
                transaccion.setUsuario(user);
                caja.setSaldoFinal(caja.getSaldoFinal() + monto);
                cajaRepository.save(caja);
                transaccion = transaccionRepository.save(transaccion);
                return transaccion;
            } else {
                throw new RuntimeException("No se encontró una caja abierta para el usuario con ID: " + user.getId());
            }
        } catch (RuntimeException e) {
            log.error("Error al registrar ingreso para el usuario {}: {}", user.getUsername(), e.getMessage());
            throw new RuntimeException("Error al registrar el ingreso: " + e.getMessage());
        }
    }

    @Transactional
    public Transaccion registrarEgreso(Double monto, String descripcion, User user) {
        try {
            Optional<Caja> cajaOptional = cajaRepository.findByUsuario(user);
            if (cajaOptional.isPresent()) {
                Caja caja = cajaOptional.get();
                if (monto > caja.getSaldoFinal()) {
                    throw new RuntimeException("No hay suficientes fondos en la caja.");
                }
                Transaccion transaccion = new Transaccion();
                transaccion.setMonto(-monto); // Egreso, por lo tanto se resta
                transaccion.setFecha(LocalDate.now());
                transaccion.setHora(LocalTime.now());
                transaccion.setTipo("EGRESO");
                transaccion.setDescripcion(descripcion);
                transaccion.setUsuario(user);
                caja.setSaldoFinal(caja.getSaldoFinal() - monto);
                cajaRepository.save(caja);
                return transaccionRepository.save(transaccion);
            } else {
                throw new RuntimeException("No se encontró una caja abierta para el usuario con ID: " + user.getId());
            }
        } catch (RuntimeException e) {
            log.error("Error al registrar egreso para el usuario {}: {}", user.getUsername(), e.getMessage());
            throw new RuntimeException("Error al registrar el egreso: " + e.getMessage());
        }
    }

    @Transactional
    public Transaccion cobrarReserva(Long reservaId, String metodoIngreso, User user) {
        try {
            Optional<Reserva> reservaOptional = reservaRepository.findById(reservaId);
            if (reservaOptional.isPresent()) {
                Reserva reserva = reservaOptional.get();
                if (reserva.isPagado()) {
                    throw new RuntimeException("La reserva ya ha sido pagada anteriormente.");
                }

                Optional<Caja> cajaOptional = cajaRepository.findByUsuario(user);
                if (cajaOptional.isPresent()) {
                    Caja caja = cajaOptional.get();
                    String descripcion = "Pago Reserva Nº " + reservaId;
                    Transaccion transaccion = new Transaccion();
                    transaccion.setMonto(reserva.getMonto());
                    transaccion.setFecha(LocalDate.now());
                    transaccion.setHora(LocalTime.now());
                    transaccion.setUsuario(user);

                    if ("EFECTIVO".equals(metodoIngreso)) {
                        transaccion.setTipo("INGRESO");
                        transaccion.setDescripcion(descripcion);
                        caja.setSaldoFinal(caja.getSaldoFinal() + reserva.getMonto());
                    } else {
                        transaccion.setTipo("INGRESO"); // Ajustar el tipo según la lógica específica
                        transaccion.setDescripcion("Pago Reserva - " + reservaId);
                        transaccion.setMetodoIngreso(metodoIngreso);
                    }

                    transaccion = transaccionRepository.save(transaccion);

                    reserva.setPagado(true);
                    reservaRepository.save(reserva);

                    cajaRepository.save(caja);

                    return transaccion;
                } else {
                    throw new RuntimeException("No se encontró una caja abierta para el usuario con ID: " + user.getId());
                }
            } else {
                throw new RuntimeException("No se encontró la reserva con ID: " + reservaId);
            }
        } catch (RuntimeException e) {
            log.error("Error al cobrar reserva para el usuario {}: {}", user.getUsername(), e.getMessage());
            throw new RuntimeException("Error al cobrar la reserva: " + e.getMessage());
        }
    }
}
