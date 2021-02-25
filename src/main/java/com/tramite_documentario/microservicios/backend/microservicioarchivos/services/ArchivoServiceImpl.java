package com.tramite_documentario.microservicios.backend.microservicioarchivos.services;

import com.tramite_documentario.microservicio.backend.commonmicroservicios.services.CommonServiceImpl;
import com.tramite_documentario.microservicios.backend.commonarchivos.models.entity.Archivo;
import com.tramite_documentario.microservicios.backend.microservicioarchivos.models.repository.ArchivoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class ArchivoServiceImpl extends CommonServiceImpl<Archivo, ArchivoRepository> implements ArchivoService{
    
	

    @Autowired
    private JavaMailSender mailSender;
	
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
    
    public void sendFiles(List<String> emails, Resource documento, String nombreArchivo) throws MessagingException, UnsupportedEncodingException {
    	String[] emailsToSend = new String[emails.size()];

        for (int i = 0; i < emailsToSend.length; i++) {
            emailsToSend[i] = emails.get(i);
        }


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("ginoag7@gmail.com", "Mikasa Support");
        helper.setTo(emailsToSend);
        helper.addAttachment(nombreArchivo + ".pdf", documento, "application/json");

        String mensaje = "Por favor no responder: Envio de archivo" + nombreArchivo;

        String contenido = "<h1>Hola Usuario,</h1> " +
                "<p>Se le alcanzo el archivo solicitado</p>";

        helper.setSubject(mensaje);
        helper.setText(contenido, true);

        mailSender.send(message);
    }
}
