package com.tramite_documentario.microservicios.backend.microservicioarchivos.services;

import com.tramite_documentario.microservicio.backend.commonmicroservicios.services.CommonService;
import com.tramite_documentario.microservicios.backend.commonarchivos.models.entity.Archivo;

import java.util.List;

public interface ArchivoService extends CommonService<Archivo> {

    public List<Archivo> findAllByIdSolicitudIsNull();

    public List<Archivo> saveAll(List<Archivo> archivos);
}
