package com.darkmind.partydrinkingcards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    private int COMMON_PERCENT = 40;
    private int RARE_PERCENT = 70;
    private int EPIC_PERCENT = 90;
    private int LEGEND_PERCENT = 40;

    private int adCount = 0;
    private final int ADMAX = 7;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        final DBHelper dbHelper = new DBHelper(this);


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
                            vibrator.vibrate(60);
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


        if(new DBHelper(this).getParameters().sound == 1){
            Intent svc=new Intent(this, BackgroundSoundService.class);
            svc.setAction("PLAY_GAME");
            startService(svc);
        }
    }

    @Override
    public void onBackPressed() {
        if(new DBHelper(this).getParameters().sound == 1){
            Intent svc=new Intent(this, BackgroundSoundService.class);
            svc.setAction("PLAY_NORMAL");
            startService(svc);
        }
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
