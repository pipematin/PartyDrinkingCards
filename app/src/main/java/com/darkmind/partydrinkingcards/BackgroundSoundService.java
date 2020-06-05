package com.darkmind.partydrinkingcards;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class BackgroundSoundService extends Service {
    public static boolean playing = false;
    MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        player = MediaPlayer.create(this,R.raw.make_it_funky);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);
        try{
            player.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getAction() != null){
            if (intent.getAction().equals("PLAY")) {
                player.start();
                playing = true;
            }
            if (intent.getAction().equals("PAUSE")) {
                player.pause();
                playing = false;
            }

            if (intent.getAction().equals("PLAY_GAME")) {
                player.stop();
                player.release();
                player = MediaPlayer.create(this,R.raw.funky_groove);
                player.setLooping(true); // Set looping
                player.setVolume(100,100);
                try{
                    player.prepare();
                }catch (Exception e){
                    e.printStackTrace();
                }
                player.start();
                playing = true;
            }

            if (intent.getAction().equals("PLAY_NORMAL")) {
                player.stop();
                player.release();
                player = MediaPlayer.create(this,R.raw.make_it_funky);
                player.setLooping(true); // Set looping
                player.setVolume(100,100);
                try{
                    player.prepare();
                }catch (Exception e){
                    e.printStackTrace();
                }
                player.start();
                playing = true;
            }
        }else{
            player.start();
            playing = true;
        }
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        playing = false;
        if(player != null){
            player.stop();
            player.release();
        }

    }
}
