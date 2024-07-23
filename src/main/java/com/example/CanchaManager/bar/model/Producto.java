package com.example.CanchaManager.bar.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String modelo;
    private Double precioCompra;
    private Double precioVenta;

    @ManyToOne
    private Proveedor proveedor;

    @OneToMany(mappedBy = "producto")
    private List<ProductoMesa> mesas;

    // getters y setters
}
