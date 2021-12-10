package models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import services.QuerySearchService;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.IntStream;

/**
 Handles parsing and handling of individual submissions
 @author Ravnit Sehgal
 */
public class Submission {

    private final String author;
    private final String postLink;
    private final String subreddit;
    private final int id;
    private String title;
    private final String date;
    private final Sentiment.Phase sentiment;
    public static Logger LOGGER = Logging.LOGGER;
    private static Random sentimentGen = new Random();

    private static Map<String, List<Submission>> posts = new LinkedHashMap<>(); // global variable

    public Submission(int id, String author, String subreddit, String title,
                      String postLink,
                      String date,
                      Sentiment.Phase sentiment) {
        this.id = id;
        this.author = author;
        this.subreddit = subreddit;
        this.postLink = postLink;
        this.title = title;
        this.date = date;
        this.sentiment = sentiment;
    }

    public String getDate() {
        return date;
    }

    public Sentiment.Phase getSentiment() {
        return sentiment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public String getPostLink() {
        return postLink;
    }

    //parses response to a List of Submissions
    public static List<Submission> parse(String response) throws JSONException {
        JSONObject submissionObject = new JSONObject(response);
        JSONArray submissions = submissionObject.getJSONArray("data");
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
                        selftext = post.getString("selftext");
                        if (selftext.length() > characterCount)
                            selftext = selftext.substring(0, characterCount);
                        if (selftext.equals(""))
                            selftext = title;
//                        Sentiment.Phase sentiment = Sentiment.generateSentiment
//                                (selftext, sentenceCount);
                        int n = sentimentGen.nextInt(3);
                        Sentiment.Phase sentiment = Sentiment.sentiment(n);

//                        Sentiment.Phase sentiment = Sentiment.Phase.HAPPY;
                        newPosts.add(new Submission(id, author, subreddit, title, link, date, sentiment));

                    });
        } catch (JSONException e) {
            e.printStackTrace();
            newPosts.add(new Submission(0, null, null, null, null, null, null));
        }

        System.out.println("Running time: " + (System.nanoTime() - startTime) / 1_000_000_000 + "s");
        System.out.println("Reference logs: " + " Words: " + characterCount + " Sentences: " + sentenceCount);

        return newPosts;
    }

    // generates all submissions at a time
    public static Map<String, List<Submission>> generateSubmissions(String query) {
        String link = GlobalParameters.generatePushShiftUrl(query, GlobalParameters.getPostsToEvaluate());
        List<Submission> submissions;
        System.out.println("Request to update submissions for query: "+query);
        try {
            String response = QuerySearchService.getResults(link);
            submissions = parse(response); // get all results
            if (submissions.size() == 0 || response.equals(""))
                throw new Exception("No posts found for " + query);
        } catch (Exception e) {
            System.out.println("Request failed: "+query);
            e.printStackTrace();
            LOGGER.info(e.getMessage());
            return null;
        }

        posts.put(query, submissions);
        try {
            posts.get(query).sort(Comparator.comparing(Submission::getDate).reversed()); // sort in ordered pair
        } catch (Exception e){
            System.out.println("Error while sorting: ");
            e.printStackTrace();
        }
        Sentiment.calculateOverallSentiment(query, posts.get(query));
        return posts;
    }

    public static CompletionStage<Map<String, List<Submission>>> generateSubmissionsAsync(String query){
        String link = GlobalParameters.generatePushShiftUrl(query, GlobalParameters.getPostsToEvaluate());
        List<Submission> submissions;
        System.out.println("Request to update submissions for query: "+query);
        try {
            String response = QuerySearchService.getResults(link);
            submissions = parse(response); // get all results
            if (submissions.size() == 0 || response.equals(""))
                throw new Exception("No posts found for " + query);
        } catch (Exception e) {
            System.out.println("Request failed: "+query);
            LOGGER.info(e.getMessage());
            return null;
        }

        if (submissions.size() == 0)
            CompletableFuture.failedFuture(new Exception("No posts found for query: "+query));
//
//
//        if (posts.containsKey(query) && (posts.get(query).size() == submissions.size()))
//            return CompletableFuture.completedFuture(posts);
//
//        if (posts.isEmpty() && submissions.size() > 0) {
//            posts.put(query, submissions);
//        } else if(submissions.size() > 0) {
//            Map<String, List<Submission>> newPost = new LinkedHashMap<>(); // cloning post and reordering posts to put new query on top
//            newPost.put(query, submissions); // put the newest query first
//            newPost.putAll(posts); //reassigning top result posts to global variable
//            posts = newPost;
//        }
//        posts.get(query).sort(Comparator.comparing(Submission::getDate).reversed()); // sort in ordered pair
//        System.out.println("KeySet: "+posts.keySet());
        posts.put(query, submissions);
        posts.get(query).sort(Comparator.comparing(Submission::getDate).reversed()); // sort in ordered pair
        return CompletableFuture.completedFuture(posts);
    }

    public static Map<String, List<Submission>> clearPosts() {
        posts.clear();
        return posts;
    }

    public static Map<String, List<Submission>> getPosts() {
        return posts;
    }

}
