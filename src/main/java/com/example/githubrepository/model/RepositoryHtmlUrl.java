package com.example.githubrepository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryHtmlUrl {
    @JsonProperty("html_url")
    private String htmlUrl;
}
