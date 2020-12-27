package com.tramite_documentario.microservicios.backend.microservicioarchivos.services;

import com.tramite_documentario.microservicio.backend.commonmicroservicios.services.CommonServiceImpl;
import com.tramite_documentario.microservicios.backend.microservicioarchivos.models.TipoArchivo;
import com.tramite_documentario.microservicios.backend.microservicioarchivos.models.repository.TipoArchivoRepository;
import org.springframework.stereotype.Service;

@Service
public class TipoArchivoServiceImpl extends CommonServiceImpl<TipoArchivo, TipoArchivoRepository> {
}
