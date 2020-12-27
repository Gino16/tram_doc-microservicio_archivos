package com.tramite_documentario.microservicios.backend.microservicioarchivos.models.repository;

import com.tramite_documentario.microservicios.backend.microservicioarchivos.models.Archivo;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ArchivoRepository extends PagingAndSortingRepository<Archivo, Long> {
}
