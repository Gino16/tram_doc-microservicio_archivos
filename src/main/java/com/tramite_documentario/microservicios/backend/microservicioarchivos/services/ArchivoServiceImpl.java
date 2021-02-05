package com.tramite_documentario.microservicios.backend.microservicioarchivos.services;

import com.tramite_documentario.microservicio.backend.commonmicroservicios.services.CommonServiceImpl;
import com.tramite_documentario.microservicios.backend.commonarchivos.models.entity.Archivo;
import com.tramite_documentario.microservicios.backend.microservicioarchivos.models.repository.ArchivoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArchivoServiceImpl extends CommonServiceImpl<Archivo, ArchivoRepository> implements ArchivoService{
    @Override
    public List<Archivo> findAllByIdSolicitudIsNull() {
        return this.repository.findAllByIdSolicitudIsNull();
    }

    @Override
    public List<Archivo> saveAll(List<Archivo> archivos) {
        return (List<Archivo>) this.repository.saveAll(archivos);
    }

    @Override
    public List<Archivo> findAllByIdSolicitud(Long id) {
        return this.repository.findAllByIdSolicitud(id);
    }
}
