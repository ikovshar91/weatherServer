package main;

import json.Root;
import json.Parametres;
import Api.Api;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

public class Repository {

    public static CompletableFuture<Result<Parametres>> getWeatherForTag(String tag , Executor executor){
        return CompletableFuture.supplyAsync(() -> {
          Result<Root> rootResult = Api.getWeather(tag);
           Root root = rootResult.result;
           if(root == null){
               return new Result<>(rootResult.exception);
           }

           Parametres parametres = new Parametres();
                parametres.city = root.name;
                parametres.temperature = root.main.temp;
                parametres.feelsLike = root.main.feels_like;
                parametres.windSpeed = root.wind.speed;

           return new Result<>(parametres);
        }, executor);
    }

    public static CompletableFuture<List<Result<Parametres>>> getWeatherForTags(String [] tags, Executor executor){
        List<CompletableFuture<Result<Parametres>>> futures = Arrays.stream(tags)
                .map(x -> getWeatherForTag(x,executor))
                .collect(Collectors.toList());
        return FutureUtils.sequence(futures);
    }



}
