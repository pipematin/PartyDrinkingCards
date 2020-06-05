package com.darkmind.partydrinkingcards;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Options extends Activity {

    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RadioButton RB_sound_yes;
        RadioButton RB_sound_no;
        RadioButton RB_vibration_yes;
        RadioButton RB_vibration_no;
        CheckBox common;
        CheckBox rare;
        CheckBox epic;
        CheckBox legendary;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        dbHelper = new DBHelper(this);
        DBHelper.ParameterReturn parameterReturn;

        parameterReturn = dbHelper.getParameters();

        TextView tv_options = findViewById(R.id.Img_Options);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/brokenwings.ttf");
        tv_options.setTypeface(typeface);

        RB_sound_yes = findViewById(R.id.RB_Sound_Yes);
        RB_sound_no = findViewById(R.id.RB_Sound_No);
        if (parameterReturn.sound == 1) {
            RB_sound_yes.setChecked(true);
            RB_sound_no.setChecked(false);
        } else {
            RB_sound_yes.setChecked(false);
            RB_sound_no.setChecked(true);
        }

        RB_vibration_yes = findViewById(R.id.RB_Vibration_Yes);
        RB_vibration_no = findViewById(R.id.RB_Vibration_No);
        if (parameterReturn.vibration == 1) {
            RB_vibration_yes.setChecked(true);
            RB_vibration_no.setChecked(false);
        } else {
            RB_vibration_yes.setChecked(false);
            RB_vibration_no.setChecked(true);
        }

        common = findViewById(R.id.CB_common);
        rare = findViewById(R.id.CB_rare);
        epic = findViewById(R.id.CB_epic);
        legendary = findViewById(R.id.CB_legendary);

        if (parameterReturn.common == 1) common.setChecked(true);
        else common.setChecked(false);
        if (parameterReturn.rare == 1) rare.setChecked(true);
        else rare.setChecked(false);
        if (parameterReturn.epic == 1) epic.setChecked(true);
        else epic.setChecked(false);
        if (parameterReturn.legendary == 1) legendary.setChecked(true);
        else legendary.setChecked(false);
    }

    public void onRBSoundClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        dbHelper = new DBHelper(this);

        switch (view.getId()) {
            case R.id.RB_Sound_Yes:
                if (checked) {
                    dbHelper.setParameter(1, 1);
                    if (!BackgroundSoundService.playing) {
                        Intent svc = new Intent(this, BackgroundSoundService.class);
                        startService(svc);
                    }
                }
                break;
            case R.id.RB_Sound_No:
                if (checked) {
                    dbHelper.setParameter(1, 0);
                    if (BackgroundSoundService.playing) {
                        Intent svc = new Intent(this, BackgroundSoundService.class);
                        stopService(svc);
                    }
                }
                break;
        }
    }

    public void onRBVibrationClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        dbHelper = new DBHelper(this);

        switch (view.getId()) {
            case R.id.RB_Vibration_Yes:
                if (checked) {
                    dbHelper.setParameter(2, 1);
                }
                break;
            case R.id.RB_Vibration_No:
                if (checked) {
                    dbHelper.setParameter(2, 0);
                }
                break;
        }
    }


    public void onCBcliked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        dbHelper = new DBHelper(this);
        DBHelper.ParameterReturn parameterReturn = dbHelper.getParameters();

        switch (view.getId()) {
            case R.id.CB_common:
                if (checked) {
                    dbHelper.setParameter(3, 1);
                } else {
                    if (parameterReturn.rare == 0 && parameterReturn.epic == 0
                            && parameterReturn.legendary == 0) {
                        ((CheckBox) view).setChecked(true);
                        Toast.makeText(this, R.string.CB_warning, Toast.LENGTH_SHORT).show();
                    } else {
                        dbHelper.setParameter(3, 0);
                    }
                }
                break;
            case R.id.CB_rare:
                if (checked) {
                    dbHelper.setParameter(4, 1);
                } else {
                    if (parameterReturn.common == 0 && parameterReturn.epic == 0
                            && parameterReturn.legendary == 0) {
                        ((CheckBox) view).setChecked(true);
                        Toast.makeText(this, R.string.CB_warning, Toast.LENGTH_SHORT).show();
                    } else {
                        dbHelper.setParameter(4, 0);
                    }
                }
                break;
            case R.id.CB_epic:
                if (checked) {
                    dbHelper.setParameter(5, 1);
                } else {
                    if (parameterReturn.rare == 0 && parameterReturn.common == 0
                            && parameterReturn.legendary == 0) {
                        ((CheckBox) view).setChecked(true);
                        Toast.makeText(this, R.string.CB_warning, Toast.LENGTH_SHORT).show();
                    } else {
                        dbHelper.setParameter(5, 0);
                    }
                }
                break;
            case R.id.CB_legendary:
                if (checked) {
                    dbHelper.setParameter(6, 1);
                } else {
                    if (parameterReturn.rare == 0 && parameterReturn.epic == 0
                            && parameterReturn.common == 0) {
                        ((CheckBox) view).setChecked(true);
                        Toast.makeText(this, R.string.CB_warning, Toast.LENGTH_SHORT).show();
                    } else {
                        dbHelper.setParameter(6, 0);
                    }
                }
                break;
        }
    }

    public void onRBLanguageClicked(View view) {
        switch (view.getId()) {
            case R.id.RB_language_en:
                setLocale("en");
                break;
            case R.id.RB_language_es:
                setLocale("es");
                break;
        }
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, Options.class);
        finish();
        startActivity(refresh);
    }

    @Override
    protected void onResume() {
        if (new DBHelper(this).getParameters().sound == 1) {
            if (!BackgroundSoundService.playing) {
                Intent svc = new Intent(this, BackgroundSoundService.class);
                svc.setAction("PLAY");
                startService(svc);
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (new DBHelper(this).getParameters().sound == 1) {
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
        if (new DBHelper(this).getParameters().sound == 1) {
            stopService(new Intent(this, BackgroundSoundService.class));
        }
        super.onDestroy();
    }

}
