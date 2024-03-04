package com.medilabo.prediction.service;

import com.medilabo.prediction.communication.NoteCommunication;
import com.medilabo.prediction.communication.PatientCommunication;
import com.medilabo.prediction.domain.Note;
import com.medilabo.prediction.domain.Patient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;

@Service
public class PredictionService {

    @Autowired
    private NoteCommunication noteCommunication;

    @Autowired
    private PatientCommunication patientCommunication;

    @Value("${patient.url}")
    private String PATIENT_URL;

    private final HttpHeaders headers;

    private static final Logger logger = LoggerFactory.getLogger(PredictionService.class);

    @Autowired
    public PredictionService() {
        String username = "practitioner";
        String password = "password";
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(
                auth.getBytes(Charset.forName("US-ASCII")));
        String authHeader = "Basic " + new String(encodedAuth);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        this.headers = headers;
    }

    private List<String> triggerList = List.of(
            "Hémoglobine A1C",
            "Microalbumine",
            "Taille",
            "Poids",
            "Fumeur",
            "Fumeuse",
            "Anormal",
            "Cholestérol",
            "Vertiges",
            "Rechute",
            "Réaction",
            "Anticorps");

    public String getPrediction(int patientId) {

        logger.info("prediction for id " + patientId);

        // get patient and notes
        Patient patient = patientCommunication.getPatient(patientId);
        List<Note> noteList = noteCommunication.getNoteList(patient.getIdPatient());

        // calculate algorithm parameters
        Boolean under30 = patient.getAge() < 30;
        Boolean isMale = patient.getGender().equals(Patient.Gender.MALE);
        int triggers = calculateTriggerNumber(noteList);

        // run algorithm
        logger.info("run prediction algorithm");
        if ((isMale && under30 && triggers >= 5)
                || (!isMale && under30 && triggers >= 7)
                || (!under30 && triggers >= 8))
            return "Early onset";

        if ((isMale && under30 && triggers >= 3)
                || (!isMale && under30 && triggers >= 4)
                || (!under30 && triggers >= 6))
            return "Danger";

        if (!under30 && triggers >= 2)
            return "Borderline";

        return "None";

    }

    public int calculateTriggerNumber(List<Note> noteList) {

        int triggerNumber = 0;

        if(!noteList.isEmpty()) {
            for (Note note : noteList) {
                String noteContent = note.getNote();
                if (noteContent != null && !noteContent.isBlank()) {
                    for (String trigger : triggerList) {
                        trigger = trigger.toLowerCase();
                        noteContent = noteContent.toLowerCase();
                        if (noteContent.contains(trigger)) {
                            triggerNumber++;
                        }
                    }
                }
            }
        }
        return triggerNumber;
    }

}
