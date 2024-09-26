package com.fiap.hackathon.common.exceptions.custom;

import com.fiap.hackathon.common.exceptions.model.CustomError;
import com.fiap.hackathon.common.exceptions.model.CustomException;

import java.util.List;

public class CreateEntityException extends CustomException {

    public CreateEntityException(ExceptionCodes code, String message) {
        super(code, message);
    }

    public CreateEntityException(ExceptionCodes code, String message, List<CustomError> customErrors) {
        super(code, message, customErrors);
    }
}