package com.tramite_documentario.microservicios.backend.microservicioarchivos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
@EntityScan({"com.tramite_documentario.microservicios.backend.commonarchivos.models.entity"})
public class MicroservicioArchivosApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioArchivosApplication.class, args);
    }

}
