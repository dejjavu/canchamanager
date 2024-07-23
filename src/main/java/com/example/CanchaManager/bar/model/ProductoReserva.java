package com.example.CanchaManager.bar.model;

import com.example.CanchaManager.cancha.model.Reserva;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class ProductoReserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Reserva reserva;

    @ManyToOne
    private Producto producto;

    private Integer cantidad;

    public void setReserva(Reserva reserva) {
    }

    public void setProducto(Producto producto) {
    }

    public void setCantidad(Integer cantidad) {
    }

    // getters y setters
}
