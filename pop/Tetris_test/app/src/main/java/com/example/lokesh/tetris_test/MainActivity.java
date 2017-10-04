package com.example.lokesh.tetris_test;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button Play ,FullScreenPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Play = (Button)findViewById(R.id.buttonPlay);
        FullScreenPlay = (Button)findViewById(R.id.play_full_screen);

        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.gameOver = false;
                Intent intent = new Intent(MainActivity.this,GameActivity.class);
                intent.putExtra("FullScreen","false");
                startActivity(intent);
            }
        });

        FullScreenPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.gameOver = false;
                Intent intent = new Intent(MainActivity.this,GameActivity.class);
                intent.putExtra("FullScreen","true");
                startActivity(intent);
            }
        });

    }



}
