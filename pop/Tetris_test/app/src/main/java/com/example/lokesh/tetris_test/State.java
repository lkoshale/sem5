package com.example.lokesh.tetris_test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lokesh on 8/9/17.
 */

public class State {

    private int mX;
    private int mY;

    private SurfaceHolder surface;
    private Canvas canvas;

    private int COL = Constants.COL;
    private int ROW = Constants.ROW;

    private int Rsize;
    private int Csize;

    private final int SIZE = COL*(ROW-1);

    public List<Box> mList = new ArrayList<>();



    public  State(Canvas canvas,SurfaceHolder surface,int X,int Y){
        this.canvas = canvas;
        this.surface = surface;
        this.mY = Y;
        this.mX = X;

//        this.Row =  R;
//        this.Col = C;
        getDim();
        makeGrid();
    }

    public void getDim(){
        Csize = (mX)/COL;
        Rsize = (mY)/ROW;
        Log.e("box size :",Rsize+" "+Csize);
    }



    public void makeGrid(){

        int u = 0;
        int b = u+Rsize;
        int l;
        int r ;

        for(int j=0;j<ROW-1;j++) {

            l = 0;
            r = l+Csize;

            for (int i = 0; i < COL; i++) {
                if(i==COL-1) r=r-1;
                Box box = new Box(l, u, r, b,j,i);
                // box.setColor(Color.YELLOW);
                mList.add(box);
                l = r + 1;
                r = l + Csize;
            }
            u = b + 1;
            b = u + Rsize;
        }

    }

    public Box getBox(int r,int c){
        int k = r*COL + c ;

        if( k<SIZE){
            return mList.get(k);
        }
        else{
            Log.e("IN STATE","Index Out of bound for grid");
        }

        return null;
    }


}
