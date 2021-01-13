package com.tramite_documentario.microservicios.backend.microservicioarchivos.clients;

import com.tramite_documentario.microservicios.backend.commonarchivos.models.entity.Solicitud;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservicio-solicitudes")
public interface SolicitudFeignClient {

    @GetMapping("/{id}")
    public Solicitud ver(@PathVariable Long id);
}
