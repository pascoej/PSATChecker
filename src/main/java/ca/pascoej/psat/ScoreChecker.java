package ca.pascoej.psat;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by john on 12/5/14.
 */
public class ScoreChecker {
    private ExecutorService service;

    public ScoreChecker(int threads) {
        service = Executors.newFixedThreadPool(threads);
    }

    public RawScores scrapeRawScores(Map<String, String> cookies) {
        SubjectChecker reading = new SubjectChecker(service,cookies, "CR", 48);
        SubjectChecker math = new SubjectChecker(service,cookies, "M", 38);
        SubjectChecker writing = new SubjectChecker(service,cookies, "W", 39);
        reading.addWorkers();
        math.addWorkers();
        writing.addWorkers();
        service.shutdown();
        try {
            service.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RawScores rawScores = new RawScores(math.getSubscore(),writing.getSubscore(),reading.getSubscore());
        return rawScores;
    }
    public Map<String, String> login(String username, String password) {
        try {
            Connection.Response response = Jsoup.connect("https://quickstart.collegeboard.org/posweb/login.jsp").userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.7.12) Gecko/20050915 Firefox/1.0.7").data("username",username).data("password",password).method(Connection.Method.POST).execute();
            return response.cookies();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
