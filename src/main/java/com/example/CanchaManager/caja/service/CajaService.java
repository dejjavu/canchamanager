package com.example.CanchaManager.caja.service;

import com.example.CanchaManager.bar.model.Mesa;
import com.example.CanchaManager.bar.repository.MesaRepository;
import com.example.CanchaManager.caja.model.Caja;
import com.example.CanchaManager.caja.model.Transaccion;
import com.example.CanchaManager.caja.repository.CajaRepository;
import com.example.CanchaManager.caja.repository.TransaccionRepository;
import com.example.CanchaManager.cancha.model.Reserva;
import com.example.CanchaManager.config.tiposEnum;
import com.example.CanchaManager.cancha.repository.ReservaRepository;
import com.example.CanchaManager.security.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    @Autowired
    MesaRepository mesaRepository;




    @Transactional
    public Caja abrirCaja(User user, Double saldoInicial) {
        Optional<Caja> cajaOptional = cajaRepository.findByUsuarioAndAbiertaTrue(user);
        if (cajaOptional.isPresent()) {
            throw new RuntimeException("Ya existe una caja abierta para este usuario.");
        }

        Caja caja = new Caja();
        caja.setUsuario(user);
        caja.setAbierta(true);
        caja.setFechaApertura(LocalDate.now().atStartOfDay());
        caja.setSaldoInicial(saldoInicial);
        caja.setSaldoFinal(saldoInicial);
        return cajaRepository.save(caja);
    }

    @Transactional
    public Caja cerrarCaja(User user) {
        Optional<Caja> cajaOptional = cajaRepository.findByUsuarioAndAbiertaTrue(user);
        if (cajaOptional.isEmpty()) {
            throw new RuntimeException("No se encontró una caja abierta para el usuario con ID: " + user.getId());
        }

        Caja caja = cajaOptional.get();
        caja.setAbierta(false);
        caja.setFechaCierre(LocalDate.now().atStartOfDay());
        return cajaRepository.save(caja);
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
    public void cobrarReserva(Long reservaId, String metodoIngreso, User user) {
        try {
            Optional<Reserva> reservaOptional = reservaRepository.findById(reservaId);
            if (reservaOptional.isPresent()) {
                Reserva reserva = reservaOptional.get();
                if (reserva.isPagado()) {
                    throw new RuntimeException("La reserva ya ha sido pagada anteriormente.");
                }

                Transaccion transaccion = new Transaccion();
                transaccion.setMonto(reserva.getMonto());
                transaccion.setFecha(LocalDate.now());
                transaccion.setHora(LocalTime.now());
                transaccion.setUsuario(user);
                transaccion.setReserva(reserva);
                transaccion.setMetodoIngreso(metodoIngreso);

                if ("EFECTIVO".equals(metodoIngreso)) {
                    // Actualizar el saldo en la caja
                    Caja caja = cajaRepository.findByUsuarioAndAbiertaTrue(user)
                            .orElseThrow(() -> new RuntimeException("No hay caja abierta para el usuario."));
                    caja.setSaldoFinal(caja.getSaldoFinal() + reserva.getMonto()); // Asegúrate de que reserva.getMonto() retorne un double
                    cajaRepository.save(caja);

                }
                transaccion.setTipo("INGRESO");
                transaccion.setDescripcion("Pago Reserva Nº " + reservaId);

                transaccionRepository.save(transaccion);

                reserva.setPagado(true);
                reservaRepository.save(reserva);

            } else {
                throw new RuntimeException("No se encontró la reserva con ID: " + reservaId);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al cobrar la reserva: " + e.getMessage());
        }
    }

    @Transactional
    public void cobrarMesa(Long mesaId, Double monto, String metodoIngreso, User user) {
        try {
            Optional<Mesa> mesaOptional = mesaRepository.findById(mesaId);
            if (mesaOptional.isPresent()) {
                Mesa mesa = mesaOptional.get();

                Transaccion transaccion = new Transaccion();
                transaccion.setMonto(monto);
                transaccion.setFecha(LocalDate.now());
                transaccion.setHora(LocalTime.now());
                transaccion.setUsuario(user);
                transaccion.setMesa(mesa);
                transaccion.setMetodoIngreso(metodoIngreso); // Asignar el método de ingreso

                if ("EFECTIVO".equals(metodoIngreso)) {
                    transaccion.setTipo("INGRESO");
                    transaccion.setDescripcion("Pago Mesa Nº " + mesaId);
                } else {
                    transaccion.setTipo("INGRESO");
                    transaccion.setDescripcion("Pago Mesa - " + mesaId);
                }

                transaccion = transaccionRepository.save(transaccion);

            } else {
                throw new RuntimeException("No se encontró la mesa con ID: " + mesaId);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al cobrar la mesa: " + e.getMessage());
        }
    }


}
