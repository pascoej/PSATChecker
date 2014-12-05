package ca.pascoej.psat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Created by john on 12/5/14.
 */
public class SubjectChecker {
    private ExecutorService service;
    private Map<String, String> cookies;
    private String subject;
    private int questions;

    private RawScores.Subscore subscore;

    private List<ScoreWorker> workers = new ArrayList<ScoreWorker>();

    public SubjectChecker(ExecutorService service, Map<String, String> cookies, String subject, int questions) {
        this.service = service;
        this.cookies = cookies;
        this.subject = subject;
        this.questions = questions;
    }

    public void addWorkers() {
        for (int i = 1; i <= questions; i++) {
            workers.add(new ScoreWorker(cookies,subject,i));
        }
        for (ScoreWorker scoreWorker : workers) {
            service.execute(scoreWorker);
        }
    }
    public RawScores.Subscore getSubscore() {
        if (subscore != null) {
            return subscore;
        }
        int correct = 0;
        int incorrect = 0;
        int omitted = 0;
        for (ScoreWorker scoreWorker : workers) {
            if (scoreWorker.isDone() && scoreWorker.isGotAnswer()) {
                if (scoreWorker.isOmitted()) {
                   omitted += 1;
                }else if (scoreWorker.isCorrect()) {
                    correct += 1;
                }else {
                    incorrect += 1;
                }
            }
        }
        subscore = new RawScores.Subscore(correct,incorrect,omitted);
        return subscore;
    }


}
