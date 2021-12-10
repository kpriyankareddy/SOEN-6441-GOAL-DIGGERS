package stocks;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import models.Sentiment;
import models.Submission;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.util.Objects.requireNonNull;

/** A JSON presentation class for stock updates. */
public class UpdateSubmissions { // Has symbol and current price
    private final String symbol;
    private final List<Submission> posts;

    public UpdateSubmissions(String symbol, List<Submission> posts) {
//        System.out.println("Stock update: State - Symbol: "+symbol+" Price: "+posts);

        Map<String, List<Submission>> newPosts = Submission.generateSubmissions(symbol);

        this.symbol = requireNonNull(symbol);
        this.posts = requireNonNull(newPosts.get(symbol));
    }

    public String getType() {
        return "stockupdate";
    }

    public List<Submission> getPrice() {

        return Submission.generateSubmissions(symbol).get(symbol);
    }

    public Sentiment.Phase getOverallSentiment(){
        return Sentiment.getSentiments().get(symbol);
    }

    public String getSymbol() {
        return symbol;
    }
}
