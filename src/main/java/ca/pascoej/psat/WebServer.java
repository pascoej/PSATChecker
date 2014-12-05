package ca.pascoej.psat;

import com.google.gson.Gson;
import spark.*;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by john on 12/5/14.
 */
public class WebServer {
    private static int threads = 5;

    public static void main(String[] args) {
        final Gson gson = new Gson();
        if (args.length == 1) {
            threads = Integer.parseInt(args[0]);
        }
        try {
            Spark.setPort(80);
        }catch (Exception e){
            Spark.setPort(4567);
        }
        Spark.staticFileLocation("/public");
        Spark.post("/psat/getscores", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                String user = (String) request.queryParams("username");
                String pass = (String) request.queryParams("password");
                Map<String, String> vars = new HashMap<String, String>();
                if (user == null || pass == null || user.equalsIgnoreCase("username_here"))
                    return "put a username pls";
                ScoreChecker scoreChecker = new ScoreChecker(threads);
                RawScores rawScores = scoreChecker.scrapeRawScores(scoreChecker.login(user, pass));
                vars.put("mc", rawScores.getMath().getCorrect() + "");
                vars.put("mi", rawScores.getMath().getIncorrect() + "");
                vars.put("mo", rawScores.getMath().getOmitted() + "");

                vars.put("rc", rawScores.getReading().getCorrect() + "");
                vars.put("ri", rawScores.getReading().getIncorrect() + "");
                vars.put("ro", rawScores.getReading().getOmitted() + "");

                vars.put("wc", rawScores.getWriting().getCorrect() + "");
                vars.put("wi", rawScores.getWriting().getIncorrect() + "");
                vars.put("wo", rawScores.getWriting().getOmitted() + "");
                ModelAndView modelAndView = new ModelAndView(vars, "scores-temp.html");
                return new MustacheTemplateEngine().render(modelAndView);
            }
        });
        Spark.get("/psat/:username/:password", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                String user = request.params(":username");
                String pass = request.params(":password");
                if (user == null || pass ==null || user.equalsIgnoreCase("username_here"))
                    return "put a username pls";
                ScoreChecker scoreChecker = new ScoreChecker(threads);
                RawScores rawScores = scoreChecker.scrapeRawScores(scoreChecker.login(user,pass));
                return gson.toJson(rawScores);
            }
        });
    }
}
