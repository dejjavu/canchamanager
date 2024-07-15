package com.example.CanchaManager.controller;

import com.example.CanchaManager.model.*;
import com.example.CanchaManager.repository.UserRepository;
import com.example.CanchaManager.security.JwtUtils;
import com.example.CanchaManager.service.CajaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/caja")
public class CajaController {

    @Autowired
    private CajaService cajaService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/abrir")
    public ResponseEntity<ApiResponse> abrirCaja(@RequestParam Double saldoInicial, HttpServletRequest request) {
        User user = getUserFromRequest(request);
        try {
            Caja caja = cajaService.abrirCaja(user, saldoInicial);

            // Construir la respuesta de éxito
            ApiResponse response = new ApiResponse(true, "Caja abierta exitosamente");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            // Capturar la excepción y construir la respuesta de error
            ApiResponse response = new ApiResponse(false, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/cerrar/{cajaId}")
    public ResponseEntity<ApiResponse> cerrarCaja(HttpServletRequest request) {
        User user = getUserFromRequest(request);
        try {
            Caja caja = cajaService.cerrarCaja(user);

            // Construir la respuesta de éxito
            ApiResponse response = new ApiResponse(true, "Caja cerrada exitosamente");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            // Capturar la excepción y construir la respuesta de error
            ApiResponse response = new ApiResponse(false, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/ingreso")
    public ResponseEntity<ApiResponse> registrarIngreso(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        // Obtener el usuario autenticado
        User user = getUserFromRequest(request);

        // Obtener los datos del cuerpo de la solicitud
        Double monto = Double.valueOf(body.get("monto").toString());
        String descripcion = body.get("descripcion").toString();
        String tipoIngresoStr = body.get("tipoIngreso").toString(); // Obtener el tipo de ingreso como String

        // Convertir el tipo de ingreso de String a tiposEnum
        tiposEnum tipoIngreso = tiposEnum.valueOf(tipoIngresoStr);

        try {
            // Registrar el ingreso usando el servicio
            Transaccion transaccion = cajaService.registrarIngreso(monto, descripcion, user, tipoIngreso);

            // Construir la respuesta de éxito
            ApiResponse response = new ApiResponse(true, "Ingreso registrado correctamente");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            // Capturar la excepción y construir la respuesta de error
            ApiResponse response = new ApiResponse(false, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/egreso")
    public ResponseEntity<ApiResponse> registrarEgreso(@RequestParam Double monto, @RequestParam String descripcion, HttpServletRequest request) {
        User user = getUserFromRequest(request);
        try {
            Transaccion transaccion = cajaService.registrarEgreso(monto, descripcion, user);
            ApiResponse response = new ApiResponse(true, "Egreso registrado correctamente");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            ApiResponse response = new ApiResponse(false, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/cobrar-reserva")
    public ResponseEntity<String> cobrarReserva(@RequestParam Long reservaId,
                                                @RequestParam String metodoIngreso,
                                                HttpServletRequest request) {
        // Obtener el usuario autenticado desde el request
        User user = getUserFromRequest(request);

        // Llamar al servicio para cobrar la reserva
        try {
            cajaService.cobrarReserva(reservaId, metodoIngreso, user);
            // Si el servicio no lanza excepción, significa que el pago fue exitoso
            return ResponseEntity.status(HttpStatus.CREATED).body("Pago exitoso para la Reserva Nº " + reservaId);
        } catch (RuntimeException e) {
            // Capturar la excepción lanzada por el servicio
            String mensajeError = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensajeError);
        }
    }

    // Método para obtener el usuario autenticado desde el request
    private User getUserFromRequest(HttpServletRequest request) {
        // Implementación específica para obtener el usuario desde el JWT
        String jwt = jwtUtils.getJwtFromCookies(request); // Obtener el JWT de las cookies
        String username = jwtUtils.getUserNameFromJwtToken(jwt); // Obtener el nombre de usuario del JWT

        // Buscar al usuario en la base de datos por nombre de usuario
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
