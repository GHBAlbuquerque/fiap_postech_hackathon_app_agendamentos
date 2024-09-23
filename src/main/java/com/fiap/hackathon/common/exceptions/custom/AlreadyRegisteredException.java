package com.fiap.hackathon.common.exceptions.custom;

import com.fiap.hackathon.common.exceptions.model.CustomError;
import com.fiap.hackathon.common.exceptions.model.CustomException;

import java.util.List;

public class AlreadyRegisteredException extends CustomException {

    public AlreadyRegisteredException(ExceptionCodes code, String message) {
        super(code, message);
    }

    public AlreadyRegisteredException(ExceptionCodes code, String message, List<CustomError> customErrors) {
        super(code, message, customErrors);
    }
}