package com.tramite_documentario.microservicios.backend.microservicioarchivos.controllers;

import com.tramite_documentario.microservicio.backend.commonmicroservicios.controllers.CommonController;
import com.tramite_documentario.microservicio.backend.commonmicroservicios.services.CommonService;
import com.tramite_documentario.microservicios.backend.commonarchivos.models.entity.Archivo;
import com.tramite_documentario.microservicios.backend.commonarchivos.models.entity.Solicitud;
import com.tramite_documentario.microservicios.backend.microservicioarchivos.clients.SolicitudFeignClient;
import com.tramite_documentario.microservicios.backend.microservicioarchivos.services.ArchivoService;
import com.tramite_documentario.microservicios.backend.microservicioarchivos.services.TipoArchivoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class ArchivoController extends CommonController<Archivo, ArchivoService> {

    @Autowired
    private TipoArchivoServiceImpl tipoArchivoService;

    @Autowired
    private SolicitudFeignClient solicitudFeignClient;

    @PostMapping("/crear-con-file")
    public ResponseEntity<?> crearConFile(@Valid Archivo archivo, BindingResult result, @RequestParam MultipartFile documento) throws IOException {
        if (!documento.isEmpty()) {
            archivo.setFile(documento.getBytes());
        }
        return this.guardar(archivo, result);
    }


    @PostMapping
    @Override
    public ResponseEntity<?> guardar(@Valid @RequestBody Archivo entity, BindingResult result) {

        if (result.hasErrors()) {
            return validar(result);
        }
        if (entity.getIdSolicitud() != null) {
            Solicitud solicitud = solicitudFeignClient.ver(entity.getIdSolicitud());
            if (solicitud == null) {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(entity));
    }

    @PutMapping("/editar-con-file/{id}")
    public ResponseEntity<?> editarConFile(@Valid @RequestBody Archivo archivo, BindingResult result, @PathVariable Long id, @RequestParam MultipartFile documento) throws IOException {
        if (result.hasErrors()) {
            return this.validar(result);
        }

        Optional<Archivo> a = service.findById(id);

        if (a.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Archivo archivoDb = a.get();
        archivoDb.setDescripcion(archivo.getDescripcion());
        if (archivo.getIdSolicitud() != null) {
            archivoDb.setIdSolicitud(archivo.getIdSolicitud());
        }
        if (!documento.isEmpty()) {
            archivoDb.setFile(documento.getBytes());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(archivoDb));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Archivo archivo, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            this.validar(result);
        }
        Optional<Archivo> a = this.service.findById(id);
        if (a.isPresent()) {
            Archivo archivoDb = a.get();
            archivoDb.setDescripcion(archivo.getDescripcion());
            if (archivo.getIdSolicitud() != null) {
                archivoDb.setIdSolicitud(archivo.getIdSolicitud());
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(archivoDb));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tipoArchivos")
    public ResponseEntity<?> listarTipoArchivos() {
        return ResponseEntity.ok().body(tipoArchivoService.findAll());
    }


    @GetMapping("/archivos-sin-solicitud")
    public ResponseEntity<?> listarArchivosSinSolicitud(){
        return ResponseEntity.ok(this.service.findAllByIdSolicitudIsNull());
    }

    @PostMapping("/guardar-todos")
    public ResponseEntity<?> guardarTodo(@RequestBody List<Archivo> archivos){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.saveAll(archivos));
    }
}
