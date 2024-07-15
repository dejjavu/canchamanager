package com.example.CanchaManager.model;

import jakarta.persistence.*;

@Entity
public class Precio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_precio")
    private tiposEnum tipoPrecio;

    private Double monto;

    // Constructor vacío
    public Precio() {
    }

    // Constructor con parámetros
    public Precio(tiposEnum tiposEnum, Double monto) {
        this.tipoPrecio = tiposEnum;
        this.monto = monto;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public tiposEnum getTipoPrecio() {
        return tipoPrecio;
    }

    public void setTipoPrecio(tiposEnum tiposEnum) {
        this.tipoPrecio = tiposEnum;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }
}
