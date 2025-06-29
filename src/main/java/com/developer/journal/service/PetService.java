package com.developer.journal.service;

import com.developer.journal.constant.ApiConst;
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

    public Pet[] getPetsByStatus(String status) {
        String finalUrl = AppCacheService.appCache.get(ApiConst.PET_API.toString()).replace("<status>", status);
        ResponseEntity<Pet[]> result = restTemplate.getForEntity(finalUrl, Pet[].class);
        if(result.getStatusCode().value() == 200) {
            return result.getBody();
        } else {
            return null;
        }
    }
}
