package com.example.moviesrappi.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesrappi.R;
import com.example.moviesrappi.httpconects.DownloadJSON;
import com.example.moviesrappi.cache.SingletonCache;
import com.example.moviesrappi.DialogDetailMovie;
import com.example.moviesrappi.models.Movie;
import com.example.moviesrappi.models.Movies;

import java.util.ArrayList;

import static com.example.moviesrappi.httpconects.Urls.url_path_poster;

public class AdapterItemMovie extends GridRecyclerView.Adapter<AdapterItemMovie.ItemMovieHolder>{
    ArrayList<Movie> listMovies;
    Movies movies;
    int tipo;
    boolean isMovie;
    AppCompatActivity activity;
    Context ctx;


    public Movies getMovies(){
        return movies;
    }

    public AdapterItemMovie(AppCompatActivity a, Movies movies, int tipo, boolean isMovie){
        this.movies = movies;
        this.listMovies = movies.getResults();
        this.tipo = tipo;
        this.isMovie = isMovie;
        this.activity = a;
        this.ctx = a.getApplicationContext();
        SingletonCache.initInstance();
        SingletonCache.listActual = movies;
    }

    @NonNull
    @Override
    public ItemMovieHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        ItemMovieHolder pvh = new ItemMovieHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final ItemMovieHolder viewHolder, final int i) {
        viewHolder.txtTitle.setText(listMovies.get(i).getTitle());
        viewHolder.txtLanguage.setText(listMovies.get(i).getOriginal_language().toUpperCase());
        viewHolder.txtDate.setText(listMovies.get(i).getRelease_date());
        viewHolder.ratingBar.setRating(listMovies.get(i).getVote_average()/2);
        if (listMovies.get(i).getPoster_path()!=null){
            if (listMovies.get(i).getBitmap()==null){
                try{
                    new DownloadJSON((AppCompatActivity) activity, false).ejecutarImageDownload(viewHolder.imgPoster, i, tipo, isMovie,url_path_poster + listMovies.get(i).getPoster_path());
                }catch (Exception e){
                    e.printStackTrace();
                }

            }else{
                viewHolder.imgPoster.setImageBitmap(listMovies.get(i).getBitmap());
            }

        }else{
            viewHolder.imgPoster.setImageResource(R.mipmap.ic_launcher);
        }
        viewHolder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=(Integer) v.getTag();
                Movie movie= listMovies.get(position);
                switch (v.getId())
                {
                    case R.id.content:
                        if (movie!=null){
                            Intent intent = new Intent(activity, DialogDetailMovie.class);
                            intent.putExtra("movie", i);
                            intent.putExtra("isMovie", isMovie);
                            intent.putExtra("tipo", tipo);
                            activity.startActivity(intent);
                        }else{
                            Toast.makeText(activity, "No existen datos para mostrar. Vuelva a intentar.", Toast.LENGTH_SHORT).show();
                        }
//                        DisplayMetrics metrics = new DisplayMetrics(); //get metrics of screen
//                        ((Activity)activity.getApplicationContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
//                        int height = (int) (metrics.heightPixels*0.9); //set height to 90% of total
//                        int width = (int) (metrics.widthPixels*0.9); //set width to 90% of total
//                        dialog.getWindow().setLayout(width, height); //set layout
                        break;
                }
            }
        });
        viewHolder.content.setTag(i);
    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    public static class ItemMovieHolder extends RecyclerView.ViewHolder {
        RatingBar ratingBar;
        TextView txtDate;
        TextView txtLanguage;
        TextView txtTitle;
        ImageView imgPoster;
        LinearLayout content;

        ItemMovieHolder(View convertView) {
            super(convertView);
            txtDate = (TextView) convertView.findViewById(R.id.txtYear);
            txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            txtLanguage = (TextView) convertView.findViewById(R.id.txtLanguage);
            imgPoster = (ImageView) convertView.findViewById(R.id.imgPoster);
            ratingBar = (RatingBar) convertView.findViewById(R.id.rating);
            content = (LinearLayout) convertView.findViewById(R.id.content);
        }
    }
}


