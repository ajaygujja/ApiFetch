package com.gujja.ajay.brucew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {
    private ImageView avatarImg;
    private TextView profile_name;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        avatarImg = findViewById(R.id.avatar_img);
        profile_name = findViewById(R.id.profile_name);
        btn = findViewById(R.id.delete_btn);

        Intent intent = getIntent();
        String name_ = intent.getStringExtra("name");
        String avatar_url = intent.getStringExtra("profile");
        Glide.with(DetailActivity.this).load(avatar_url).into(avatarImg);
        profile_name.setText(name_);
    }
}