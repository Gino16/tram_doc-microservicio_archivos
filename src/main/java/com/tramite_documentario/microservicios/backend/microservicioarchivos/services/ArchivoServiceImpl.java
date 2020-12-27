package com.tramite_documentario.microservicios.backend.microservicioarchivos.services;

import com.tramite_documentario.microservicio.backend.commonmicroservicios.services.CommonServiceImpl;
import com.tramite_documentario.microservicios.backend.microservicioarchivos.models.Archivo;
import com.tramite_documentario.microservicios.backend.microservicioarchivos.models.repository.ArchivoRepository;
import org.springframework.stereotype.Service;

@Service
public class ArchivoServiceImpl extends CommonServiceImpl<Archivo, ArchivoRepository> {
}
