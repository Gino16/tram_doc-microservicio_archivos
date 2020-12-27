package com.tramite_documentario.microservicios.backend.microservicioarchivos.controllers;

import com.tramite_documentario.microservicio.backend.commonmicroservicios.controllers.CommonController;
import com.tramite_documentario.microservicio.backend.commonmicroservicios.services.CommonService;
import com.tramite_documentario.microservicios.backend.microservicioarchivos.models.Archivo;
import com.tramite_documentario.microservicios.backend.microservicioarchivos.services.TipoArchivoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class ArchivoController extends CommonController<Archivo, CommonService<Archivo>> {

    @Autowired
    private TipoArchivoServiceImpl tipoArchivoService;

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Archivo archivo, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            this.validar(result);
        }
        Optional<Archivo> a = this.service.findById(id);
        if (a.isPresent()){
            Archivo archivoDb = a.get();
            archivoDb.setDescripcion(archivo.getDescripcion());
            archivoDb.setUrl(archivo.getUrl());
            return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(archivoDb));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tipoArchivos")
    public ResponseEntity<?> listarTipoArchivos() {
        return ResponseEntity.ok().body(tipoArchivoService.findAll());
    }

}
