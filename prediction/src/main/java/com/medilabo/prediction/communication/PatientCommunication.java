package com.medilabo.prediction.communication;

import com.medilabo.prediction.domain.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PatientCommunication extends BasicCommunication {

    @Value("${patient.url}")
    private String PATIENT_URL;

    private static final Logger logger = LoggerFactory.getLogger(PatientCommunication.class);

    public Patient getPatient(int patientId) {

        logger.info("get patient with id " + patientId);
        setHeaders();

        HttpEntity request = new HttpEntity<>(null, headers);
        Patient patient = new RestTemplate().exchange(PATIENT_URL + "get?id=" + patientId, HttpMethod.GET, request, Patient.class).getBody();

        return patient;
    }
}
