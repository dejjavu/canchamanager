package com.example.CanchaManager.bar.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class ProductoMesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Mesa mesa;
    @ManyToOne
    private Producto producto;
    private Integer cantidad;

    public void setMesa(Mesa mesa) {
    }

    public void setProducto(Producto producto) {
    }

    public void setCantidad(Integer cantidad) {
    }

    // getters y setters
}
