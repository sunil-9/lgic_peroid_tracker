package com.example.periodtracker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.periodtracker.R;

public class GetLocationActivity extends AppCompatActivity {
EditText longitude,latitude;
String txt_long,txt_lat,url;

Button showMap;
Intent intent=null, chooser =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
        longitude=findViewById(R.id.et_lon);
        latitude=findViewById(R.id.et_lat);
       showMap=findViewById(R.id.show_map);
       showMap.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               txt_lat = latitude.getText().toString().trim();
               txt_long = longitude.getText().toString().trim();
               if (TextUtils.isEmpty(txt_lat) || TextUtils.isEmpty(txt_long)){
                   Toast.makeText(GetLocationActivity.this, "please enter details", Toast.LENGTH_SHORT).show();
               }else{
                   url ="geo:"+txt_lat+","+txt_long;
                   intent = new Intent(Intent.ACTION_VIEW);
                   intent.setData(Uri.parse(url));
                   chooser=Intent.createChooser(intent,"Launch map");
                   startActivity(intent);
               }
           }
       });

    }
}