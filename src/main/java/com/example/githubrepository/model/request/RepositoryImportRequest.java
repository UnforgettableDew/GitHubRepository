package com.example.githubrepository.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryImportRequest {
    @JsonProperty("vcs_url")
    private String vcsUrl;
    private String username;
    private String repo;
}
