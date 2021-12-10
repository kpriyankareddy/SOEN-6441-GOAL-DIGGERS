package services;

import models.GlobalParameters;
import models.Sentiment;
import models.Submission;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.IntStream;

/**
 Wrapper for search query made by the user
 @author Ravnit Sehgal
 */
public class QuerySearchService {

    public static String getResults(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .join();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void clearResults() {
        Sentiment.clearSentiments();
        Submission.clearPosts();
    }

    public static CompletionStage<JSONArray> processQueryAsync(String query, WSClient wsClient) {
        String url = GlobalParameters.generatePushShiftUrl(query, GlobalParameters.getPostsToEvaluate());
        final CompletionStage<WSResponse> futureResponse = wsClient.url(url)
                .get();

        return futureResponse.thenApplyAsync(response -> {
            JSONObject submissionObject = new JSONObject(response.getBody());
            JSONArray submissions = submissionObject.getJSONArray("data");
            return submissions;
        });

    }

    public static CompletionStage<List<Submission>> parseAsync(JSONArray submissions) throws JSONException {

        CompletableFuture<List<Submission>> completableFuture = new CompletableFuture<>();

        return completableFuture.completeAsync(() -> parseHelper(submissions));

    }

    public static List<Submission> parseHelper(JSONArray submissions){
        List<Submission> newPosts = new ArrayList<>();
        int postsToEvaluate = GlobalParameters.getPostsToEvaluate();
        int characterCount = GlobalParameters.getCharacterCount();
        int sentenceCount = GlobalParameters.getSentenceCount(); // determined via running multiple epochs for different keywords

        int interval = Math.min(submissions.length(), postsToEvaluate);
        long startTime = System.nanoTime();
        try {
            IntStream
                    .range(0, submissions.length()) // submission.length for filtering and keeping track of flair | not interval
                    .parallel()
                    .filter(i -> {
                        try {
                            return submissions.getJSONObject(i).getString("link_flair_type").equals("text");
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    })
                    .limit(interval)
                    .unordered()
                    .forEach(i -> {

                        JSONObject post = submissions.getJSONObject(i);
                        int id;
                        String title, author, subreddit, link, date, selftext;
                        id = i + 1;
                        title = post.getString("title");
                        author = post.getString("author");
                        subreddit = post.getString("subreddit");
                        link = post.getString("full_link");
                        Date dt = new Date(post.getLong("created_utc") * 1000);
                        date = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss").format(dt);
                        try {
                            selftext = post.getString("selftext");
                            if (selftext.length() > characterCount)
                                selftext = selftext.substring(0, characterCount);
                            if (selftext.equals(""))
                                selftext = title;
                        }catch (Exception e) {
                            e.printStackTrace();
                            selftext = title;
                        }
                        Sentiment.Phase sentiment = Sentiment.generateSentiment
                                (selftext, sentenceCount);

                        System.out.println("POST: "+i);
                        newPosts.add(new Submission(id, author, subreddit, title, link, date, sentiment));


                    });
        } catch (JSONException e) {
            e.printStackTrace();
            newPosts.add(new Submission(0, null, null, null, null, null, null));
        }

        System.out.println("Running time: " + (System.nanoTime() - startTime) / 1_000_000_000 + "s");
        System.out.println("Reference logs: " + " Words: " + characterCount + " Sentences: " + sentenceCount);

//            CompletableFuture.completedFuture(newPosts);
        return newPosts;
    }

}
