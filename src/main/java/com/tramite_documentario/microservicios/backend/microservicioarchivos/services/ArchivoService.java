package com.tramite_documentario.microservicios.backend.microservicioarchivos.services;

import com.tramite_documentario.microservicio.backend.commonmicroservicios.services.CommonService;
import com.tramite_documentario.microservicios.backend.commonarchivos.models.entity.Archivo;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;

public interface ArchivoService extends CommonService<Archivo> {

    public List<Archivo> findAllByIdSolicitudIsNull();

    public List<Archivo> saveAll(List<Archivo> archivos);

    public List<Archivo> findAllByIdSolicitud(Long id);
    
    @Async
    public void sendFiles(List<String> emails, Resource documento, String nombreArchivo) throws MessagingException, UnsupportedEncodingException;
}
