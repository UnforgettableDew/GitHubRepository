package com.example.githubrepository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Commit implements Comparable<Calendar>{
    private String author;
    private String message;
    private Calendar date;
    @JsonProperty("github_commit_url")
    private String githubCommitUrl;

    public Commit(String author, String message, String date, String githubCommitUrl) throws ParseException {
        this.author = author;
        this.message = message;
        this.githubCommitUrl = githubCommitUrl;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date parsedDate = dateFormat.parse(date);

        this.date = Calendar.getInstance();
        this.date.setTime(parsedDate);
    }


    @Override
    public int compareTo(Calendar otherDate) {
        return date.compareTo(otherDate);
    }
}
