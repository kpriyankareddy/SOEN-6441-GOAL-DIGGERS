package stocks;

import java.util.List;

import static java.util.Objects.requireNonNull;

/** A JSON presentation class for stock history. */
public class PostHistory {
    private final String symbol;
    private final List<UpdatePosts> posts;

    public PostHistory(String symbol, List<UpdatePosts> prices) { // Has symbol and history of previous prices
        this.symbol = requireNonNull(symbol);
        this.posts = requireNonNull(prices);
    }

    public String getType() {

//        System.out.println("Asking to update history");
        return "stockhistory";
    }

    public String getSymbol() {
        return symbol;
    }

    public List<UpdatePosts> getHistory() {
        return posts;
    }
}
