package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.UserDetails;
import models.UserSubmission;
import models.UserSubmissionDetails;
import org.springframework.util.CollectionUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class UserInfoService {

    public UserDetails getUserDetails(String username) {
        UserDetails userDetails = null;
        String userDetailslink = "https://www.reddit.com/user/" + URLEncoder.encode(username, StandardCharsets.UTF_8) + "/about.json";
        try {
            userDetails = new ObjectMapper().readValue(getDetails(userDetailslink), UserDetails.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (userDetails != null && userDetails.getData() != null && userDetails.getData().getIcon_img() != null) {
            userDetails.getData().setIcon_img(userDetails.getData().getIcon_img());
        }
        return userDetails;
    }

    public List<UserSubmission> getUserSubmissions(String username) {
        List<UserSubmission> userSubmission = null;
        String userSubmissionsLink = "https://api.pushshift.io/reddit/search/submission/?author=" + URLEncoder.encode(username, StandardCharsets.UTF_8) + "&sort=desc&sort_type=created_utc&size=10";
        UserSubmissionDetails userSubmissionDetails = null;
        try {
            userSubmissionDetails = new ObjectMapper().readValue(getDetails(userSubmissionsLink), UserSubmissionDetails.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (userSubmissionDetails != null && !CollectionUtils.isEmpty(userSubmissionDetails.getData()))
            userSubmission = userSubmissionDetails.getData();
        if (userSubmission != null) {
            userSubmission.forEach(submission -> {
                submission.setDate(submission.getCreated_utc());
            });
        }
        return userSubmission;
    }

    public String getDetails(String requestLink) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(requestLink)).build();
            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .join();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
