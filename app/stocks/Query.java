package stocks;

import akka.NotUsed;
import akka.japi.Pair;
import akka.japi.function.Function;
import akka.stream.ThrottleMode;
import akka.stream.javadsl.Source;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * A stock is a source of stock quotes and a symbol.
 */
public class Query {
    public final String symbol;

    private final NewSubmissionGenerator submissionGenerator;

    private final Source<UpdatePosts, NotUsed> source;

    private static final Duration duration = Duration.of(10000, ChronoUnit.MILLIS);


    public Query(String symbol) {
        this.symbol = requireNonNull(symbol); // TODO: Step 4. 1. Make network request here

        System.out.println("New query initialised");
        // done
        submissionGenerator = new SubmissionGenerator(symbol);
        source = Source.unfold(submissionGenerator.seed(), (Function<UpdatePosts, Optional<Pair<UpdatePosts, UpdatePosts>>>) last -> {
            UpdatePosts next = submissionGenerator.newQuote(last);
            return Optional.of(Pair.apply(next, next));
        });
    }

    /**
     * Returns a source of stock history, containing a single element.
     */
    public Source<PostHistory, NotUsed> history(int n) { // called every time StockHistory is updated
        System.out.println("Updating history:");
        return source.grouped(n)
//                .map(quotes -> new StockHistory(symbol, quotes.stream().map(sq -> sq.posts).collect(Collectors.toList())))
                .map(posts -> new PostHistory(symbol, posts))
                .take(1);
    }

    /**
     * Provides a source that returns a stock quote every 10000 milliseconds.
     */
    public Source<UpdateSubmissions, NotUsed> update() { // called every time StockUpdate is updated i.e 10 seconds

        return source.throttle(1, duration, 1, (ThrottleMode) ThrottleMode.shaping())
                .map(sq -> new UpdateSubmissions(sq.query, sq.posts));
    }

    @Override
    public String toString() {
        return "Stock(" + symbol + ")";
    }



}
