package com.darkmind.partydrinkingcards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class PlayerEditor extends Activity {
    DBHelper dbHelper = new DBHelper(this);
    private Context mContext;
    private RelativeLayout mRelativeLayout;

    Button btn_add;
    private PopupWindow mPopupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_editor);


        mContext = getApplicationContext();
        mRelativeLayout = findViewById(R.id.RL_PLayerEditor);

        btn_add = findViewById(R.id.Btn_addPlayerNew);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numPlayers =  dbHelper.numberOfPlayers();
                if(numPlayers >= Player.MAX_PLAYERS) {
                    Resources res = getResources();
                    String limit = res.getString(R.string.playerLimit,Player.MAX_PLAYERS);
                    Toast.makeText(getApplicationContext(),limit,Toast.LENGTH_SHORT).show();
                    return;
                }
                createPopUp();
            }
        });

        //Parte de database y players
        crearLista();

        Button btn_start = findViewById(R.id.Btn_startGame);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbHelper.numberOfPlayers() < 1) {
                    Resources res = getResources();
                    Toast.makeText(getApplicationContext(),res.getText(R.string.limitPlayerStart),Toast.LENGTH_SHORT).show();
                    return;
                }
                startGame();
            }
        });

    }

    public void startGame(){
        Intent i = new Intent(this,Game.class);
        i.putExtra(Game.GAME_TYPE,Game.GAME_TYPE_FAST);
        i.putExtra(Game.GAME_STARTED, false);
        startActivity(i);
    }

    public void createPopUp(){
        View temp;
        // Initialize a new instance of LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        ViewGroup parent = findViewById(R.id.RL_PLayerEditor);
        if(inflater != null){
            temp = inflater.inflate(R.layout.popup_insert_name,parent,false);
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

        final EditText editText = customView.findViewById(R.id.et_addPlayer);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editText.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager inputMethodManager= (InputMethodManager) PlayerEditor.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        if(inputMethodManager != null) inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
        editText.requestFocus();

        final Button btn_confirm = customView.findViewById(R.id.Btn_confirmAddPlayer);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_addPlayer = customView.findViewById(R.id.et_addPlayer);
                String name = et_addPlayer.getText().toString();
                if(!name.isEmpty()){
                    Player player = new Player(name);
                    addPlayer(player);
                }

                mPopupWindow.dismiss();
            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    EditText et_addPlayer = customView.findViewById(R.id.et_addPlayer);
                    String name = et_addPlayer.getText().toString();
                    if(!name.isEmpty()){
                        Player player = new Player(name);
                        addPlayer(player);
                    }

                    mPopupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });



        mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER_HORIZONTAL,0,-500);
    }

    public void crearLista(){
        ArrayList<String> arrayList = dbHelper.getPlayers();
        String[] players = new String[arrayList.size()];
        players = arrayList.toArray(players);

        final ListView listView = findViewById(R.id.lv_playerEditor);
        PeListAdapter arrayAdapter = new PeListAdapter(this,players);
        listView.setAdapter(arrayAdapter);
        listView.setSelection(arrayAdapter.getCount());
        Button b = findViewById(R.id.Btn_addPlayerNew);
        if(dbHelper.numberOfPlayers() == Player.MAX_PLAYERS)b.setVisibility(View.GONE);
        else b.setVisibility(View.VISIBLE);
    }
    public void addPlayer(Player player){
        dbHelper.insertPlayer(player);
        crearLista();
    }

    public class PeListAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;
        private final DBHelper dbHelper;

        private PeListAdapter(Context context, String[] values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
            this.dbHelper = new DBHelper(getContext());
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(inflater != null){
                convertView = inflater.inflate(R.layout.list_item, parent, false);
                Button b_delete = convertView.findViewById(R.id.btn_deletePlayer);
                b_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deletePlayer(new Player(values[position]));
                        crearLista();
                    }
                });
                TextView tv_name = convertView.findViewById(R.id.tv_Name);
                tv_name.setText(values[position]);

            }
            return convertView;
        }

        private void deletePlayer(Player player){
            dbHelper.deletePlayer(player);
        }
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
