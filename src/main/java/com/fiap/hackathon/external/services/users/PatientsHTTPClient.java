package com.fiap.hackathon.external.services.users;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "mscliente",
        url = "${fiap.postech.fastfood.mscliente.url}"
)
public interface PatientsHTTPClient {

    /*@GetMapping(value = "/customers/{id}")
    ResponseEntity<GetCustomerResponse> getCustomerById(
            @PathVariable("id") final Long customerId,
            @RequestHeader("microsservice") final String microsservico,
            @RequestHeader("Content-Type") final String contentType
    );

    @GetMapping(value = "/customers")
    ResponseEntity<GetCustomerResponse> getCustomerByCpf(
            @RequestParam("cpf") final String customerCpf,
            @RequestHeader("microsservice") final String microsservico,
            @RequestHeader("Content-Type") final String contentType
    );*/
}
