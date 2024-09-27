package com.fiap.hackathon.common.interfaces.gateways;

import com.fiap.hackathon.common.exceptions.custom.AuthenticationException;

public interface AuthenticationGateway {

    void validatePatientCaller(String email, String requestId) throws AuthenticationException;
}
