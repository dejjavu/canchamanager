package com.example.CanchaManager.bar.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;

    @OneToMany(mappedBy = "mesa")
    private List<ProductoMesa> productos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public List<ProductoMesa> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoMesa> productos) {
        this.productos = productos;
    }


// getters y setters

}
