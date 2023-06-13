package com.example.githubrepository.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GitHubFile {
    private String filename;
    private String type;
    private String url;
    private String path;
}
