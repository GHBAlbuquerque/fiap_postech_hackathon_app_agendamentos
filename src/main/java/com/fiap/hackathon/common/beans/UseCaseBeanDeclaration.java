package com.fiap.hackathon.common.beans;

import com.fiap.hackathon.common.interfaces.usecase.AppointmentUseCase;
import com.fiap.hackathon.core.usecase.AppointmentUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseBeanDeclaration {

    @Bean
    public AppointmentUseCase AppointmentUseCase() {
        return new AppointmentUseCaseImpl();
    }

}
