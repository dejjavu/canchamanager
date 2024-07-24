package com.example.CanchaManager.cancha.repository;

import com.example.CanchaManager.cancha.model.Cancha;
import com.example.CanchaManager.cancha.model.Reserva;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;


public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Reserva r SET r.deshabilitada = true WHERE r.cancha.id = :canchaId AND r.fechaReserva >= :fecha AND (r.fechaReserva > :fecha OR r.horaInicio >= :hora)")
    void deshabilitarReservasFuturas(Long canchaId, LocalDate fecha, LocalTime hora);

    @Modifying
    @Transactional
    @Query("UPDATE Reserva r SET r.deshabilitada = false WHERE r.cancha.id = :canchaId AND r.fechaReserva >= :fecha AND (r.fechaReserva > :fecha OR r.horaInicio >= :hora)")
    void habilitarReservasFuturas(Long canchaId, LocalDate fecha, LocalTime hora);

    boolean existsByCanchaAndFechaReservaAndHoraInicio(Cancha cancha, LocalDate fechaReserva, LocalTime horaInicio);

}
