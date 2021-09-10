package servlets;

import com.google.gson.Gson;
import json.Error;
import json.Parametres;
import json.UpParametres;
import main.DateNow;
import main.Repository;
import main.Result;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class WeatherServlet extends HttpServlet {
    private static final Gson gson = new Gson();
    Executor executor;
    public WeatherServlet(Executor executor){
        this.executor = executor;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");

        AsyncContext context = req.startAsync();
        context.start(() -> {
            Result<List<Parametres>> parametresResult = getParametres(req.getParameterValues("tag"), executor);
            if(parametresResult.result != null) {
                List<Parametres> results = parametresResult.result;
                    try {
                        resp.getWriter().print(gson.toJson(new UpParametres(DateNow.getDateNow(), results)));
                        resp.setStatus(200);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                        resp.setStatus(500);
                        }
                    } else {
                    resp.setStatus(500);
                    Error error = new Error(parametresResult.exception);
                    try {
                        resp.getWriter().print(gson.toJson(error));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            context.complete();
        });
    }

    public static Result<List<Parametres>> getParametres(String [] tags, Executor executor){
        List<Result<Parametres>> statsResult = null;
        try {
            statsResult = Repository.getWeatherForTags(tags, executor).get();

        } catch (InterruptedException | ExecutionException e){
            return new Result<>(e);
        }

        Optional<Exception> error = statsResult.stream()
                .filter(r -> r.exception != null)
                .map(r -> r.exception)
                .findFirst();
        if (error.isPresent()){
            return new Result<>(error.get());
        } else {
            LinkedList<Parametres> stats = new LinkedList<>();

            statsResult.stream()
                    .filter(r -> r.result != null)
                    .map(r -> r.result)
                    .forEach(stats::add);

            return new Result<>(stats);
        }
    }
}
