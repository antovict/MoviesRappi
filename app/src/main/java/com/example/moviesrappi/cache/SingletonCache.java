package com.example.moviesrappi.cache;

import com.example.moviesrappi.models.Movie;
import com.example.moviesrappi.models.Movies;
import com.example.moviesrappi.models.Video;

public class SingletonCache
{
    private static SingletonCache instance;

    public static Movies moviesPolupar = new Movies();
    public static Movies listActual = new Movies();
    public static Movies moviesTopRated = new Movies();
    public static Movies moviesUpcoming = new Movies();
    public static Movies seriesPolupar = new Movies();
    public static Movies seriesTopRated = new Movies();
    public static Movies seriesUpcoming = new Movies();
    public static Movie detail = null;
    public static Video viewVideo = null;

    public static void initInstance()
    {
        if (instance == null)
        {
            instance = new SingletonCache();
        }
    }

    public static SingletonCache getInstance()
    {
        // Return the instance
        return instance;
    }

    private SingletonCache()
    {
        // Constructor hidden because this is a singleton
    }

    public void customSingletonMethod()
    {
        // Custom method
    }
}