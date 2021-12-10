package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSubDetails {

    Integer total_karma;
    String name;
    SubredditsInfo subreddit;
    String snoovatar_img;
    String icon_img;

    public void setTotal_karma(Integer total_karma) {
        this.total_karma = total_karma;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubreddit(SubredditsInfo subreddit) {
        this.subreddit = subreddit;
    }

    public void setSnoovatar_img(String snoovatar_img) {
        this.snoovatar_img = snoovatar_img;
    }

    public SubredditsInfo getSubreddit() {
        return subreddit;
    }

    public Integer getTotal_karma() {
        return total_karma;
    }

    public String getName() {
        return name;
    }

    public String getSnoovatar_img() { return snoovatar_img; }

    public String getIcon_img() { return icon_img; }

    public void setIcon_img(String url) {
        if(url != null) {
            url = url.split("[?]")[0];
        }
        this.icon_img = url;
    }

}
