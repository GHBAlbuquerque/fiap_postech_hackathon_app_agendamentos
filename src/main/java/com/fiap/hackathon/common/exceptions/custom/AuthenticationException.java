package com.fiap.hackathon.common.exceptions.custom;

import com.fiap.hackathon.common.exceptions.model.CustomException;

public class AuthenticationException extends CustomException {

    public AuthenticationException(ExceptionCodes code, String message) {
        super(code, message);
    }
}
