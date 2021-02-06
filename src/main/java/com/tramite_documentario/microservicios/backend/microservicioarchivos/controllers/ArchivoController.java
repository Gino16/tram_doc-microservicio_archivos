package com.tramite_documentario.microservicios.backend.microservicioarchivos.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tramite_documentario.microservicio.backend.commonmicroservicios.controllers.CommonController;
import com.tramite_documentario.microservicios.backend.commonarchivos.models.entity.Archivo;
import com.tramite_documentario.microservicios.backend.commonarchivos.models.entity.Solicitud;
import com.tramite_documentario.microservicios.backend.microservicioarchivos.clients.SolicitudFeignClient;
import com.tramite_documentario.microservicios.backend.microservicioarchivos.models.entity.Correos;
import com.tramite_documentario.microservicios.backend.microservicioarchivos.services.ArchivoService;
import com.tramite_documentario.microservicios.backend.microservicioarchivos.services.TipoArchivoServiceImpl;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class ArchivoController extends CommonController<Archivo, ArchivoService> {

    @Autowired
    private TipoArchivoServiceImpl tipoArchivoService;

    @Autowired
    private SolicitudFeignClient solicitudFeignClient;

    @Autowired
    private JavaMailSender mailSender;

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

    @GetMapping("/solicitud/{id}")
    public ResponseEntity<?> listarArchivosBySolicitud(@PathVariable Long id){

        List<Archivo> archivos = this.service.findAllByIdSolicitud(id);

        return archivos != null ? ResponseEntity.ok(archivos) : ResponseEntity.notFound().build();
    }

    @GetMapping("/archivos-sin-solicitud")
    public ResponseEntity<?> listarArchivosSinSolicitud(){
        return ResponseEntity.ok(this.service.findAllByIdSolicitudIsNull());
    }

    @PostMapping("/guardar-todos")
    public ResponseEntity<?> guardarTodo(@RequestBody List<Archivo> archivos){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.saveAll(archivos));
    }

    @GetMapping("/ver-archivo/{id}")
    public ResponseEntity<?> verArchivo(@PathVariable Long id){
        Optional<Archivo> a = service.findById(id);

        if (a.isEmpty() || a.get().getFile() == null){
            return ResponseEntity.notFound().build();
        }

        Resource documento = new ByteArrayResource(a.get().getFile());

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(documento);
    }

    @GetMapping("/enviar-archivo/{id}")
    public ResponseEntity<?> enviarArchivo(@PathVariable Long id, @RequestBody Correos correos) throws IOException, MessagingException {

        Optional<Archivo> a = service.findById(id);

        if (a.isEmpty() || a.get().getFile() == null){
            return ResponseEntity.notFound().build();
        }

        Resource documento = new ByteArrayResource(a.get().getFile());


        List<String> emails = correos.getCorreos();

        String[] emailsToSend = new String[emails.size()];

        for (int i = 0; i < emailsToSend.length; i++) {
            emailsToSend[i] = emails.get(i);
        }


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("ginoag7@gmail.com", "Mikasa Support");
        helper.setTo(emailsToSend);
        helper.addAttachment(a.get().getTipoArchivo().getNombre() + ".pdf", documento, "application/json");

        String mensaje = "Envio pdf adjunto de archivo" + a.get().getTipoArchivo().getNombre();

        String contenido = "<h1>Hola Usuario,</h1> " +
                "<p>Le alcanzo el archivo solicitado</p>";

        helper.setSubject(mensaje);
        helper.setText(contenido, true);

        mailSender.send(message);

        for (String email: emails){
            System.out.println(email);
        }

        return ResponseEntity.ok().body("Nada");
    }
}
