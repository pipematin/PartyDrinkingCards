package com.darkmind.partydrinkingcards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

public class StartMenu extends Activity{
    private Context mContext;
    private PopupWindow mPopupWindow;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate( Bundle savedInstanceState) {

        Button b_ng;
        Button b_options;
        Resources res = getResources();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        mContext = getApplicationContext();
        mRelativeLayout = findViewById(R.id.RL_StartMenu);

        b_ng = findViewById(R.id.b_ng);
        b_ng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        b_options = findViewById(R.id.b_options);
        b_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOptions();
            }
        });

//        Button b_extra = findViewById(R.id.b_extra);
//        b_extra.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),Extra.class));
//            }
//        });

        ImageButton imageButton = findViewById(R.id.tutorial_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopUp();
            }
        });

        /* -------------Database---------------*/

//        TextView tv_options = findViewById(R.id.Tv_StartMenu);
//        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/brokenwings.ttf");
//        tv_options.setTypeface(typeface);

        DBHelper dbHelper = new DBHelper(this);

        String totalCards = res.getString(R.string.totalCards,dbHelper.numberOfCards());
        TextView tv_cards = findViewById(R.id.numberCards);
        tv_cards.setText(totalCards);


        if(dbHelper.getParameters().sound == 1){
            Intent svc = new Intent(this, BackgroundSoundService.class);
            startService(svc); //OR stopService(svc);
        }
    }

    public void setLocale(String lang, int val, DBHelper dbHelper) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        dbHelper.setParameter(7,val);
        Intent refresh = new Intent(this, StartMenu.class);
        finish();
        startActivity(refresh);
    }

    public void startGame(){
        Intent i = new Intent(this,GameMenu.class);
        startActivity(i);
    }

    public void startOptions(){
        Intent i = new Intent(this,Options.class);
        startActivity(i);
    }

    public void createPopUp(){
        View temp;
        // Initialize a new instance of LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        ViewGroup parent = findViewById(R.id.RL_StartMenu);
        if(inflater != null){
            temp = inflater.inflate(R.layout.popup_tutorial,parent,false);
        }else return;

        final View customView = temp;

        // Initialize a new instance of popup window
        mPopupWindow = new PopupWindow(customView,
                RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);

        // Set an elevation value for popup window
        // Call requires API level 21
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }

        // Get a reference for the custom view close button
        ImageButton closeButton = customView.findViewById(R.id.ib_close);

        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER_HORIZONTAL,0,-500);
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

        String totalCards = getResources().getString(R.string.totalCards,new DBHelper(this).numberOfCards());
        TextView tv_cards = findViewById(R.id.numberCards);
        tv_cards.setText(totalCards);
        super.onResume();
    }

    @Override
    protected void onPause() {
        if(new DBHelper(this).getParameters().sound == 1) {
            if (BackgroundSoundService.playing) {
                Intent svc = new Intent(this, BackgroundSoundService.class);
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
