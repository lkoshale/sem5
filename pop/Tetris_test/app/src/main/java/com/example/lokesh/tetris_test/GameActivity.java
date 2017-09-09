package com.example.lokesh.tetris_test;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {


    private TetrisGame game ;
    Button Left, Right, Rotate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();

        //Getting the screen resolution into point object
        Point size = new Point();
        display.getSize(size);

        setContentView(R.layout.activity_game);

        game = (TetrisGame) findViewById(R.id.surface);
        game.intialize(GameActivity.this,size.x,size.y);

        Left = (Button)findViewById(R.id.butoonLeft);
        Right = (Button)findViewById(R.id.buttonRight);
        Rotate = (Button)findViewById(R.id.butoonRot);


        game.setOnTouchListener(new OnSwipeTouchListener(GameActivity.this){

            public void onSwipeRight() {
                game.swipedRight();
             //   Toast.makeText(GameActivity.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                game.swipedLeft();
               // Toast.makeText(GameActivity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                game.swipedBottom();
             //   Toast.makeText(GameActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

            public void singleTap(){
                game.singleTap();
              //  Toast.makeText(GameActivity.this, "Tap", Toast.LENGTH_SHORT).show();
            }

        });

        Left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.swipedLeft();
            }
        });

        Right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.swipedRight();
            }
        });

        Rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.singleTap();
            }
        });


        Log.e("GAME : size ",size.x+" "+size.y);


    }


    @Override
    protected void onPause() {
        super.onPause();
        game.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        game.resume();
    }



}
