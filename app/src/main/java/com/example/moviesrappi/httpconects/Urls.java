package com.example.moviesrappi.httpconects;


import static com.example.moviesrappi.httpconects.Api.api_key;

public class Urls
{
    public static final String url_movies_popular = "https://api.themoviedb.org/3/movie/popular?api_key=" + api_key;
    public static final String url_movies_top_rated = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + api_key;
    public static final String url_movies_upcoming = "https://api.themoviedb.org/3/movie/upcoming?api_key=" + api_key;
    public static final String url_movies_search = "https://api.themoviedb.org/3/search/movie?api_key=" + api_key;
    public static final String url_series_popular = "https://api.themoviedb.org/3/tv/popular?api_key=" + api_key;
    public static final String url_series_top_rated = "https://api.themoviedb.org/3/tv/top_rated?api_key=" + api_key;
    public static final String url_series_upcoming = "https://api.themoviedb.org/3/tv/on_the_air?api_key=" + api_key;
    public static final String url_series_search = "https://api.themoviedb.org/3/search/tv?api_key=" + api_key;
    public static final String url_path_poster = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/";
}
