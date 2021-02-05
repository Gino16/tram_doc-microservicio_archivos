package com.tramite_documentario.microservicios.backend.microservicioarchivos.models.repository;

import com.tramite_documentario.microservicios.backend.commonarchivos.models.entity.Archivo;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ArchivoRepository extends PagingAndSortingRepository<Archivo, Long> {

    public List<Archivo> findAllByIdSolicitudIsNull();

    public List<Archivo> findAllByIdSolicitud(Long id);
}
