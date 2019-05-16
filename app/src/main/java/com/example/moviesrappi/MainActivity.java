package com.example.moviesrappi;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.example.moviesrappi.cache.SingletonCache;
import com.example.moviesrappi.fragments.FragmentContent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity  implements SearchView.OnQueryTextListener{
    private LottieAnimationView lav;
    int btnSelected = -1;
    Toolbar toolbar;
    TabLayout tabs;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.action_popular:
                    if (btnSelected!=0){
                        btnSelected = 0;
                        fragment = new FragmentContent().newInstance(btnSelected, null, tabs.getSelectedTabPosition()==0);
                        replaceFragment(fragment);
                    }
                    return true;
                case R.id.action_top_rated:
                    if (btnSelected!=1){
                        btnSelected = 1;
                        fragment = new FragmentContent().newInstance(btnSelected, null,tabs.getSelectedTabPosition()==0);
                        replaceFragment(fragment);
                    }
                    return true;
                case R.id.action_upcoming:
                    if (btnSelected!=2){
                        btnSelected = 2;
                        fragment = new FragmentContent().newInstance(btnSelected, null, tabs.getSelectedTabPosition()==0);
                        replaceFragment(fragment);
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Movie");
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);
        searchView.onActionViewExpanded();
        searchView.clearFocus();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        tabs = findViewById(R.id.tabs);
        lav = (LottieAnimationView)findViewById(R.id.lav);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        SingletonCache.initInstance();
        setInitialFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            Fragment fr = null;
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fr = new FragmentContent().newInstance(btnSelected, null,tab.getPosition()==0);
                replaceFragment(fr);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setInitialFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        btnSelected = 0;
        fragmentTransaction.add(R.id.fragment, new FragmentContent().newInstance(btnSelected, null,tabs.getSelectedTabPosition()==0));
        fragmentTransaction.commit();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        new isOnline(query).execute();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    private class isOnline extends AsyncTask<String, Void, Boolean> {
        String query;
        ProgressDialog dialog;

        public isOnline(String query){
            this.query = query;
            dialog = new ProgressDialog(MainActivity.this);
        }
        @Override
        protected Boolean doInBackground(String... params) {
                Fragment fragment;
                boolean internet = isOnlineNet();
                if (internet){
                    fragment = new FragmentContent().newInstance(3, query,tabs.getSelectedTabPosition()==0);
                }else{
                    fragment = new FragmentContent().newInstance(btnSelected, query,tabs.getSelectedTabPosition()==0);
                }
                replaceFragment(fragment);
           return internet;
        }
        @Override
        protected void onPostExecute(Boolean isConnect) {
            dialog.dismiss();
        }
        @Override
        protected void onPreExecute() {
            dialog.setTitle("Comprobando Conexion");
            dialog.setMessage("Conectando a Servidor");
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    public boolean isOnlineNet(){
        try{
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int val = p.waitFor();
            boolean result = (val == 0);
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
