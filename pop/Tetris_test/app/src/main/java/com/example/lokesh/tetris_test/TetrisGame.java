package com.example.lokesh.tetris_test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import static com.example.lokesh.tetris_test.Constants.COL;
import static com.example.lokesh.tetris_test.Constants.ROW;
import static com.example.lokesh.tetris_test.Constants.getColor;
import static com.example.lokesh.tetris_test.Constants.inComing;

/**
 * Created by lokesh on 8/9/17.
 */

public class TetrisGame extends SurfaceView implements Runnable {


    //boolean variable to track if the game is playing or not
    boolean playing = false;
    boolean firstTime ;


    public Context context;

    //the game thread
    private Thread gameThread = null;
    private int mX;
    private int mY;



    //These objects will be used for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private State state;
    private Activity activity;

    private  Blocks block = null;



    public TetrisGame(Context context) {
        super(context);
        this.context = context;
    }

    public TetrisGame(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;

    }

    public TetrisGame(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;

    }

    public void intialize(Activity activity, int maxX, int maxY){
        Constants.inComing = false;
        this.activity = activity;
        firstTime = true;
        playing = true;
        mY= maxY;
        mX = maxX;
        setConstants();

        surfaceHolder = getHolder();
        paint = new Paint();

        state = new State(canvas,surfaceHolder,mX,mY);

    }


    public void play(){

        if(Constants.gameOver){
//            Toast.makeText(context,"------GAME OVER------",Toast.LENGTH_LONG).show();
            Log.e("GAME OVER ","GAME OVER");
            Intent intent = new Intent(activity,GameOver.class);
            activity.startActivity(intent);

        }

        if(playing && !Constants.gameOver){

            if(!inComing){
                int k = Constants.getINT();
                switch(k) {
                    case 0 : block = new Blocks.Iblock(state, Constants.getColor());
                        break;
                    case 1 :  block = new Blocks.Oblock(state, Constants.getColor());
                        break;
                    case 2: block = new Blocks.Jblock(state, Constants.getColor());
                        break;
                    case 3: block = new Blocks.Lblock(state, Constants.getColor());
                        break;
                    case 4: block = new Blocks.Sblock(state, Constants.getColor());
                        break;
                    case 5 : block = new Blocks.Zblock(state, Constants.getColor());
                        break;
                    default: block = new Blocks.Zblock(state, Constants.getColor());
                        Log.e("inavlid block ID :"," id = "+k);
                }

                Constants.setInComing(true);

            }
            else{

                if(block != null) {
                    block.moveDown();
                }
                else
                    Log.e("Block is null","inside play");

            }


        }

    }



    @Override
    public void run() {

        while(playing){

//            if(firstTime)
//                initMap();
//            else


                update();

                draw();

                control();


        }

    }

    public void singleTap(){
        if (block!=null){
            block.rotate();
        }
    }

    public void swipedLeft(){
        if(block!=null)
            block.moveLR(Constants.LEFT);
    }

    public void swipedRight(){
        if(block!=null)
            block.moveLR(Constants.RIGHT);
    }

    public void swipedBottom(){

    }


    private void update() {

//        int size =( ROW-1)*COL;
//
//        Box t = state.getBox(ROW-2,COL-1);
//
//        if(t!=null){
//            t.setColor(Constants.getColor());
//        }

  //       iblock.moveDown();

        play();


    }

    private void draw() {


        if (surfaceHolder.getSurface().isValid()) {

            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            paint.setColor(Color.RED);


            for(Box b : state.mList) {
                paint.setColor(b.getColor());
                paint.setStyle(Paint.Style.FILL);
                canvas.drawRect(b.getLeft(), b.getUp(), b.getRight(), b.getBottom(), paint);

            }


            // make the border

            for(Box b : state.mList) {

                if(b.getColor() != Constants.DEFAULT_COLOR){
                    paint.setColor(Color.WHITE);
                    paint.setStrokeWidth(1);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(b.getLeft(), b.getUp(), b.getRight(), b.getBottom(), paint);
                }


              //  b.draw();
            }


            surfaceHolder.unlockCanvasAndPost(canvas);
        }

    }

    private void control() {
        try {
            gameThread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void pause() {
        //when the game is paused
        //setting the variable to false
        playing = false;
        try {
            //stopping the thread
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        //when the game is resumed
        //starting the thread again
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    private void setConstants(){

        Constants.setCOLORS();

        Constants.setCanvas(canvas);
        Constants.setDefaultColor(Color.BLACK);
        Constants.setStrokeColor(Color.WHITE);
        Constants.setStrokeWidth(1);

        Constants.setCOL(11);
        Constants.setROW(20);

        Constants.setmX(mX);
        Constants.setmY(mY);

    }


    public void initMap(){
        Log.e("inside init","  ");
        if(surfaceHolder.getSurface().isValid()) {

            canvas = surfaceHolder.lockCanvas();
//            mX = canvas.getHeight();
//            mY = canvas.getWidth();

            Log.e("Canvas hieght width:",mX+" "+mY+" comp "+canvas.getHeight()+" "+canvas.getWidth());
            surfaceHolder.unlockCanvasAndPost(canvas);
        }

        firstTime = false;
    }


}
