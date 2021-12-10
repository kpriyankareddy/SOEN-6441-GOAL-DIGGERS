package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSubmissionDetails {

    List<UserSubmission> data;

    public List<UserSubmission> getData() {
        return data;
    }

    public void setData(List<UserSubmission> data) {
        this.data = data;
    }
}
