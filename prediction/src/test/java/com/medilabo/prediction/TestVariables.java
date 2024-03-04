package com.medilabo.prediction;

import com.medilabo.prediction.domain.Note;
import com.medilabo.prediction.domain.Patient;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class TestVariables {
    public Date datePast;
    public Patient patient;
    public Note note;
    public List<Note> noteList;

    public void initializeVariables() {
        // initialize male patient over 30
        datePast = Date.valueOf(LocalDate.now().minusYears(31));
        patient = new Patient("firstName", "lastName", datePast, Patient.Gender.MALE, "adress", "0123456789");
        patient.setIdPatient(1);
        // initialize note with 1 trigger
        note = new Note(patient.getIdPatient(), patient.getFirstName(), "Taille");
        noteList = List.of(note);
    }

}
