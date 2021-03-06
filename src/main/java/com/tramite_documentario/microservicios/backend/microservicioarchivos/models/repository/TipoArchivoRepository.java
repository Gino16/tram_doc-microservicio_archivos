package com.tramite_documentario.microservicios.backend.microservicioarchivos.models.repository;

import com.tramite_documentario.microservicios.backend.commonarchivos.models.entity.TipoArchivo;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TipoArchivoRepository extends PagingAndSortingRepository<TipoArchivo, Long> {
}
