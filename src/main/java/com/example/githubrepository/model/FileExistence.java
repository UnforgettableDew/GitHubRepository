package com.example.githubrepository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileExistence {
    private String filename;

    @JsonProperty("is_exist")
    private Boolean isExist;
    private String path;

    public FileExistence(String filename, Boolean isExist) {
        this.filename = filename;
        this.isExist = isExist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileExistence that = (FileExistence) o;
        return filename.equals(that.filename) && isExist.equals(that.isExist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename, isExist);
    }
}
