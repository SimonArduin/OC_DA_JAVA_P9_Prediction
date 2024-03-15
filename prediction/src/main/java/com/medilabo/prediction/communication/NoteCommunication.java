package com.medilabo.prediction.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.prediction.domain.Note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NoteCommunication extends BasicCommunication {

    @Value("${note.url}")
    private String NOTE_URL;

    private static final Logger logger = LoggerFactory.getLogger(NoteCommunication.class);

    public List<Note> getNoteList(int patientId) {

        logger.info("get note list for id " + patientId);
        setHeaders();

        // send request to Note microservice
        HttpEntity request = new HttpEntity<>(null, headers);
        ResponseEntity<List<LinkedHashMap<String, Object>>> responseEntity = new RestTemplate()
                .exchange(NOTE_URL + "getbypatientid?patientId=" + patientId,
                        HttpMethod.GET,
                        request,
                        new ParameterizedTypeReference<List<LinkedHashMap<String, Object>>>() {
                        });

        List<LinkedHashMap<String, Object>> responseBody = responseEntity.getBody();

        // convert List<LinkedHashMap> to List<Note>
        ObjectMapper objectMapper = new ObjectMapper();
        List<Note> noteList = responseBody.stream()
                .map(noteMap -> objectMapper.convertValue(noteMap, Note.class))
                .collect(Collectors.toList());

        return noteList;
    }
}
