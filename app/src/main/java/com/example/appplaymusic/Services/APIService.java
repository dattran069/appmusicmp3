package com.example.appplaymusic.Services;

public class APIService {
    private static String baseUrl="https://appmusic274.000webhostapp.com/Server/";
    public static DataService getService(){
        return APIRetrofitClient.getClient(baseUrl).create(DataService.class);
    }
}
