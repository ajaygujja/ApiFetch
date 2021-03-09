package com.gujja.ajay.brucew;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gujja.ajay.brucew.Room.API_Dao;
import com.gujja.ajay.brucew.Room.API_Data;
import com.gujja.ajay.brucew.Room.DatabaseClient;
import com.gujja.ajay.brucew.model.Repo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

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

        adapter = new RecycleViewAdapter(this,arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && arrayList != null) {
            fetchfromServer();
        } else {
            fetchfromRoom();
        }
    }

    private void fetchfromRoom() {
        Log.i("ajajaja", "fetchfromRoom: started");
        Thread thread = new Thread(() -> {
            List<API_Data> api_dataList = DatabaseClient.getInstance(MainActivity.this).getAppDatabase().api_dao().getAll();
            arrayList.clear();
            Log.i("nhush", "fetchfromRoom: "+ api_dataList);
            for (API_Data val: api_dataList){
                Repo repo = new Repo(val.getId(),val.getLoginName_(),val.getAvatar_url(),val.getType___());
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

/*            try {
                JSONArray jsonArray = new JSONArray(response);
                Log.i("ajayajajja", "fetchfromServer: "+ jsonArray);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.getJSONObject(i);
                    Log.i("ajayajajja", "fetchfromServer: "+ data);
                    API_Data api_data = new API_Data();
                    api_data.setLoginName_(data.getString("login"));
                    api_data.setAvatar_url(data.getString("avatar_url"));
                    api_data.setType___(data.getString("type"));
                    api_data.setId(data.getInt("id"));

                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().api_dao().insert(api_data);


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }*/


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

/*        for (int i = 0; i <= repoList.size(); i++){
            API_Data api_data = new API_Data();
            api_data.setLoginName_(repoList.get(i).getLogin());
            api_data.setAvatar_url(repoList.get(i).getAvatar_url());
            Log.i("ajay", "doInBackground: " + repoList.get(i).getAvatar_url());
            api_data.setType___(repoList.get(i).getType());
            api_data.setId(repoList.get(i).getId());

            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().api_dao().insert(api_data);

        }*/
         class SaveTask extends AsyncTask<Void,Void,Void> {

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
}
