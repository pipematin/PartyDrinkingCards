package com.darkmind.partydrinkingcards;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class Extra extends Activity implements RewardedVideoAdListener{

    /*Publicidad */
    private RewardedVideoAd mRewardedVideoAd;
    Button b_extra1;
    Button b_extra2;
    Button b_extra3;
    Button b_extra4;

    int index = 1;
    int i;
    final DBHelper dbHelper = new DBHelper(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Button b_extra;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);
        Resources res = getResources();

        MobileAds.initialize(this,"");
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

        changeTotalCards();

        b_extra1 = findViewById(R.id.b_unlock1);
        b_extra2 = findViewById(R.id.b_unlock2);
        b_extra3 = findViewById(R.id.b_unlock3);
        b_extra4 = findViewById(R.id.b_unlock4);

        for( i = 1; i <= 4; i++){
            switch (i){
                case 1:
                    b_extra = b_extra1;
                    break;
                case 2:
                    b_extra = b_extra2;
                    break;
                case 3:
                    b_extra = b_extra3;
                    break;
                default:
                    b_extra = b_extra4;
                    break;
            }

            setButtonColor(b_extra,i);
        }

        b_extra1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                if(dbHelper.getExtra(false,1) == 0){
                    if (mRewardedVideoAd.isLoaded()) {
                        index = 1;
                        mRewardedVideoAd.show();
                    }
                }else{
                    if(dbHelper.getExtra(true, 1) == 0){
                        dbHelper.setExtra(1,1);
                        setButtonColor(b_extra1,1);
                    }else{
                        dbHelper.setExtra(0,1);
                        setButtonColor(b_extra1,1);
                    }
                }
            }
        });

        b_extra2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                if(dbHelper.getExtra(false,2) == 0){
                    if (mRewardedVideoAd.isLoaded()) {
                        index = 2;
                        mRewardedVideoAd.show();
                    }
                }else{
                    if(dbHelper.getExtra(true, 2) == 0){
                        dbHelper.setExtra(1,2);
                        setButtonColor(b_extra2,2);
                    }else{
                        dbHelper.setExtra(0,2);
                        setButtonColor(b_extra2,2);
                    }
                }
            }
        });

        b_extra3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                if(dbHelper.getExtra(false,3) == 0){
                    if (mRewardedVideoAd.isLoaded()) {
                        index = 3;
                        mRewardedVideoAd.show();
                    }
                }else{
                    if(dbHelper.getExtra(true, 3) == 0){
                        dbHelper.setExtra(1,3);
                        setButtonColor(b_extra3,3);
                    }else{
                        dbHelper.setExtra(0,3);
                        setButtonColor(b_extra3,3);
                    }
                }
            }
        });

        b_extra4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                if(dbHelper.getExtra(false,4) == 0){
                    if (mRewardedVideoAd.isLoaded()) {
                        index = 4;
                        mRewardedVideoAd.show();
                    }
                }else{
                    if(dbHelper.getExtra(true, 4) == 0){
                        dbHelper.setExtra(1,4);
                        setButtonColor(b_extra4,4);
                    }else{
                        dbHelper.setExtra(0,4);
                        setButtonColor(b_extra4,4);
                    }
                }
            }
        });


        /*
        Button b_reverse = findViewById(R.id.b_reverse);
        b_reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.reverseExtra();
                onCreate(new Bundle());
            }
        });
        */
    }

    private void setButtonColor(Button b_extra,int i){
        Resources res = getResources();

        if(dbHelper.getExtra(false, i) == 1){
            if(dbHelper.getExtra(true, i) == 0){
                b_extra.setText(R.string.disabled);
                b_extra.setBackground(res.getDrawable(R.drawable.button_red));
            }else{
                b_extra.setText(R.string.enabled);
                b_extra.setBackground(res.getDrawable(R.drawable.button_shape_green));
            }
        }else{
            if(!mRewardedVideoAd.isLoaded()){
                b_extra.setText(R.string.loading);
                b_extra.setBackground(res.getDrawable(R.drawable.button_transparent));
                b_extra.setTextColor(res.getColor(R.color.darkRed));
            }else{
                b_extra.setText(R.string.unlock);
                b_extra.setBackground(res.getDrawable(R.drawable.button_shape_green));
                b_extra.setTextColor(res.getColor(R.color.darkYellow));
            }
        }

        changeTotalCards();
    }

    private void changeTotalCards(){
        String totalCards = getResources().getString(R.string.totalCards,dbHelper.numberOfCards());
        TextView tv_cards = findViewById(R.id.numberCards);
        tv_cards.setText(totalCards);
    }

    private void loadRewardedVideoAd() {
        Publicity publicity = new Publicity();
        mRewardedVideoAd.loadAd(publicity.getVideo(),
                new AdRequest.Builder().build());
    }

    @Override
    protected void onResume() {
        mRewardedVideoAd.resume(this);

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
        mRewardedVideoAd.pause(this);
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
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        if(new DBHelper(this).getParameters().sound == 1){
            stopService(new Intent(this,BackgroundSoundService.class));
        }
        super.onDestroy();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        dbHelper.unlockExtra(index);
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
       // Toast.makeText(this, "onRewardedVideoAdLeftApplication",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        onCreate(new Bundle());
        //Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        loadRewardedVideoAd();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Button b_extra;
        for(i = 1; i <= 4; i++ ){
            switch (i){
                case 1:
                    b_extra = b_extra1;
                    break;
                case 2:
                    b_extra = b_extra2;
                    break;
                case 3:
                    b_extra = b_extra3;
                    break;
                default:
                    b_extra = b_extra4;
                    break;
            }

            if(dbHelper.getExtra(false,i) == 0) {
                b_extra.setText(R.string.unlock);
                b_extra.setBackground(getResources().getDrawable(R.drawable.button_transparent_complete));
                b_extra.setTextColor(getResources().getColor(R.color.darkRed));
            }
        }

    }

    @Override
    public void onRewardedVideoAdOpened() {
        //Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        //Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {
        //Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
    }
}
