package com.example.githubrepository.model;

import com.example.githubrepository.util.GitHubJsonParser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadFileChecker implements Callable<List<FileExistence>> {
    private ExecutorService executorService;
    private String url;
    private String accessToken;
    private RestTemplate restTemplate;
    private List<String> filenames;

    public ThreadFileChecker(ExecutorService executorService,
                             String url,
                             String accessToken,
                             RestTemplate restTemplate,
                             List<String> filenames) {
        this.executorService = executorService;
        this.url = url;
        this.accessToken = accessToken;
        this.restTemplate = restTemplate;
        this.filenames = filenames;
    }

    @Override
    public List<FileExistence> call() throws Exception {
        List<FileExistence> result = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String jsonFiles = response.getBody();
        List<GitHubFile> files = GitHubJsonParser.parseJsonFile(jsonFiles);

        for (GitHubFile ghFile : files) {
            if (ghFile.getType().equals("dir")) {
                List<FileExistence> fileExistenceFuture =
                        executorService.submit(new ThreadFileChecker(executorService, ghFile.getUrl(), accessToken, restTemplate, filenames)).get();
                result.addAll(fileExistenceFuture);
            }
            for(String checkfile:filenames){
                if (ghFile.getFilename().equals(checkfile)){
                    result.add(new FileExistence(checkfile, true, ghFile.getPath()));
                }
            }
//            if(ghFile.getFilename().equals("SecurityPartApplication.java")){
//                result.add(new FileExistence("SecurityPartApplication.java",true));
//            }
//            if(ghFile.getFilename().equals("ApplicationUserRepository.java")){
//                result.add(new FileExistence("ApplicationUserRepository.java",true));
//            }
        }
        return result;
    }
}
