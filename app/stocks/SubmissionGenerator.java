package stocks;

import models.Submission;

import java.util.List;
import java.util.Map;

public class SubmissionGenerator implements NewSubmissionGenerator {

    private final String symbol;
    private static Map<String, List<Submission>> posts;

    public SubmissionGenerator(String symbol) {
        System.out.println("Will generate posts for the first time");
        this.symbol = symbol;
        Map<String, List<Submission>> posts = Submission.generateSubmissions(symbol);
        this.posts = posts;
    }

    public static Map<String, List<Submission>> getPosts(){
        return posts;
    }


    public static void clearPosts(){
        posts.clear();
    }

    @Override
    public UpdatePosts newQuote(UpdatePosts last) { // TODO. Step 5. Add new submissions based on previous ones + Remove random.
        return new UpdatePosts(last.query, last.posts);
    }


    @Override
    public UpdatePosts seed() {
        return new UpdatePosts(symbol, posts.get(symbol));
    }
}
