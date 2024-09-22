package com.fiap.hackathon.common.beans;

import com.fiap.fastfood.common.interfaces.usecase.CustomerUseCase;
import com.fiap.fastfood.core.usecase.CustomerUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseBeanDeclaration {

    @Bean
    public CustomerUseCase customerUseCase() {
        return new CustomerUseCaseImpl();
    }

}
