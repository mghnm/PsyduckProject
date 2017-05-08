package com.example.mghan.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    RelativeLayout background;
    Bitmap duckMap;
    final static int INTERVAL = 500;
    Random randomColor = new Random();
    int color, counter;
    MediaPlayer[] mediaPlayers = new MediaPlayer[5];

    private void changeColor(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                color = Color.argb(255, randomColor.nextInt(256), randomColor.nextInt(256), randomColor.nextInt(256));
                background.setBackgroundColor(color);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        background = (RelativeLayout) this.findViewById(R.id.activity_main);
        Point size = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(size);
        duckMap = BitmapFactory.decodeResource(getResources(), R.raw.psiduck);
        duckMap = Bitmap.createScaledBitmap(duckMap, (int)(size.x / (2*7.5)), (size.y / (2*10)), true);
        final MediaPlayer psyduck = MediaPlayer.create(this, R.raw.psyduck);
        for(int i = 0; i < 5; i++){
           mediaPlayers[i] = MediaPlayer.create(this, R.raw.psyduck);
        }
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(INTERVAL);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    changeColor();
                }
            }
        }).start();

        background.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
             int x = (int) event.getX();
             int y = (int) event.getY();

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);

                ImageView duckimg = new ImageView(getApplicationContext());
                lp.setMargins(x - (duckMap.getWidth()) ,y - (duckMap.getHeight()),0,0);
                duckimg.setLayoutParams(lp);
                duckimg.setImageBitmap(duckMap);
                ((ViewGroup) v).addView(duckimg);

                mediaPlayers[counter++].start();
                if(counter >= 4){
                    counter = 0;
                }
                return true;
            }
        });
    }
}
