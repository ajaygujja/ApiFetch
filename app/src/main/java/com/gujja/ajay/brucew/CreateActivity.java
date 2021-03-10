package com.gujja.ajay.brucew;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gujja.ajay.brucew.Room.API_Data;
import com.gujja.ajay.brucew.Room.DatabaseClient;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;

public class CreateActivity extends AppCompatActivity {

    private EditText name, type;
    private Button create_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        name = findViewById(R.id.Create_name);
        type = findViewById(R.id.Create_type);
        create_btn = findViewById(R.id.create_button);

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread =  new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String ajay = name.getText().toString();
                        String val = type.getText().toString();

                        API_Data api_data = new API_Data();
                        api_data.setLoginName_(ajay);
                        api_data.setAvatar_url("https://avatars.githubusercontent.com/u/3?v=4");
                        api_data.setType___(val);

                        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().api_dao().insert(api_data);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "EXECUTED", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                thread.start();

                Intent intent1 = new Intent(CreateActivity.this, MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                finish();

            }
        });

    }

    public Uri getImage(String imageName){
        String CompletPath = Environment.getExternalStorageDirectory() + "/" + imageName;
        File file = new File(CompletPath);
        Uri uri = Uri.fromFile(file);
        return uri;
    }
}