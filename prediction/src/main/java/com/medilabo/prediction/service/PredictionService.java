package com.medilabo.prediction.service;

import com.medilabo.prediction.communication.NoteCommunication;
import com.medilabo.prediction.communication.PatientCommunication;
import com.medilabo.prediction.domain.Note;
import com.medilabo.prediction.domain.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PredictionService {

    @Autowired
    private NoteCommunication noteCommunication;

    @Autowired
    private PatientCommunication patientCommunication;

    @Value("${patient.url}")
    private String PATIENT_URL;

    private static final Logger logger = LoggerFactory.getLogger(PredictionService.class);

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

    /**
     * This method calls the patient microservice and the note microservice,
     * then runs an algorithm returning the risk for the patient to suffer from diabetes
     * @param patientId the id of the patient
     * @return a String corresponding to the risk level
     */
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
        if ((isMale && under30 && triggers >= 5) // if patient is male, under 30, and has 5 triggers or more
                || (!isMale && under30 && triggers >= 7) // if patient is female, under 30, and has 7 triggers or more
                || (!under30 && triggers >= 8)) // if patient is over 30 and has 8 triggers or more
            return "Early onset";

        if ((isMale && under30 && triggers >= 3) // if patient is male, under 30, and has 3 triggers or more
                || (!isMale && under30 && triggers >= 4) // if patient is female, under 30, and has 4 triggers or more
                || (!under30 && triggers >= 6)) // if patient is over 30 and has 6 triggers or more
            return "Danger";

        if (!under30 && triggers >= 2) // if patient is under 30 and has 2 triggers or more
            return "Borderline";

        return "None";

    }

    /**
     * This method counts the number of times any word from the trigger list is found in a list of notes
     * @param noteList the list of notes to inspect
     * @return an int of the number of triggers found
     */
    public int calculateTriggerNumber(List<Note> noteList) {

        return (int) noteList.stream()
                // remove empty and blank notes from noteList
                .filter(note -> {
                    String noteContent = note.getNote();
                    return noteContent != null && !noteContent.isBlank();
                })
                // create a map of each instance of a trigger found in each note
                .flatMap(note -> triggerList.stream()
                        .map(trigger -> trigger.toLowerCase())
                        .filter(trigger -> note.getNote().toLowerCase().contains(trigger)))
                // get the size of the map
                .count();
    }

}
