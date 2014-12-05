package ca.pascoej.psat;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

/**
 * Created by john on 12/5/14.
 */
public class ScoreWorker implements Runnable {
    private static final String BASE_URL = "https://quickstart.collegeboard.org/posweb/questionInfoNewAction.do?testYear=2014&skillCd=";
    Map<String, String> cookies;
    String subject;
    int questionNumber;

    boolean done;
    boolean correct;
    boolean gotAnswer;
    boolean omitted;

    public ScoreWorker(Map<String, String> cookies, String subject, int questionNumber) {
        this.cookies = cookies;
        this.subject = subject;
        this.questionNumber = questionNumber;
    }

    public void check() {
        String url = BASE_URL + subject + "&questionNbr=" +  questionNumber;
        try {
            String body = Jsoup.connect(url).cookies(cookies).execute().body();
            if (body.contains("answered correctly")) {
                correct = true;
                gotAnswer = true;
            }else if (body.contains("answered incorrectly")) {
                correct = false;
                gotAnswer = true;
            }else if (body.contains("omitted")){
                omitted = true;
                gotAnswer = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        done = true;
    }

    public boolean isDone() {
        return done;
    }

    public boolean isGotAnswer() {
        return gotAnswer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public boolean isOmitted() {
        return omitted;
    }

    @Override
    public void run() {
        check();
    }
}
