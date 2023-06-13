package com.example.githubrepository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonPropertyOrder({"branch_name", "commits"})
public class CommitBranch {
    @JsonProperty("branch_name")
    private String branchName;
    private List<Commit> commits;
}
