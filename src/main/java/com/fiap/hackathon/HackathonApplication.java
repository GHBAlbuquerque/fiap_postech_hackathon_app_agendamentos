package com.fiap.hackathon;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableAutoConfiguration
@EnableFeignClients
@OpenAPIDefinition(info = @Info(title = "Hackathon PosTech FIAP - Agendamentos", description = "Microsserviço de gerenciamento de Agendamentos realizado para o Hackathon da Pós-Graduação de Arquitetura de Sistemas da FIAP", version = "v1"))
public class HackathonApplication {

    public static void main(String[] args) {
        SpringApplication.run(HackathonApplication.class, args);
    }

}
