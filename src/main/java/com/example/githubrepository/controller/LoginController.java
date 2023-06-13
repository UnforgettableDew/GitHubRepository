package com.example.githubrepository.controller;

import com.example.githubrepository.model.GitHubAccessTokenResponse;
import com.example.githubrepository.service.AccessTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*",
        allowedHeaders = "*",
        exposedHeaders = "*",
        methods = {GET, POST, PUT, DELETE},
        maxAge = 3600)
public class LoginController {
    private final AccessTokenProvider gitHubAccessTokenProvider;

    @Autowired
    public LoginController(AccessTokenProvider gitHubAccessTokenProvider) {
        this.gitHubAccessTokenProvider = gitHubAccessTokenProvider;
    }


    @GetMapping("/github/callback")
    public GitHubAccessTokenResponse handleGitHubCallback(@RequestParam("code") String code) {
        // Отримати токен доступу з GitHubOAuthService
        // Зберегти токен доступу у вашій системі (наприклад, у сеансі користувача)
        // для подальшого використання в запитах до GitHub API
//        Map<String, String> accessToken = new HashMap<>();
//        accessToken.put("github_access_token", gitHubAccessTokenProvider.getAccessToken(code));
        // Перенаправити користувача на домашню сторінку або іншу потрібну сторінку
        GitHubAccessTokenResponse accessToken =
                new GitHubAccessTokenResponse(gitHubAccessTokenProvider.getAccessToken(code));

//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlbi10eXBlIjoiYWNjZXNzLXRva2VuIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfU1RVREVOVCJ9XSwic3ViIjoiam9obiIsImlhdCI6MTY4NTM3MDg1NCwiZXhwIjoxNjg1OTcwODU0fQ.pIFW33MHU9OmzRsgPaVdrk8RgfayrWCzvjE2q92M94A");
//        HttpEntity<GitHubAccessTokenResponse> requestEntity = new HttpEntity<>(accessToken, headers);
//
//        String url = "http://localhost:8080/api/v1/student/github/save-access-token";
//        ResponseEntity<GitHubAccessTokenResponse> responseEntity = restTemplate.exchange(
//                url,
//                HttpMethod.POST,
//                requestEntity,
//                GitHubAccessTokenResponse.class
//        );
        return accessToken;
    }

    @GetMapping
    public String hello() {
        return "Hello";
    }
}
