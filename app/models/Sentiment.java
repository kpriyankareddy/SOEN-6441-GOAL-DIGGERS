package models;


import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 Handles sentiment of individual submissions and calculates overall sentiments
 @author Ravnit Sehgal
 */
public class Sentiment {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sentiment.class);
    private static final Map<String, Phase> sentiments = new LinkedHashMap<>();

    public enum Phase {
        HAPPY,
        SAD,
        NEUTRAL
    }

    public static Map<String, Phase> getSentiments() {
        return sentiments;
    }

    public static void clearSentiments() {
        sentiments.clear();
    }


    public static Phase generateSentiment(String text, int sentenceCount) {
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline(); // pipeline to streamline connection to nlp model
        CoreDocument coreDocument = new CoreDocument(text); // entire text
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreSentence> sentences = coreDocument.sentences(); // breaks para into sentences and performs nlp on each sentence


        AtomicInteger numPositives = new AtomicInteger();
        AtomicInteger numNegatives = new AtomicInteger();
        AtomicInteger numNeutral = new AtomicInteger();

//        int numPositives=0,numNeutral=0,numNegatives=0;

        sentences.stream()
                .limit(sentenceCount)
                .forEach(sentence -> {
                    String sentiment = sentence.sentiment();
                    if (sentiment.equals("Neutral")) {
                        numNeutral.getAndIncrement();
                    } else if (sentiment.equals("Positive")) {
                        numPositives.getAndIncrement();
                    } else numNegatives.getAndIncrement();
                });

        return (numPositives.get() >= numNeutral.get()) && (numPositives.get() > numNegatives.get()) ?
                Phase.HAPPY :
                numNegatives.get() >= numNeutral.get() ? Phase.SAD : Phase.NEUTRAL;

    }

    public static Sentiment.Phase sentiment(int n) {
        if(n == 0)
            return Phase.SAD;
        else if(n == 1)
            return Phase.HAPPY;
        else return Phase.NEUTRAL;
    }

    public static void calculateOverallSentiment(String query, List<Submission> submission) {

        if (!sentiments.containsKey(query) && submission != null) {
            double numPositives = 0D, numNegatives = 0D;
            Phase overallSentiment;


            for (Submission post : submission) {
                if (post.getSentiment() == Phase.HAPPY)
                    numPositives += 1D;
                else if (post.getSentiment() == Phase.SAD)
                    numNegatives += 1D;
            }

            double sum = (numPositives + numNegatives);


            if ((numPositives / (sum)) >= 0.7)
                overallSentiment = Phase.HAPPY;
            else if ((numNegatives / (sum)) >= 0.7)
                overallSentiment = Phase.SAD;
            else overallSentiment = Phase.NEUTRAL;


            System.out.println("Positives: " + numPositives);
            System.out.println("Negatives: " + numNegatives);


            sentiments.put(query, overallSentiment);

        }
    }
}
