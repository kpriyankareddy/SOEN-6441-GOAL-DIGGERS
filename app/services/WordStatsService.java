package services;

import models.Submission;
import stocks.SubmissionGenerator;

import java.util.*;

import static java.util.stream.Collectors.toMap;

/**
 This service is used to get the word statistics of submissions
 @author Priyanka Kudumula
 */

public class WordStatsService {

    public static Map<String, Integer> getWordStatisticsMap() {
        Map<String, Integer> word_counter = new HashMap<>();
        Map<String, Integer> sorted_word_counter = new HashMap<>();

        Map<String, List<Submission>> allposts = SubmissionGenerator.getPosts();

        for (Map.Entry<String, List<Submission>> e : allposts.entrySet()) {
            for (Submission submission: e.getValue()){
                String title = submission.getTitle();

                String[] tokens = title.split(" "); // split based on space

                for (String token: tokens) {

                    String word = token.toLowerCase();
                    if (word_counter.containsKey(word)) {
                        int count = word_counter.get(word); // get word count
                        word_counter.put(word, count + 1); // override word count
                    } else {
                        word_counter.put(word, 1); // initial word count to 1
                    }
                }
            }

            sorted_word_counter = word_counter
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(
                            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                    LinkedHashMap::new));

        }

        return sorted_word_counter;

    }

}
