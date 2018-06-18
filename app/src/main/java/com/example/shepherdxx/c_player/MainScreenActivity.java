package com.example.shepherdxx.c_player;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainScreenActivity extends AppCompatActivity {

    Intent i;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        i=new Intent(this,RadioListActivity.class);
        v=findViewById(R.id.link);
        View.OnClickListener oclik = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(i);
                finish();
            }
        };
        v.setOnClickListener(oclik);
    }
}
