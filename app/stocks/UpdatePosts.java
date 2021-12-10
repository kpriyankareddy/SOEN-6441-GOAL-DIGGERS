package stocks;

import models.Submission;

import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class UpdatePosts { // contains current version of state
    public final String query;
    public final List<Submission> posts; // Todo: Step 5. Change parameters to refactor submissions

    public UpdatePosts(String query, List<Submission> posts) {
        System.out.println("Updating source from stock quote:"+query);
        this.query = requireNonNull(query);
        this.posts = requireNonNull(posts);
    }
}
