package com.darkmind.partydrinkingcards;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

public class Bonus extends Activity {

    private MediaPlayer mp = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus);

        play_sound();

        ImageView imageView = findViewById(R.id.IV_vlad);
        Glide.with(this).load(R.drawable.gif_russian_dance).into(imageView);

        DBHelper dbHelper = new DBHelper(this);
        String player = dbHelper.getRandomPlayer();
        String vlad_string = getResources().getString(R.string.vlad_visit, player);
        TextView tv = findViewById(R.id.Tv_Bonus);
        tv.setText(vlad_string);

        Button btn_bonus = findViewById(R.id.Btn_bonus);
        btn_bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });
    }

    public void startGame(){
        Intent i = new Intent(this,Game.class);
        i.putExtra(Game.GAME_TYPE, i.getIntExtra(Game.GAME_TYPE, Game.GAME_TYPE_FAST));
        i.putExtra(Game.GAME_STARTED, true);
        startActivity(i);
    }

    private void play_sound(){
        mp = MediaPlayer.create(this, R.raw.cheeki_breeki2);
        mp.setVolume(100,100);
        try{
            mp.setLooping(true);
            mp.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        if(mp != null && mp.isPlaying()){
            mp.stop();
        }
        super.onPause();
    }
}
