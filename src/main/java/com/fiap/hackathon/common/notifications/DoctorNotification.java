package com.fiap.hackathon.common.notifications;

public class DoctorNotification {

    public static final String SUBJECT = "Health&Med - Nova consulta agendada";
    public static final String BODY = "Olá, Dr. %s!\nVocê tem uma nova consulta marcada!\nPaciente: %s.\nData e horário: %s às %s.";

    public static String create(String doctorName, String patientName, String date, String timeslot) {
        return String.format(BODY,
                doctorName,
                patientName,
                date,
                timeslot);
    }
}
