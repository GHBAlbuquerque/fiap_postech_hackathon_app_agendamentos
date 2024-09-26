package com.fiap.hackathon.communication.gateways;

import com.fiap.hackathon.common.exceptions.custom.AuthenticationException;
import com.fiap.hackathon.common.interfaces.gateways.AuthenticationGateway;
import com.fiap.hackathon.external.services.users.UsersHTTPClient;

import static com.fiap.hackathon.common.exceptions.custom.ExceptionCodes.APPOINTMENT_10_DIFFERENT_USER;
import static com.fiap.hackathon.common.logging.Constants.CONTENT_TYPE;
import static com.fiap.hackathon.common.logging.Constants.MS_USER;

public class AuthenticationGatewayImpl implements AuthenticationGateway {

    private final UsersHTTPClient usersHTTPClient;

    public AuthenticationGatewayImpl(UsersHTTPClient httpClient) {
        this.usersHTTPClient = httpClient;
    }

    @Override
    public void validatePatientCaller(String email, String patientId) throws AuthenticationException {
        final var response = usersHTTPClient.getPatientById(patientId, MS_USER, CONTENT_TYPE).getBody();

        if(!email.equals(response.getEmail())) {
            throw new AuthenticationException(
                    APPOINTMENT_10_DIFFERENT_USER,
                    "Requester Patient is not the request Patient."
            );
        }
    }
}
