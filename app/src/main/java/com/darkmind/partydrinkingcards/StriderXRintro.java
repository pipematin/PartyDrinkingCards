package com.darkmind.partydrinkingcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class StriderXRintro extends Activity {
    private Thread timer;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        ImageView iv;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_striderxr_intro);
        iv = findViewById(R.id.IV_Strider);
        LinearLayout ll = findViewById(R.id.LL_striderXR);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.interrupt();
            }
        });
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.striderxr_transition);
        iv.startAnimation(animation);
        final Intent i = new Intent(this,Caution.class);
        timer = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch (InterruptedException e){
                    finish();
                    startActivity(i);
                }finally {
                    finish();
                    startActivity(i);
                }
            }
        };
        timer.start();
    }
}
