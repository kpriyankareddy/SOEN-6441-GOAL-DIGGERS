package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSubmission {

    Integer created_utc;
    String full_link;
    String title;
    String selftext;
    String date;

    public Integer getCreated_utc() {
        return created_utc;
    }

    public String getFull_link() {
        return full_link;
    }

    public String getTitle() {
        return title;
    }

    public String getSelftext() {
        return selftext;
    }

    public String getDate() {
        return date;
    }

    public void setDate(Integer created_utc) {
        if(created_utc!=null) {
            java.util.Date date = new java.util.Date((long)created_utc * 1000);
            this.date= String.valueOf(date);
        } else {
            this.date="No data";
        }
    }
//
//    public void setCreated_utc(Integer created_utc) {
//        this.created_utc = created_utc;
//    }
//
//    public void setFull_link(String full_link) {
//        this.full_link = full_link;
//    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public void setSelftext(String selftext) {
//        this.selftext = selftext;
//    }

    public void setDate(String date) {
        this.date = date;
    }
}
