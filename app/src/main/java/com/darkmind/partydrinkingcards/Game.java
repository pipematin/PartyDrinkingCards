package com.darkmind.partydrinkingcards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Game extends Activity {

    private TextView tv_players;
    private TextView tv_text;
    private TextView tv_type;
    public static final String GAME_TYPE = "game";
    public static final int GAME_TYPE_NORMAL = 1;
    public static final int GAME_TYPE_FAST = 2;
    private final int LEGENDARY_START = 7;
    private int card_count = 0;
    private boolean sound = false;

    private int COMMON_PERCENT = 35;
    private int RARE_PERCENT = 65;
    private int EPIC_PERCENT = 90;

    private ArrayList<Integer> sound_effects = new ArrayList<>();
    private MediaPlayer mp = null;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        final DBHelper dbHelper = new DBHelper(this);

        if (dbHelper.getParameters().sound == 1){
            fill_sound_effects();
            this.sound = true;
        }


        Button btn_nextCard = findViewById(R.id.Btn_nextCard);
        btn_nextCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DBHelper.ParameterReturn parameterReturn = dbHelper.getParameters();
                if(parameterReturn.vibration == 1){
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if(vibrator != null){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(60, VibrationEffect.DEFAULT_AMPLITUDE));
                        }else{
                            //deprecated in API 26
                            vibrator.vibrate(30);
                        }
                    }
                }

                nextCard();
            }
        });

        tv_players = findViewById(R.id.TV_Players);
        tv_text = findViewById(R.id.TV_Text);
        tv_type = findViewById(R.id.TV_Type);

        tv_type.setText(R.string.WelcomeType);
        tv_players.setText(R.string.WelcomePlayers);
        tv_text.setText(R.string.WelcomeText);

        TextView player_game = findViewById(R.id.TV_Game_players);
        TextView challenge_game = findViewById(R.id.TV_Game_challenge);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/brokenwings.ttf");
        tv_type.setTypeface(typeface);
        player_game.setTypeface(typeface);
        challenge_game.setTypeface(typeface);


    }
    private void fill_sound_effects(){
        sound_effects.add(R.raw.blyamp3);
        sound_effects.add(R.raw.bully);
        sound_effects.add(R.raw.cyka);
        sound_effects.add(R.raw.fuck);
        sound_effects.add(R.raw.fuckers);
        sound_effects.add(R.raw.jhonny);
        sound_effects.add(R.raw.to_be_continued);
        sound_effects.add(R.raw.why_are_you_running);
        sound_effects.add(R.raw.yeah_boy);
        sound_effects.add(R.raw.swamp);
        sound_effects.add(R.raw.deja_vu);
        sound_effects.add(R.raw.credits);
        sound_effects.add(R.raw.nani);
        sound_effects.add(R.raw.coda);
        sound_effects.add(R.raw.coffin);
        sound_effects.add(R.raw.wow);
        sound_effects.add(R.raw.bruh);
        sound_effects.add(R.raw.come_here_boy);
        sound_effects.add(R.raw.got_you_homie);
        sound_effects.add(R.raw.ricardo);
        sound_effects.add(R.raw.giorno_piano);
        sound_effects.add(R.raw.sad_trombone);
        sound_effects.add(R.raw.jeff);
        sound_effects.add(R.raw.ph_intro);
        sound_effects.add(R.raw.shut_up);
        sound_effects.add(R.raw.turn_down_for_what);
        sound_effects.add(R.raw.nooooo);
        sound_effects.add(R.raw.cr7);
        sound_effects.add(R.raw.leeroy);
        sound_effects.add(R.raw.sad_violin);
        sound_effects.add(R.raw.titanic);
        sound_effects.add(R.raw.fortnite);
        sound_effects.add(R.raw.flea);
        sound_effects.add(R.raw.hahahahha);
        sound_effects.add(R.raw.mariano);
        sound_effects.add(R.raw.pentakill);
        sound_effects.add(R.raw.avengers);
        sound_effects.add(R.raw.sasha);
    }

    private void playSoundEffect(){
        int i = DBHelper.getRandomNum(0, sound_effects.size()-1);
        int sound = sound_effects.get(i);
        System.out.println("i: " + i + "\nID: "+ sound);

        mp = MediaPlayer.create(this, sound);
        mp.setVolume(100,100);
        try{
            mp.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void nextCard(){
        final DBHelper.DataReturn dataReturn;

        dataReturn = getCardByOptions();
        if(dataReturn == null) return;

        tv_players = findViewById(R.id.TV_Players);
        tv_text = findViewById(R.id.TV_Text);
        tv_type = findViewById(R.id.TV_Type);

        tv_type.setText("");
        tv_players.setText("");
        tv_text.setText("");

        String type = getStringType(dataReturn.type,tv_type);
        tv_type.setText(type);


        if( mp != null){
            mp.stop();
        }
        if(dataReturn.type == Card.LEGENDARY_INDEX && this.sound ){
            System.out.println("PLAYING SOUND");
            playSoundEffect();
        }

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.card_transition);
        final Animation animation1 = AnimationUtils.loadAnimation(this,R.anim.card_transition);
        final Animation animation2 = AnimationUtils.loadAnimation(this,R.anim.card_transition);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(dataReturn.numPlayers == 0){
                    tv_players.setText(R.string.cardForEveryone);
                }else{
                    Resources res = getResources();
                    String players = "";
                    for(int i = 0; i < dataReturn.numPlayers; i++){
                        if(i == dataReturn.numPlayers -1) players += dataReturn.players.get(i);
                        else if(i == dataReturn.numPlayers -2) players += dataReturn.players.get(i) + " "
                                + res.getString(R.string.and) + " ";
                        else{
                            players += dataReturn.players.get(i) + ", ";
                        }
                    }
                    tv_players.setText(players);
                }
                tv_players.startAnimation(animation1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tv_text.setText(dataReturn.text);
                tv_text.startAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        ImageView img = findViewById(R.id.img_gema);
        switch (dataReturn.type){
            case Card.COMMON_INDEX:
                img.setBackground(getResources().getDrawable(R.drawable.gema_comun));
                break;
            case Card.RARE_INDEX:
                img.setBackground(getResources().getDrawable(R.drawable.gema_rara));
                break;
            case Card.EPIC_INDEX:
                img.setBackground(getResources().getDrawable(R.drawable.gema_epica));
                break;
            case Card.LEGENDARY_INDEX:
                img.setBackground(getResources().getDrawable(R.drawable.gema_legendaria));
                break;
            default:
                img.setBackground(null);
                break;
        }

        img.startAnimation(animation2);
        tv_type.startAnimation(animation);
    }

    private DBHelper.DataReturn getCardByOptions(){
        DBHelper dbHelper = new DBHelper(this);
        DBHelper.ParameterReturn parameterReturn;
        parameterReturn = dbHelper.getParameters();
        int min = 1;
        int max = 100;
        int randomNum =  min + (int)(Math.random() * ((max - min) + 1));
        int maxplayers;
        DBHelper.DataReturn dataReturn;

        Intent i = getIntent();
        int mode = i.getIntExtra(GAME_TYPE,GAME_TYPE_FAST);
        if(mode == GAME_TYPE_FAST){
            maxplayers = 0;
        }else{
            maxplayers = dbHelper.numberOfPlayers();
        }

        ArrayList<Integer> set = new ArrayList<>();
        if(parameterReturn.common == 1) set.add(Card.COMMON_INDEX);
        if(parameterReturn.rare == 1) set.add(Card.RARE_INDEX);
        if(parameterReturn.epic == 1) set.add(Card.EPIC_INDEX);
        if(parameterReturn.legendary == 1) set.add(Card.LEGENDARY_INDEX);

        if(set.size() == 4){
            card_count++;
            if(randomNum < COMMON_PERCENT){
                dataReturn = dbHelper.getCard(set.get(0),maxplayers);
            }else if(randomNum < RARE_PERCENT){
                dataReturn = dbHelper.getCard(set.get(1),maxplayers);
            }else if(randomNum < EPIC_PERCENT){
                dataReturn = dbHelper.getCard(set.get(2),maxplayers);
            }else{
                if(card_count < LEGENDARY_START) {
                    dataReturn = dbHelper.getCard(set.get(0),maxplayers);
                }
                else dataReturn = dbHelper.getCard(set.get(3),maxplayers);
            }
        }else if(set.size() == 3){
            if(randomNum < 45){
                dataReturn = dbHelper.getCard(set.get(0),maxplayers);
            }else if(randomNum < 75){
                dataReturn = dbHelper.getCard(set.get(1),maxplayers);
            }else{
                dataReturn = dbHelper.getCard(set.get(2),maxplayers);
            }
        }else if(set.size() == 2){
            if(randomNum < 60){
                dataReturn = dbHelper.getCard(set.get(0),maxplayers);
            }else{
                dataReturn = dbHelper.getCard(set.get(1),maxplayers);
            }
        }else{
            dataReturn = dbHelper.getCard(set.get(0),maxplayers);
        }

        return dataReturn;
    }

    public String getStringType(int id,TextView tv){
        Resources res = getResources();
        switch(id){
            case Card.COMMON_INDEX:
                tv.setTextColor(res.getColor(R.color.grey));
                return res.getString(R.string.Common);
            case Card.RARE_INDEX:
                tv.setTextColor(res.getColor(R.color.blue));
                return res.getString(R.string.Rare);
            case Card.EPIC_INDEX:
                tv.setTextColor(res.getColor(R.color.purple));
                return res.getString(R.string.Epic);
            case Card.LEGENDARY_INDEX:
                tv.setTextColor(res.getColor(R.color.golden));
                return res.getString(R.string.Legendary);
        }

        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
