package stocks;

public interface NewSubmissionGenerator {
    UpdatePosts newQuote(UpdatePosts last);
    UpdatePosts seed();
}
