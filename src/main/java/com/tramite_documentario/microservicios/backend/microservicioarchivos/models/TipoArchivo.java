package com.tramite_documentario.microservicios.backend.microservicioarchivos.models;

import javax.persistence.*;

@Entity
@Table(name = "tipo_archivos")
public class TipoArchivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_archivo")
    private Long id;

    private String nombre;

    public Long getId() {
        return id;
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
}
