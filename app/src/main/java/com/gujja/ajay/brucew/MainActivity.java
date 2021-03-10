package com.gujja.ajay.brucew;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gujja.ajay.brucew.Room.API_Data;
import com.gujja.ajay.brucew.Room.DatabaseClient;
import com.gujja.ajay.brucew.model.Repo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements RecycleViewAdapter.OnItemClickListener {

    private static String URL = "https://api.github.com/users";
    List<Repo> repoList;
    private ArrayList<Repo> arrayList;
    private RecycleViewAdapter adapter;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressBar = findViewById(R.id.progress_bar);
        RecyclerView recyclerView = findViewById(R.id.recycle_view);

        progressBar.setVisibility(View.GONE);
        arrayList = new ArrayList<>();

        adapter = new RecycleViewAdapter(this, arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(MainActivity.this);


        FetchData();


    }

    private void FetchData() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

                Log.i("TAG", "run: " + DatabaseClient.getInstance(MainActivity.this).getAppDatabase().api_dao().getAll());

                if (activeNetwork != null && DatabaseClient.getInstance(MainActivity.this).getAppDatabase().api_dao().getAll().isEmpty()) {
                    fetchfromServer();
                } else {
                    fetchfromRoom();
                }
            }
        });
        thread.start();
    }

    private void fetchfromRoom() {
        Log.i("ajajaja", "fetchfromRoom: started");
        Thread thread = new Thread(() -> {
            List<API_Data> api_dataList = DatabaseClient.getInstance(MainActivity.this).getAppDatabase().api_dao().getAll();
            arrayList.clear();
            Log.i("nhush", "fetchfromRoom: " + api_dataList);

            for (int i = 0; i < api_dataList.size(); i++) {
                API_Data val = api_dataList.get(i);
                Repo repo = new Repo(val.getId(), val.getLoginName_(), val.getAvatar_url(), val.getType___());
                arrayList.add(repo);
            }
            runOnUiThread(() -> adapter.notifyDataSetChanged());
        });
        thread.start();
    }

    private void fetchfromServer() {
        progressBar.setVisibility(View.VISIBLE);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, response -> {
            if (response == null) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Couldn't fetch the menu! Pleas try again.", Toast.LENGTH_LONG).show();
                return;
            }

            repoList = new Gson().fromJson(response.toString(), new TypeToken<List<Repo>>() {
            }.getType());
            Log.i("TAG", "onResponse: " + response.toString());
            arrayList.clear();
            arrayList.addAll(repoList);


            adapter.notifyDataSetChanged();

            progressBar.setVisibility(View.GONE);

            saveTask();
        }, error -> {
            progressBar.setVisibility(View.GONE);
            Log.e("TAG", "Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        jsonArrayRequest.setShouldCache(false);
        requestQueue.add(jsonArrayRequest);
    }

    private void saveTask() {

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                for (int i = 0; i < repoList.size(); i++) {
                    API_Data api_data = new API_Data();
                    api_data.setLoginName_(repoList.get(i).getLogin());
                    api_data.setAvatar_url(repoList.get(i).getAvatar_url());
                    Log.i("ajay", "doInBackground: " + repoList.get(i).getAvatar_url());
                    api_data.setType___(repoList.get(i).getType());
//                     api_data.setId(repoList.get(i).getId());

                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().api_dao().insert(api_data);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();


    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        Repo repo = arrayList.get(position);
        detailIntent.putExtra("name", repo.getLogin());
        detailIntent.putExtra("profile", repo.getAvatar_url());
        Log.i("BRUCE", "onItemClick: " + repo.getId() + repo.getLogin());
        startActivity(detailIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.create_menu) {
            Intent intent = new Intent(this, CreateActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }
}
