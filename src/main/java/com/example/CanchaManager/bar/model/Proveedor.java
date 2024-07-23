package com.example.CanchaManager.bar.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String contacto;

    @OneToMany(mappedBy = "proveedor")
    private List<Producto> productos;

    // getters y setters
}
