package com.example.githubrepository.controller;

import com.example.githubrepository.model.*;
import com.example.githubrepository.model.request.RepositoryImportRequest;
import com.example.githubrepository.model.request.RepositoryRequest;
import com.example.githubrepository.service.GitHubRepositoryCreator;
import com.example.githubrepository.service.GitHubService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/github")
public class GitHubController {
    private final GitHubService gitHubService;
    private final GitHubRepositoryCreator gitHubRepositoryCreator;

    @Autowired
    public GitHubController(GitHubService gitHubService, GitHubRepositoryCreator gitHubRepositoryCreator) {
        this.gitHubService = gitHubService;
        this.gitHubRepositoryCreator = gitHubRepositoryCreator;
    }

    @GetMapping
    public String hello() {
        return "hello";
    }

    @GetMapping("/user/{username}/repos")
    public GitHubRepository[] getGithubRepositories(@PathVariable String username) {
        return gitHubService.getRepositories(username);
    }

    @GetMapping("/user/{username}/repo/{repositoryName}/branches")
    public List<Branch> getGitHubBranches(@PathVariable String username,
                                          @PathVariable String repositoryName) throws JsonProcessingException {
        return gitHubService.getBranches(username, repositoryName);
    }


    @GetMapping("/user/{username}/repo/{repositoryName}/commits/{branch}")
    public CommitBranch getGitHubCommitsByBranch(@PathVariable String username,
                                                 @PathVariable String repositoryName,
                                                 @PathVariable String branch) throws ParseException, JsonProcessingException {
        return gitHubService.getCommits(username, repositoryName, branch);
    }

    @GetMapping("/user/{username}/repo/{repositoryName}/commits/{branch}/last-commit")
    public CommitBranch getLastCommit(@PathVariable String username,
                                      @PathVariable String repositoryName,
                                      @PathVariable String branch) throws ParseException, JsonProcessingException {
        return gitHubService.getLastCommit(username, repositoryName, branch);
    }

    @GetMapping("/user/{username}/repo/{repositoryName}/commits/last-commits")
    public List<CommitBranch> getLastCommitsOnEachBranch(@PathVariable String username,
                                                         @PathVariable String repositoryName)
            throws ParseException, JsonProcessingException {
        return gitHubService.getLastCommitsOnEachBranch(username, repositoryName);
    }

    @GetMapping("/user/{username}/repo/{repositoryName}/all-commits")
    public List<CommitBranch> getAllCommits(@PathVariable String username,
                                            @PathVariable String repositoryName)
            throws ParseException, JsonProcessingException {

        return gitHubService.getAllCommitsOnEachBranch(username, repositoryName);
    }

    @GetMapping("/user/{username}/repo/{repositoryName}/last-commit")
    public CommitBranch getLastCommit(@PathVariable String username,
                                      @PathVariable String repositoryName)
            throws ParseException, JsonProcessingException {

        return gitHubService.getTotalLastBranch(username, repositoryName);
    }

    @PostMapping("/user/{username}/repo/{repositoryName}/files")
    public List<FileExistence> getFiles(@RequestParam("access_token") String accessToken,
                                        @PathVariable String username,
                                        @PathVariable String repositoryName,
                                        @RequestBody List<String> filenames) throws ParseException, JsonProcessingException, ExecutionException, InterruptedException {
        String url = "https://api.github.com/repos/" + username + "/" + repositoryName + "/contents";
        return gitHubService.checkBasicFilesExistence(url, accessToken, filenames);
    }

    @PostMapping("/repo/create")
    public ResponseEntity<RepositoryHtmlUrl> createRepo(@RequestParam("access_token") String accessToken,
                                                        @RequestBody RepositoryRequest request) throws JsonProcessingException {
        return new ResponseEntity<>(gitHubRepositoryCreator.createRepository(accessToken, request), HttpStatus.CREATED);
    }

    @PutMapping("/repo/import")
    public RepositoryHtmlUrl importRepo(@RequestParam("access_token") String accessToken,
                                        @RequestBody RepositoryImportRequest request) throws JsonProcessingException {
        return gitHubService.importRepo(accessToken, request);
    }


//    @PutMapping("/create/readme")
//    public String createReadme(@RequestParam("access_token") String accessToken,
//                               @RequestBody RepositoryRequest request){
//
//    }

    //TODO: check gitignore

//    @GetMapping("/{repositoryName}/{username}/all-distinct-commits")
//    public ResponseEntity<List<CommitBranch>> getAllDistinctCommits(@PathVariable String username,
//                                                            @PathVariable String repositoryName)
//            throws ParseException, JsonProcessingException {
//
//        return new ResponseEntity<>(gitHubService.getDistinctCommits(username, repositoryName), HttpStatus.OK);
//    }
}
