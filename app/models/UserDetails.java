package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetails {
    UserSubDetails data;

    public UserSubDetails getData() {
        return data;
    }

    public void setData(UserSubDetails data) {
        this.data = data;
    }
}
