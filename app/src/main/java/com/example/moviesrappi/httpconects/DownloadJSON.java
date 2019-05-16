package com.example.moviesrappi.httpconects;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviesrappi.MainActivity;
import com.example.moviesrappi.R;
import com.example.moviesrappi.cache.SingletonCache;
import com.example.moviesrappi.models.Movies;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.moviesrappi.httpconects.Urls.url_movies_popular;
import static com.example.moviesrappi.httpconects.Urls.url_movies_top_rated;
import static com.example.moviesrappi.httpconects.Urls.url_movies_upcoming;
import static com.example.moviesrappi.httpconects.Urls.url_series_popular;
import static com.example.moviesrappi.httpconects.Urls.url_series_top_rated;
import static com.example.moviesrappi.httpconects.Urls.url_series_upcoming;

public class DownloadJSON {
    AppCompatActivity actividad;
    boolean close = false;
    View content = null;
    TextView title = null;

    public DownloadJSON(AppCompatActivity a, boolean close) {
        this.actividad = a;
        this.close = close;
        this.title = null;
    }

    public DownloadJSON(AppCompatActivity a, boolean close, View contentTitle) {
        this.actividad = a;
        this.close = close;
        this.content = contentTitle;
        this.title = (TextView) contentTitle.findViewById(R.id.titleProgress);
    }

    public AppCompatActivity getActivity() {
        return this.actividad;
    }

    public void ejecutar() {
        new TaskGetMoviesPopular().execute();
    }

    public void ejecutarImageDownload(ImageView iv, int pos, int tipo, boolean isMovie, String url) {
        new DownLoadImageTask(iv, pos, tipo, isMovie).execute(url);
    }

    public boolean isClose() {
        return this.close;
    }

    private class TaskGetMoviesPopular extends AsyncTask<Void, String, Response> {
        @Override
        protected Response doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, "{}");
            Request request = new Request.Builder().url(url_movies_popular).get().build();
            try {
                Response response = client.newCall(request).execute();
                return response;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response response) {
            // execution of result of Long time consuming operation
            if (response != null) {
                if (response.code() == 200) { //online
                    try {
                        Movies movies = new Gson().fromJson(response.body().string(), Movies.class);
                        SingletonCache.moviesPolupar = movies;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            new TaskGetMoviesTopRated().execute();
        }

        @Override
        protected void onPreExecute() {
            if (title != null) {
                title.setText("Cargando Movies Popular");
                content.setVisibility(View.VISIBLE);
            } else {
                content.setVisibility(View.GONE);
            }
        }
    }

    private class TaskGetMoviesTopRated extends AsyncTask<Void, String, Response> {
        @Override
        protected Response doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, "{}");
            Request request = new Request.Builder().url(url_movies_top_rated).get().build();
            try {
                Response response = client.newCall(request).execute();
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response response) {
            // execution of result of Long time consuming operation
            if (response != null) {
                if (response.code() == 200) { //online
                    try {
                        Movies movies = new Gson().fromJson(response.body().string(), Movies.class);
                        SingletonCache.moviesTopRated = movies;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            new TaskGetMoviesUpcoming().execute();
        }

        @Override
        protected void onPreExecute() {
            if (title != null) {
                title.setText("Cargando Movies Top Rated");
                content.setVisibility(View.VISIBLE);
            } else {
                content.setVisibility(View.GONE);
            }
        }
    }

    private class TaskGetMoviesUpcoming extends AsyncTask<Void, String, Response> {
        @Override
        protected Response doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, "{}");
            Request request = new Request.Builder().url(url_movies_upcoming).get().build();
            try {
                Response response = client.newCall(request).execute();
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response response) {
            // execution of result of Long time consuming operation
            if (response != null) {
                if (response.code() == 200) { //online
                    try {
                        Movies movies = new Gson().fromJson(response.body().string(), Movies.class);
                        SingletonCache.moviesUpcoming = movies;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            new TaskGetSeriesPopular().execute();
        }

        @Override
        protected void onPreExecute() {
            if (title != null) {
                title.setText("Cargando Movies Upcoming");
                content.setVisibility(View.VISIBLE);
            } else {
                content.setVisibility(View.GONE);
            }
        }
    }

    private class TaskGetSeriesPopular extends AsyncTask<Void, String, Response> {
        @Override
        protected Response doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, "{}");
            Request request = new Request.Builder().url(url_series_popular).get().build();
            try {
                Response response = client.newCall(request).execute();
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response response) {
            // execution of result of Long time consuming operation
            if (response != null) {
                if (response.code() == 200) { //online
                    try {
                        Movies movies = new Gson().fromJson(response.body().string(), Movies.class);
                        SingletonCache.seriesPolupar = movies;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            new TaskGetSeriesTopRated().execute();
        }

        @Override
        protected void onPreExecute() {
            if (title != null) {
                title.setText("Cargando Series Popular");
                content.setVisibility(View.VISIBLE);
            } else {
                content.setVisibility(View.GONE);
            }
        }
    }

    private class TaskGetSeriesTopRated extends AsyncTask<Void, String, Response> {
        @Override
        protected Response doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, "{}");
            Request request = new Request.Builder().url(url_series_top_rated).get().build();
            try {
                Response response = client.newCall(request).execute();
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response response) {
            // execution of result of Long time consuming operation
            if (response != null) {
                if (response.code() == 200) { //online
                    try {
                        Movies movies = new Gson().fromJson(response.body().string(), Movies.class);
                        SingletonCache.seriesTopRated = movies;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            new TaskGetSeriesUpcoming().execute();
        }

        @Override
        protected void onPreExecute() {
            if (title != null) {
                title.setText("Cargando Series Top Rated");
                content.setVisibility(View.VISIBLE);
            } else {
                content.setVisibility(View.GONE);
            }
        }
    }
    private class TaskGetSeriesUpcoming extends AsyncTask<Void, String, Response> {
        @Override
        protected Response doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, "{}");
            Request request = new Request.Builder().url(url_series_upcoming).get().build();
            try {
                Response response = client.newCall(request).execute();
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response response) {
            // execution of result of Long time consuming operation
            if (response != null) {
                if (response.code() == 200) { //online
                    try {
                        Movies movies = new Gson().fromJson(response.body().string(), Movies.class);
                        SingletonCache.seriesUpcoming = movies;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Intent i = new Intent(actividad.getApplicationContext(), MainActivity.class);
            actividad.startActivity(i);
            if (isClose()) {
                actividad.finish();
            }
        }

        @Override
        protected void onPreExecute() {
            if (title != null) {
                title.setText("Cargando Series Upcoming");
                content.setVisibility(View.VISIBLE);
            } else {
                content.setVisibility(View.GONE);
            }
        }
    }

    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        int position;
        int tipo;
        boolean isMovie;

        public DownLoadImageTask(ImageView imageView, int position, int tipo, boolean isMovie){
            this.imageView = imageView;
            this.position = position;
            this.tipo = tipo;
            this.isMovie = isMovie;
        }

        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result){
            if (result==null){
                imageView.setImageResource(R.drawable.ic_place_holder_24dp);
            }else{
                if (result.equals("")){
                    result = null;
                    imageView.setImageResource(R.drawable.ic_place_holder_24dp);
                }else{
                    imageView.setImageBitmap(result);
                }
            }
            if (tipo==0){
                if (isMovie){
                    SingletonCache.moviesPolupar.getResults().get(position).setBitmap(result);
                }else{
                    SingletonCache.seriesPolupar.getResults().get(position).setBitmap(result);
                }
            }
            if (tipo==1){
                if (isMovie){
                    SingletonCache.moviesTopRated.getResults().get(position).setBitmap(result);
                }else{
                    SingletonCache.seriesTopRated.getResults().get(position).setBitmap(result);
                }

            }
            if (tipo==2){
                if (isMovie){
                    SingletonCache.moviesUpcoming.getResults().get(position).setBitmap(result);
                }else{
                    SingletonCache.seriesUpcoming.getResults().get(position).setBitmap(result);
                }
            }
        }
    }
}
