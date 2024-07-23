package com.example.CanchaManager.cancha.repository;

import com.example.CanchaManager.cancha.model.Cancha;
import com.example.CanchaManager.cancha.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;


public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsByCanchaAndFechaReservaAndHoraInicio(Cancha cancha, LocalDate fechaReserva, LocalTime horaInicio);

}
