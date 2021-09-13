package Api;

import json.Root;
import main.Result;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Api {
    private static final Gson gson = new Gson();

    public static Result<Root> getWeather(String message) {
       try {
           URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=b8a1b7610d9f6e7a3f3a9c44ecfe9ba6");
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("GET");
           connection.setRequestProperty("User-Agent", "Mozilla/5.0");
           connection.connect();
           int responceCode = connection.getResponseCode();

           if (responceCode == 404) {
               return new Result<>(new NullPointerException(message));
           }
           BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
           String inputLine;

           StringBuilder responce = new StringBuilder();
           while ((inputLine = in.readLine()) != null) {
               responce.append(inputLine);
           }
           in.close();
           return new Result<>(gson.fromJson(responce.toString(), Root.class));

       } catch (IOException e) {
           return new Result<>(e);
       }
    }
}
