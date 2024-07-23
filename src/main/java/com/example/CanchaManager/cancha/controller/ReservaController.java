package com.example.CanchaManager.cancha.controller;

import com.example.CanchaManager.Exception.ReservaException;
import com.example.CanchaManager.cancha.service.ReservaService;
import com.example.CanchaManager.cancha.model.Reserva;
import com.example.CanchaManager.cancha.model.ReservaDTO;
import com.example.CanchaManager.security.User;
import com.example.CanchaManager.security.UserRepository;
import com.example.CanchaManager.security.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<ReservaDTO>> getAllReservas() {
        try {
            List<ReservaDTO> reservas = reservaService.getAllReservasDTO();
            return new ResponseEntity<>(reservas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> getReservaById(@PathVariable Long id) {
        try {
            ReservaDTO reserva = reservaService.getReservaById(id);
            return new ResponseEntity<>(reserva, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> createReserva(@RequestBody Reserva reserva, HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromCookies(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            reserva.setUsuario(user);

            ReservaDTO reservaDTO = reservaService.createReserva(reserva);

            return new ResponseEntity<>(reservaDTO, HttpStatus.CREATED);
        } catch (ReservaException e) {
            return new ResponseEntity<>((e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Captura de excepciones generales
            return new ResponseEntity<>(("Error al procesar la reserva"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PutMapping("/{id}")
    public ResponseEntity<Reserva> updateReserva(@PathVariable Long id, @RequestBody Reserva reserva) {
        try {
            Reserva reservaActualizada = reservaService.updateReserva(id, reserva);
            if (reservaActualizada != null) {
                return new ResponseEntity<>(reservaActualizada, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
