package com.medilabo.prediction.unit;

import com.medilabo.prediction.TestVariables;
import com.medilabo.prediction.communication.NoteCommunication;
import com.medilabo.prediction.communication.PatientCommunication;
import com.medilabo.prediction.domain.Note;
import com.medilabo.prediction.service.PredictionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PredictionService.class)
public class PredictionServiceTests extends TestVariables {


    @Autowired
    private PredictionService predictionService;

    @MockBean
    private NoteCommunication noteCommunication;

    @MockBean
    private PatientCommunication patientCommunication;

    @BeforeEach
    public void setUpPerTest() {
        initializeVariables();
        when(patientCommunication.getPatient(any(Integer.class))).thenReturn(patient);
        when(noteCommunication.getNoteList(any(Integer.class))).thenReturn(noteList);
    }

    private void setTriggerNumbers(int nb) {
        noteList = new ArrayList<>();
        for(int i=0 ; i<nb ; i++)
            noteList.add(note);
    }
    @Test
    public void contextLoads() {}

    @Nested
    public class getPredictionTests {

        // patient is male and over 30, all responses can be obtained by changing the number of triggers

        @Test
        public void getPredictionTestIfEarlyOnset() {
            setTriggerNumbers(8);
            when(noteCommunication.getNoteList(any(Integer.class))).thenReturn(noteList);
            String actual = predictionService.getPrediction(patient.getIdPatient());
            assertEquals("Early onset", actual);
        }

        @Test
        public void getPredictionTestIfDanger() {
            setTriggerNumbers(6);
            when(noteCommunication.getNoteList(any(Integer.class))).thenReturn(noteList);
            String actual = predictionService.getPrediction(patient.getIdPatient());
            assertEquals("Danger", actual);
        }

        @Test
        public void getPredictionTestIfBorderline() {
            setTriggerNumbers(2);
            when(noteCommunication.getNoteList(any(Integer.class))).thenReturn(noteList);
            String actual = predictionService.getPrediction(patient.getIdPatient());
            assertEquals("Borderline", actual);
        }
        @Test
        public void getPredictionTestIfNone() {
            String actual = predictionService.getPrediction(patient.getIdPatient());
            assertEquals("None", actual);
        }
    }
    
    @Nested
    public class calculateTriggerNumberTests {
        @Test
        public void calculateTriggerNumberTest() {
            setTriggerNumbers(4);
            int actual = predictionService.calculateTriggerNumber(noteList);
            assertEquals(4, actual);
        }

        @Test
        public void calculateTriggerNumberTestIfNoNoteContent() {
            note.setNote(null);
            int actual = predictionService.calculateTriggerNumber(noteList);
            assertEquals(0, actual);
        }

        @Test
        public void calculateTriggerNumberTestIfMultipleTriggersInOneNote() {
            setTriggerNumbers(0);
            noteList.add(new Note(patient.getIdPatient(), "Hémoglobine A1C, Microalbumine, Taille, Poids"));
            int actual = predictionService.calculateTriggerNumber(noteList);
            assertEquals(4, actual);
        }

    }

}
