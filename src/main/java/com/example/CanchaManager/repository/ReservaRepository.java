package com.example.CanchaManager.repository;

import com.example.CanchaManager.model.Cancha;
import com.example.CanchaManager.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsByCanchaAndFechaReservaAndHoraInicio(Cancha cancha, LocalDate fechaReserva, LocalTime horaInicio);

}
