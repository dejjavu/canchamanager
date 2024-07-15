package com.example.CanchaManager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cancha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private boolean estado;

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @OneToMany(mappedBy = "cancha", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Reserva> reservas;

    // Getters y setters omitidos para brevedad

    public Long getId() {
        return id;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }


}
