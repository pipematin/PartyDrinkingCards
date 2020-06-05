package com.darkmind.partydrinkingcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Caution extends Activity {
    private TimerThread timer;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        ImageView iv;
        TextView tv;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caution);
        tv = findViewById(R.id.TV_Caution);
        iv = findViewById(R.id.IV_Caution);
        LinearLayout ll = findViewById(R.id.LL_caution);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timer != null && timer.isAlive()){
                    timer.brun = false;
                }
                startActivity(new Intent(getApplicationContext(),StartMenu.class));
            }
        });
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.splash_transition);
        tv.startAnimation(animation);
        iv.startAnimation(animation);

        timer = new TimerThread();
        timer.start();
    }

    private class TimerThread extends Thread{
        public boolean brun = true;

        @Override
        public void run(){
            try{
                sleep(5000);
                if(brun){
                    startActivity(new Intent(getApplicationContext(),StartMenu.class));
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        timer.brun = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        timer.brun = true;
        super.onResume();
    }
}



