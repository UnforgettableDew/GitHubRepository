package com.example.githubrepository.service;

import com.example.githubrepository.model.*;
import com.example.githubrepository.model.request.RepositoryImportRequest;
import com.example.githubrepository.model.request.RepositoryRequest;
import com.example.githubrepository.util.GitHubJsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class GitHubService {
    private final RestTemplate restTemplate;

    @Autowired
    public GitHubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GitHubRepository[] getRepositories(String username) {
        String url = "https://api.github.com/users/" + username + "/repos";
        GitHubRepository[] repositories = restTemplate.getForObject(url, GitHubRepository[].class);
        return repositories;
    }

    public List<Branch> getBranches(String username, String repositoryName) throws JsonProcessingException {
        String url = "https://api.github.com/repos/" + username + "/" + repositoryName + "/branches";
        String jsonBranches = restTemplate.getForObject(url, String.class);

        return GitHubJsonParser.parseJsonBranch(jsonBranches);
    }

    public CommitBranch getCommits(String username,
                                   String repositoryName,
                                   String branchName) throws JsonProcessingException, ParseException {
        String url = "https://api.github.com/repos/" + username + "/" + repositoryName + "/commits?sha=" + branchName;
        String jsonCommits = restTemplate.getForObject(url, String.class);

        return GitHubJsonParser.parseJsonCommit(jsonCommits, branchName);
    }

    public CommitBranch getLastCommit(String username,
                                      String repositoryName,
                                      String branchName) throws ParseException, JsonProcessingException {
        return new CommitBranch(branchName,
                List.of(getCommits(username, repositoryName, branchName).getCommits().get(0)));
    }

    public List<CommitBranch> getLastCommitsOnEachBranch(String username, String repositoryName)
            throws JsonProcessingException, ParseException {
        List<CommitBranch> commitBranchList = new ArrayList<>();

        for (Branch branch : getBranches(username, repositoryName)) {
            commitBranchList.add(new CommitBranch(
                    branch.getName(),
                    getLastCommit(username, repositoryName, branch.getName()).getCommits()));
        }
        return commitBranchList;
    }

    public List<CommitBranch> getAllCommitsOnEachBranch(String username, String repositoryName) throws JsonProcessingException, ParseException {
        List<CommitBranch> commitBranchList = new ArrayList<>();
        for (Branch branch : getBranches(username, repositoryName)) {
            commitBranchList.add(new CommitBranch(
                    branch.getName(),
                    getCommits(username, repositoryName, branch.getName()).getCommits()));

        }
        return commitBranchList;
    }

    public CommitBranch getTotalLastBranch(String username, String repositoryName) throws ParseException, JsonProcessingException {
        List<CommitBranch> commitBranchList = getLastCommitsOnEachBranch(username, repositoryName);
        CommitBranch lastCommit = commitBranchList.get(0);

        for (CommitBranch commitBranch : commitBranchList) {
            if (commitBranch.getCommits().get(0).getDate().compareTo(lastCommit.getCommits().get(0).getDate()) > 0) {
                lastCommit = commitBranch;
            }
        }

        return lastCommit;
    }

    public RepositoryHtmlUrl importRepo(String accessToken, RepositoryImportRequest request) throws JsonProcessingException {
        String apiUrl = "https://api.github.com/repos/" + request.getUsername() + "/" + request.getRepo() + "/import";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("vcs_url", request.getVcsUrl());
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.PUT, requestEntity, String.class);
        return new RepositoryHtmlUrl(GitHubJsonParser.parseJsonUrl(responseEntity.getBody()));

    }

    public List<FileExistence> checkBasicFilesExistence(String path, String accessToken, List<String> filenames)
            throws ParseException, JsonProcessingException, ExecutionException, InterruptedException {
//        filenames.add(".gitignore");
//        filenames.add("SecurityPartApplication.java");
//        filenames.add("SecurityPartApplication1.java");
//        filenames.add("ApplicationUserRepository.java");
//        filenames.add("fff.java");
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<List<FileExistence>> fileExistenceFuture =
                executorService.submit(new ThreadFileChecker(executorService, path, accessToken, new RestTemplate(),filenames));
        List<FileExistence> fileExistence = fileExistenceFuture.get();

        for(String filename:filenames){
            if(!fileExistence.contains(new FileExistence(filename,true)))
                fileExistence.add(new FileExistence(filename, false, null));
        }

        return fileExistence;
    }

}
