package models;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 Global constants defined for the overall file
 @author Ravnit Sehgal
 */
public class GlobalParameters {
    private static final String pushShiftSearchUrl = "https://api.pushshift.io/reddit/search/submission?q=";
    private static final int postsToEvaluate = 30;
    private static final int postsToShow = 10;
    private static final int characterCount = 280;
    private static final int sentenceCount = 5  ; // determined via running multiple epochs for different keywords

    public static int getCharacterCount(){
        return characterCount;
    }

    public static int getSentenceCount(){
        return sentenceCount;
    }

    public static int getPostsToEvaluate() {
        return postsToEvaluate;
    }

    public static int getPostsToShow() {
        return postsToShow;
    }



    public static String generatePushShiftUrl(String keyword, int postsToEvaluate) {
        return pushShiftSearchUrl +
                URLEncoder.encode(keyword, StandardCharsets.UTF_8)
                + "&size=" + postsToEvaluate;

    }
}
