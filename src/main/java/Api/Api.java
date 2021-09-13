package Api;

import json.Root;
import main.Result;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Api {
    private static final Gson gson = new Gson();

    public static Result<Root> getWeather(String message) {
        URL url = null;
        try {
            url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=b8a1b7610d9f6e7a3f3a9c44ecfe9ba6");

        } catch (MalformedURLException e) {
            return new Result<>(e);
        }

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            return new Result<>(e);
        }

        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            return new Result<>(e);
        }

        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

       try {
           connection.connect();
       } catch (IOException exception) {
           exception.printStackTrace();
      }

        int responceCode = 0;

        try {
            responceCode = connection.getResponseCode();
        } catch (IOException e) {
            return new Result<>(e);
        }
       if (responceCode == 404) {
           return new Result<>(new NullPointerException(message));
       }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } catch (IOException e) {
                return new Result<>(e);
            }

            String inputLine;

            StringBuilder responce = new StringBuilder();

            try {
                while ((inputLine = in.readLine()) != null) {
                    responce.append(inputLine);
                }
                in.close();
            } catch (IOException e) {
                return new Result<>(e);
            }

            try {
                return new Result<>(gson.fromJson(responce.toString(), Root.class));
            } catch (NullPointerException e) {
                return new Result<>(e);
            }
        }
    }
