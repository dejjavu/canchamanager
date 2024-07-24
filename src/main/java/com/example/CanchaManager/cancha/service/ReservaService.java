package com.example.CanchaManager.cancha.service;

import com.example.CanchaManager.caja.model.Precio;
import com.example.CanchaManager.apis.HolidayService;
import com.example.CanchaManager.cancha.model.Cancha;
import com.example.CanchaManager.cancha.model.Reserva;
import com.example.CanchaManager.cancha.model.ReservaDTO;
import com.example.CanchaManager.config.tiposEnum;
import com.example.CanchaManager.cancha.repository.CanchaRepository;
import com.example.CanchaManager.caja.repository.PrecioRepository;
import com.example.CanchaManager.cancha.repository.ReservaRepository;
import com.example.CanchaManager.Exception.ReservaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private static final Logger logger = LoggerFactory.getLogger(ReservaService.class);

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private CanchaRepository canchaRepository;

    @Autowired
    private PrecioRepository precioRepository;

    @Autowired
    private HolidayService holidayService;

    public List<ReservaDTO> getAllReservasDTO() {
        try {
            logger.info("Obteniendo todas las reservas.");
            List<Reserva> reservas = reservaRepository.findAll();
            return reservas.stream().map(ReservaDTO::new).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al obtener todas las reservas.", e);
            throw e; // Re-lanza la excepción para que sea manejada en el controlador
        }
    }

    public ReservaDTO getReservaById(Long id) throws Exception {
        try {
            logger.info("Obteniendo reserva con ID: {}", id);
            Optional<Reserva> reservaOptional = reservaRepository.findById(id);
            if (reservaOptional.isPresent()) {
                return new ReservaDTO(reservaOptional.get());
            } else {
                logger.warn("Reserva no encontrada con ID: {}", id);
                throw new Exception("Reserva no encontrada con ID: " + id);
            }
        } catch (Exception e) {
            logger.error("Error al obtener la reserva con ID: " + id, e);
            throw e;
        }
    }

    public ReservaDTO createReserva(Reserva reserva) {
        try {
            logger.info("Creando nueva reserva: {}", reserva);
            Cancha cancha = reserva.getCancha();

            // Verificar si la cancha está habilitada
            Cancha canchaExistente = canchaRepository.findById(cancha.getId())
                    .orElseThrow(() -> new ReservaException("Cancha no encontrada."));

            if ("DESHABILITADA".equals(canchaExistente.getEstado())) {
                logger.warn("Cancha deshabilitada");
                throw new ReservaException("La cancha está deshabilitada, no se puede hacer la reserva.");
            }

            LocalDate fechaReserva = reserva.getFechaReserva();
            LocalTime horaInicio = reserva.getHoraInicio();

            // Verificar si la fecha es un feriado
            if (holidayService.isHoliday(fechaReserva)) {
                logger.warn("La fecha de la reserva es un feriado: {}", fechaReserva);
                throw new ReservaException("No se pueden hacer reservas en días feriados.");
            }

            // Verificar si ya existe una reserva para la misma cancha, fecha y hora de inicio
            if (reservaRepository.existsByCanchaAndFechaReservaAndHoraInicio(cancha, fechaReserva, horaInicio)) {
                logger.warn("Ya existe una reserva para la cancha: {} en la fecha: {} y hora: {}", cancha, fechaReserva, horaInicio);
                throw new ReservaException("La cancha ya tiene una reserva para el día y horario registrado.");
            }

            // Determinar el tipo de precio según la hora de inicio y obtener el precio correspondiente
            tiposEnum tipoPrecio = determinarTipoPrecio(horaInicio);
            Optional<Precio> precioOptional = precioRepository.findByTipoPrecio(tipoPrecio);
            if (precioOptional.isPresent()) {
                reserva.setMonto(precioOptional.get().getMonto());
            } else {
                logger.warn("No se encontró un precio válido para el tipo de reserva: {}", tipoPrecio);
                throw new ReservaException("No se encontró un precio válido para el tipo de reserva.");
            }

            // Guardar la reserva en la base de datos
            Reserva savedReserva = reservaRepository.save(reserva);
            logger.info("Reserva creada con éxito: {}", savedReserva);
            return new ReservaDTO(savedReserva);
        } catch (Exception e) {
            logger.error("Error al crear la reserva.", e);
            throw e;
        }
    }

    public Reserva updateReserva(Long id, Reserva reserva) {
        try {
            logger.info("Actualizando reserva con ID: {}", id);
            if (reservaRepository.existsById(id)) {
                reserva.setId(id);
                Reserva updatedReserva = reservaRepository.save(reserva);
                logger.info("Reserva actualizada con éxito: {}", updatedReserva);
                return updatedReserva;
            } else {
                logger.warn("Reserva con ID: {} no encontrada para actualizar.", id);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error al actualizar la reserva con ID: " + id, e);
            throw e;
        }
    }

    public boolean deleteReserva(Long id) {
        try {
            logger.info("Eliminando reserva con ID: {}", id);
            reservaRepository.deleteById(id);
            logger.info("Reserva con ID: {} eliminada con éxito.", id);
            return true;
        } catch (Exception e) {
            logger.error("Error al eliminar la reserva con ID: " + id, e);
            throw e;
        }
    }

    private tiposEnum determinarTipoPrecio(LocalTime horaInicio) {
        if (horaInicio.isAfter(LocalTime.of(4, 0)) && horaInicio.isBefore(LocalTime.of(17, 0))) {
            return tiposEnum.DIA;
        } else {
            return tiposEnum.NOCHE;
        }
    }

    @Transactional
    public List<Reserva> saveAll(List<Reserva> reservas) {
        try {
            logger.info("Guardando todas las reservas: {}", reservas);
            List<Reserva> savedReservas = reservaRepository.saveAll(reservas);
            logger.info("Reservas guardadas con éxito.");
            return savedReservas;
        } catch (Exception e) {
            logger.error("Error al guardar todas las reservas.", e);
            throw e;
        }
    }
}
