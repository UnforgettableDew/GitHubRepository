package com.example.githubrepository.util;

import com.example.githubrepository.model.Branch;
import com.example.githubrepository.model.Commit;
import com.example.githubrepository.model.CommitBranch;
import com.example.githubrepository.model.GitHubFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class GitHubJsonParser {
    private static ObjectMapper objectMapper;
    private static JsonNode jsonNode;

    public static List<Branch> parseJsonBranch(String jsonElement) throws JsonProcessingException {
        objectMapper = new ObjectMapper();
        jsonNode = objectMapper.readTree(jsonElement);

        List<Branch> branchList = new ArrayList<>();

        for (int i = 0; i < jsonNode.size(); i++) {
            JsonNode jsonBranch = jsonNode.get(i);
            branchList.add(new Branch(jsonBranch.get("name").asText(),
                    jsonBranch.get("commit").get("url").asText()));
        }

        return branchList;
    }

    public static CommitBranch parseJsonCommit(String jsonElement, String branchName) throws JsonProcessingException, ParseException {
        objectMapper = new ObjectMapper();
        jsonNode = objectMapper.readTree(jsonElement);

        CommitBranch commitBranch = new CommitBranch();
        commitBranch.setBranchName(branchName);

        List<Commit> commitList = new ArrayList<>();
        for (int i = 0; i < jsonNode.size(); i++){
            JsonNode commitNode = jsonNode.get(i).get("commit");
            commitList.add(new Commit(
                    commitNode.get("author").get("name").asText(),
                    commitNode.get("message").asText(),
                    commitNode.get("author").get("date").asText(),
                    jsonNode.get(i).get("html_url").asText()
            ));

        }
        commitBranch.setCommits(commitList);
        return commitBranch;
    }

    public static List<GitHubFile> parseJsonFile(String jsonElement) throws JsonProcessingException, ParseException{
        objectMapper = new ObjectMapper();
        jsonNode = objectMapper.readTree(jsonElement);

        List<GitHubFile> files = new ArrayList<>();
        for (int i = 0; i < jsonNode.size(); i++){
            files.add(new GitHubFile(
                    jsonNode.get(i).get("name").asText(),
                    jsonNode.get(i).get("type").asText(),
                    jsonNode.get(i).get("url").asText(),
                    jsonNode.get(i).get("path").asText()));
        }
        return files;
    }

//    public static String parseJsonUrl(String jsonElement) throws JsonProcessingException {
//        objectMapper = new ObjectMapper();
//        jsonNode = objectMapper.readTree(jsonElement);
//        return jsonNode.get("html_url").asText();
//    }

    public static String parseJsonUrl(String jsonElement) throws JsonProcessingException {
        objectMapper = new ObjectMapper();
        jsonNode = objectMapper.readTree(jsonElement);
        return jsonNode.get("html_url").asText();
    }
}
