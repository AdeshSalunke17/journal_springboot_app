package com.developer.journal.service;

import com.developer.journal.responsehandler.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PetService {
    @Autowired
    private RestTemplate restTemplate;
    private final String URL = "https://petstore.swagger.io/v2/pet/findByStatus?status=STATUS";
    public Pet[] getPetsByStatus(String status) {
        String finalUrl = URL.replace("STATUS", status);
        ResponseEntity<Pet[]> result = restTemplate.getForEntity(finalUrl, Pet[].class);
        if(result.getStatusCode().value() == 200) {
            return result.getBody();
        } else {
            return null;
        }
    }
}
