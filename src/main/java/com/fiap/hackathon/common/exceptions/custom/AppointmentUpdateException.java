package com.fiap.hackathon.common.exceptions.custom;

import com.fiap.hackathon.common.exceptions.model.CustomError;
import com.fiap.hackathon.common.exceptions.model.CustomException;

import java.util.List;

public class AppointmentUpdateException extends CustomException {

    public AppointmentUpdateException(ExceptionCodes code, String message) {
        super(code, message);
    }

    public AppointmentUpdateException(ExceptionCodes code, String message, List<CustomError> customErrors) {
        super(code, message, customErrors);
    }
}
