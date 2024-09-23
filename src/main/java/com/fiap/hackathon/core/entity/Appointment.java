package com.fiap.hackathon.core.entity;

import com.fiap.hackathon.common.exceptions.custom.AppointmentCreationException;
import com.fiap.hackathon.common.exceptions.custom.ExceptionCodes;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class Appointment {

    private String id;
    private String doctorId;
    private String patientId;
    private LocalDate date;
    private String timeslot;
    private LocalDateTime createdAt;

    public void isValid() throws AppointmentCreationException {
        validateTimeslot();
        validateDate();
    }

    private void validateTimeslot() throws AppointmentCreationException {
        if (Boolean.FALSE.equals(TimeSlotsEnum.isValid(this.timeslot))) {
            final var message = String.format("TimeSlot %s is not available. Please select times ranging from 7:00-18:00, with 1h duration max.", timeslot);

            throw new AppointmentCreationException(
                    ExceptionCodes.APPOINTMENT_07_APPOINTMENT_CREATION,
                    message
            );
        }
    }

    private void validateDate() throws AppointmentCreationException {
        /*final var startingTime = timeslot.split("-");
        final var localTime = LocalTime.parse(startingTime);
        LocalDateTime.of(date, localTime);*/

        if (this.date.isBefore(LocalDate.now())) {
            final var message = "Invalid date selected. Please select a Date in the future.";

            throw new AppointmentCreationException(
                    ExceptionCodes.APPOINTMENT_07_APPOINTMENT_CREATION,
                    message
            );
        }
    }

}
