package com.example.githubrepository.service;

import com.example.githubrepository.model.RepositoryHtmlUrl;
import com.example.githubrepository.model.request.RepositoryRequest;
import com.example.githubrepository.util.GitHubJsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class GitHubRepositoryCreator {
    @Autowired
    private RestTemplate restTemplate;

    public RepositoryHtmlUrl createRepository(String accessToken, RepositoryRequest request) throws JsonProcessingException {
        String apiUrl = "https://api.github.com/user/repos";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", request.getName());
        requestBody.put("description", request.getDescription());

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
            System.out.println("Repository created successfully");
        } else {
            System.out.println("Failed to create repository");
        }
        return new RepositoryHtmlUrl(GitHubJsonParser.parseJsonUrl(responseEntity.getBody()));
    }
}


