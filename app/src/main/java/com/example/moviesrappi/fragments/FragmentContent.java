package com.example.moviesrappi.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.moviesrappi.R;
import com.example.moviesrappi.adapters.AdapterItemMovie;
import com.example.moviesrappi.adapters.GridRecyclerView;
import com.example.moviesrappi.httpconects.DownloadJSON;
import com.example.moviesrappi.cache.SingletonCache;
import com.example.moviesrappi.models.Movie;
import com.example.moviesrappi.models.Movies;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.moviesrappi.httpconects.Urls.url_movies_search;
import static com.example.moviesrappi.httpconects.Urls.url_path_poster;

public class FragmentContent  extends Fragment {
    static String TIPO_MOVIES = "TIPO_MOVIES";
    static String QUERY_MOVIES = "QUERY_MOVIES";
    static String IS_MOVIE = "IS_MOVIE";
    String query;
    int tipo;
    boolean isMovie;
    ImageView previewImage;
    TextView txtEmpty;
    View contentPreview;
    TextView txtTitle;
    TextView txtSubtitle;
    TextView txtTipo;
    GridRecyclerView rvItems;
    Movies lsItems;
    AdapterItemMovie adapter;

    public static final FragmentContent newInstance(int tipo, String query, boolean isMovie) {
        FragmentContent fragment = new FragmentContent();
        final Bundle args = new Bundle(2);
        args.putInt(TIPO_MOVIES, tipo);
        args.putBoolean(IS_MOVIE, isMovie);
        args.putString(QUERY_MOVIES, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        SingletonCache.initInstance();
        tipo = getArguments().getInt(TIPO_MOVIES);
        query = getArguments().getString(QUERY_MOVIES);
        isMovie = getArguments().getBoolean(IS_MOVIE);
        // Setup any handles to view objects here
        txtEmpty = (TextView) view.findViewById(R.id.txtEmpty);
        txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        contentPreview = (View) view.findViewById(R.id.contentPreview);
        txtSubtitle = (TextView) view.findViewById(R.id.txtSubTitle);
        txtTipo = (TextView) view.findViewById(R.id.txtTipo);
        rvItems = (GridRecyclerView) view.findViewById(R.id.rvItems);
        previewImage = (ImageView) view.findViewById(R.id.previewImage);
        rvItems.setHasFixedSize(true);
        GridLayoutManager llm = new GridLayoutManager(getContext(), 2);
        rvItems.setLayoutManager(llm);
        lsItems = null;
        contentPreview.setVisibility(View.VISIBLE);
        switch (tipo){
            case 0:
                //popular movies
                if (isMovie){
                    lsItems = SingletonCache.moviesPolupar;
                }else{
                    lsItems = SingletonCache.seriesPolupar;
                }
                break;
            case 1:
                //top Rated
                if (isMovie){
                    lsItems = SingletonCache.moviesTopRated;
                }else{
                    lsItems = SingletonCache.seriesTopRated;
                }
                break;
            case 2:
                //upcoming
                if (isMovie){
                    lsItems = SingletonCache.moviesUpcoming;
                }else{
                    lsItems = SingletonCache.seriesUpcoming;
                }
                break;
            case 3:
                //search
                contentPreview.setVisibility(View.GONE);
                new TaskListSearch(tipo, query).execute();
                break;
            default:
                //popular
                if (isMovie){
                    lsItems = SingletonCache.moviesPolupar;
                }else{
                    lsItems = SingletonCache.seriesPolupar;
                }
                break;
        }
        String texto = "";
        switch (tipo){
            case 0:
                texto = "Listado de Populares";
                break;
            case 1:
                texto = "Listado de Top Rated";
                break;
            case 2:
                texto = "Listado de Upcoming";
                break;
        }
        txtTipo.setText(texto);
        try {
            if (lsItems.getResults().get(0).getBitmap()==null){
                try{
                    new DownloadJSON((AppCompatActivity) getActivity(), false).ejecutarImageDownload(previewImage, 0, tipo, isMovie,url_path_poster + lsItems.getResults().get(0).getPoster_path());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
               previewImage.setImageBitmap(lsItems.getResults().get(0).getBitmap());
            }

            adapter= new AdapterItemMovie((AppCompatActivity)getActivity(),lsItems, tipo, isMovie);
            rvItems.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            txtEmpty.setVisibility(View.GONE);
            txtTitle.setVisibility(View.VISIBLE);
            txtSubtitle.setVisibility(View.VISIBLE);
            txtTitle.setText(lsItems.getResults().get(0).getTitle());
            txtSubtitle.setText(lsItems.getResults().get(0).getRelease_date());
            rvItems.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            txtTitle.setVisibility(View.GONE);
            txtSubtitle.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
            txtEmpty.setText("Error en Respuesta");
            rvItems.setVisibility(View.GONE);
        }
    }


    private class TaskListSearch extends AsyncTask<Void, String, Response> {
        int tipo;
        String query;
        public TaskListSearch(int tipo, String query){
            this.tipo = tipo;
            this.query = query;
        }
        @Override
        protected Response doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, "{}");
            Request  request;
            request = new Request.Builder().url(url_movies_search + "&language=en-US&page=1&include_adult=false&query=" + query).get().build();

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
            Movies movies = null;
            if (response != null){
                if (response.code()==200){ //online
                    try {
                        movies = new Gson().fromJson(response.body().string(), Movies.class);
                        if (tipo==0){
                            SingletonCache.moviesPolupar = movies;
                        }
                        if (tipo==1){
                            SingletonCache.moviesTopRated = movies;
                        }
                        if (tipo==2){
                            SingletonCache.moviesUpcoming = movies;
                        }
                        //Log.i("ONLINE", movies.toString());
                        adapter= new AdapterItemMovie((AppCompatActivity)getActivity(),movies, tipo, isMovie);
                        rvItems.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        txtEmpty.setVisibility(View.GONE);
                        rvItems.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        txtEmpty.setVisibility(View.VISIBLE);
                        txtEmpty.setText("Error en Respuesta");
                        rvItems.setVisibility(View.GONE);
                    }
                }
            }else{ //offline
                if (tipo==0){
                    movies = SingletonCache.moviesPolupar;
                }
                if (tipo==1){
                    movies = SingletonCache.moviesTopRated;
                }
                if (tipo==2){
                    movies = SingletonCache.moviesUpcoming;
                }
                Snackbar.make(rvItems, "Sin Conexion", Snackbar.LENGTH_LONG).show();
                if (query!=null){
                    if (movies !=null){
                        ArrayList<Movie> aux = new ArrayList<Movie>();
                        int max = movies.getResults().size();
                        for (int i = 0; i<max; i++){
                            if (movies.getResults().get(i).getTitle().toLowerCase().contains(query.toLowerCase())){
                                aux.add(movies.getResults().get(i));
                                Log.i("AUX", movies.getResults().get(i).toString());
                            }
                        }
                        if (aux.size()>0){
                            adapter= new AdapterItemMovie((AppCompatActivity)getActivity(),new Movies(1, aux.size(), 1, aux), tipo, isMovie);
                            rvItems.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            txtEmpty.setVisibility(View.GONE);
                            rvItems.setVisibility(View.VISIBLE);
                        }else{
                            txtEmpty.setVisibility(View.VISIBLE);
                            txtEmpty.setText("Listado Vacio.");
                            rvItems.setVisibility(View.GONE);
                        }
                    }else {
                        txtEmpty.setVisibility(View.VISIBLE);
                        txtEmpty.setText("Listado Vacio.");
                        rvItems.setVisibility(View.GONE);
                    }
                }else{
                    if (movies.getResults().size()>0){
                        adapter= new AdapterItemMovie((AppCompatActivity)getActivity(),movies, tipo, isMovie);
                        rvItems.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        txtEmpty.setVisibility(View.GONE);
                        rvItems.setVisibility(View.VISIBLE);
                    }else{
                        txtEmpty.setVisibility(View.VISIBLE);
                        txtEmpty.setText("Listado Vacio.");
                        rvItems.setVisibility(View.GONE);
                    }
                }
            }
        }

        @Override
        protected void onPreExecute() {
            txtEmpty.setVisibility(View.VISIBLE);
            txtEmpty.setText("Buscando...");
            rvItems.setVisibility(View.GONE);
        }
    }
}
