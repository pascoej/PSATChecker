package ca.pascoej.psat;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

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
