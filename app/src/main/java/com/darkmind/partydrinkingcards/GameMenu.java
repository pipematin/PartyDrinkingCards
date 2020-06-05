package com.darkmind.partydrinkingcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameMenu extends Activity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);

        Button b_nm = findViewById(R.id.b_NGPlayers);
        b_nm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlayerEditor();
            }
        });

        Button b_fm = findViewById(R.id.b_NGFast);
        b_fm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFastMode();
            }
        });
    }

    public void startPlayerEditor(){
        Intent i = new Intent(this,PlayerEditor.class);
        startActivity(i);
    }

    public void startFastMode(){
        Intent i = new Intent(this,Game.class);
        i.putExtra(Game.GAME_TYPE,Game.GAME_TYPE_FAST);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        if(new DBHelper(this).getParameters().sound == 1){
            if( !BackgroundSoundService.playing){
                Intent svc=new Intent(this, BackgroundSoundService.class);
                svc.setAction("PLAY");
                startService(svc);
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if(new DBHelper(this).getParameters().sound == 1){
            if(BackgroundSoundService.playing){
                Intent svc=new Intent(this, BackgroundSoundService.class);
                svc.setAction("PAUSE");
                startService(svc);
            }
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if(new DBHelper(this).getParameters().sound == 1){
            stopService(new Intent(this,BackgroundSoundService.class));
        }
        super.onDestroy();
    }

}
