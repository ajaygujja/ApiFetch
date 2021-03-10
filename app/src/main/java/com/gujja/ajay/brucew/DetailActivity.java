package com.gujja.ajay.brucew;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gujja.ajay.brucew.Room.API_Data;
import com.gujja.ajay.brucew.Room.DatabaseClient;
import com.gujja.ajay.brucew.model.Repo;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView avatarImg = findViewById(R.id.avatar_img);
        TextView profile_name = findViewById(R.id.profile_name);
        Button btn = findViewById(R.id.delete_btn);

        Intent intent = getIntent();
        String name_ = intent.getStringExtra("name");
        String avatar_url = intent.getStringExtra("profile");
        Glide.with(DetailActivity.this).load(avatar_url).into(avatarImg);

        profile_name.setText(name_);

        btn.setOnClickListener(view -> {
            Log.i("name of item", "onClick: " + name_ + "____" + avatar_url);

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    List<API_Data> api_dataList = DatabaseClient.getInstance(DetailActivity.this).getAppDatabase().api_dao().getAll();
                    Log.i("Api data", "onClick: " + api_dataList);

                    for (int i = 0; i < api_dataList.size(); i++) {
                        API_Data val = api_dataList.get(i);
                        Log.i("APi ", "run: " + val);

                        if (val.getLoginName_().equals(name_)) {
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    DatabaseClient.getInstance(DetailActivity.this).getAppDatabase().api_dao().deleteword(val);
                                    Log.i("TAG", "run: launch");

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.i("TAG", "run: UI ");
                                            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    Intent intent1 = new Intent(DetailActivity.this, MainActivity.class);
                                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent1);
                                    finish();
                                }
                            });
                            break;
                        }
                    }
                }
            });
            thread.start();
        });
    }
}