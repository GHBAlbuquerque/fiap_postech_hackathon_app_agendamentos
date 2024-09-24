package com.fiap.hackathon.common.notifications;

public class DoctorNotification {
    public static final String DOCTOR_NOTIFICATION = "Health&Med - Nova consulta agendada”\nCorpo do e-mail:\n”Olá, Dr. %s!\nVocê tem uma nova consulta marcada!\nPaciente: %s.\nData e horário: %s às %s.";

    public static String create(String doctorName, String patientName, String date, String timeslot) {
        return String.format(DOCTOR_NOTIFICATION,
                doctorName,
                patientName,
                date,
                timeslot);
    }
}
