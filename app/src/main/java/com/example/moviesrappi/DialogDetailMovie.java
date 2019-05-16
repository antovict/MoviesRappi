package com.example.moviesrappi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesrappi.adapters.AdapterItemVideo;
import com.example.moviesrappi.cache.SingletonCache;
import com.example.moviesrappi.models.Movie;
import com.example.moviesrappi.models.Movies;
import com.example.moviesrappi.models.Videos;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.moviesrappi.httpconects.Api.api_key;

public class DialogDetailMovie extends AppCompatActivity {
    public Context ctx;
    public Button btnClose;
    RatingBar ratingBar;
    TextView txtDate;
    TextView txtOverview;
    TextView txtType;
    TextView txtTitle;
    ImageView imgPoster;
    Movie movie;
    int i = 0;
    int tipo = 0;
    boolean isMovie = false;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_detail_movies);
        SingletonCache.initInstance();
        i = getIntent().getExtras().getInt("movie");
        isMovie = getIntent().getExtras().getBoolean("isMovie");
        tipo = getIntent().getExtras().getInt("tipo");
        Movies movies = SingletonCache.listActual;
        movie = movies.getResults().get(i);
        try{
            //requestWindowFeature(Window.FEATURE_NO_TITLE);
            txtTitle = (TextView) findViewById(R.id.txtTitle);
            txtDate = (TextView) findViewById(R.id.txtDate);
            txtOverview = (TextView) findViewById(R.id.txtOverview);
            txtType = (TextView) findViewById(R.id.txtType);
            imgPoster = (ImageView) findViewById(R.id.imgPoster);
            ratingBar = (RatingBar) findViewById(R.id.rating);
            btnClose = (Button) findViewById(R.id.btnClose);
            mRecyclerView = (RecyclerView) findViewById(R.id.rvVideos);
            mRecyclerView.setHasFixedSize(true);

            this.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            txtTitle.setText(movie.getTitle());
            txtDate.setText(movie.getRelease_date());
            txtType.setText(movie.isAdult() ? "Adulto" : "Todo Publico");
            txtOverview.setText(movie.getOverview());
            if (movie.getBitmap()!= null){
                imgPoster.setImageBitmap(movie.getBitmap());
            }else{
                imgPoster.setImageResource(R.drawable.ic_place_holder_24dp);
            }
            ratingBar.setRating(movie.getVote_average()/2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        new GetVideosListTask(movie).execute();
    }

    private class GetVideosListTask extends AsyncTask<String, Void, Response> {
        Movie movie1;

        public GetVideosListTask(Movie movie){
            this.movie1 = movie;
        }

        protected Response doInBackground(String...urls){
            OkHttpClient client = new OkHttpClient();
            Response response;
            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, "{}");
            String url = "";
            if (isMovie){
                url = "https://api.themoviedb.org/3/movie/" + movie1.getId() + "/videos?language=en-US&api_key=" + api_key;
            }else{
                url = "https://api.themoviedb.org/3/tv/" + movie1.getId() + "/videos?language=en-US&api_key=" + api_key;
            }
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
                response = null;
            }
            return response;
        }

        protected void onPostExecute(Response response){
            Videos videos = null;
            if (response != null){
                if (response.code()==200){ //online
                    try {
                        videos = new Gson().fromJson(response.body().string(), Videos.class);
                    } catch (IOException e) {
                        videos = null;
                    }
                    mLayoutManager = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new AdapterItemVideo(getApplicationContext(), videos);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }else{ //offline
                //Snackbar.make(mRecyclerView, "Sin Conexion", Snackbar.LENGTH_LONG).show();
            }
            if (videos == null){
                mRecyclerView.setVisibility(View.GONE);
            }else{
                if(videos.getResults().size()<=0){
                    mRecyclerView.setVisibility(View.GONE);
                }else{
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}