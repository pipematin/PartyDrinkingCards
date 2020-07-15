package com.darkmind.partydrinkingcards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ChupitazoDB.db";
    /*CARDS DATA INFO*/
    private static final String CARDS_TABLE_NAME = "cards";
    private static final String CARDS_COLUMN_ID = "id";
    private static final String CARDS_COLUMN_CONTENT = "content";
    private static final String CARDS_COLUMN_CONTENT_ES = "content_es";
    private static final String CARDS_COLUMN_PLAYERS = "players";
    private static final String CARDS_COLUMN_TYPE = "type";
    private static final String CARDS_COLUMN_USED = "used";
    private static final String CARDS_COLUMN_EXTRA = "extra";
    /*PARAMETERS DATA INFO*/
    private static final String PARAMETERS_TABLE_NAME = "parameters";
    private static final String PARAMETERS_COLUMN_SOUND = "sound";
    private static final String PARAMETERS_COLUMN_VIBRATION = "vibration";
    private static final String PARAMETERS_COLUMN_LANGUAGE = "language";
    private static final String PARAMETERS_COLUMN_COMMON = "common";
    private static final String PARAMETERS_COLUMN_RARE = "rare";
    private static final String PARAMETERS_COLUMN_EPIC = "epic";
    private static final String PARAMETERS_COLUMN_LEGENDARY = "legendary";
    /*PLAYERS DATA INFO*/
    private static final String PLAYERS_TABLE_NAME = "players";
    private static final String PLAYERS_COLUMN_ID = "id";
    private static final String PLAYERS_COLUMN_NAME = "name";

    /*EXTRA DATA INFO*/
//    private static final String EXTRA_TABLE_NAME = "extra";
//    private static final String EXTRA_COLUMN_ID = "id";
//    private static final String EXTRA_COLUMN_EXTRA1 = "extra1";
//    private static final String EXTRA_COLUMN_EXTRA1_E = "extra1_e";
//    private static final String EXTRA_COLUMN_EXTRA2 = "extra2";
//    private static final String EXTRA_COLUMN_EXTRA2_E = "extra2_e";
//    private static final String EXTRA_COLUMN_EXTRA3 = "extra3";
//    private static final String EXTRA_COLUMN_EXTRA3_E = "extra3_e";
//    private static final String EXTRA_COLUMN_EXTRA4 = "extra4";
//    private static final String EXTRA_COLUMN_EXTRA4_E = "extra4_e";


    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null,7);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + PARAMETERS_TABLE_NAME +
                        "(" + PARAMETERS_COLUMN_SOUND + " integer," +
                        PARAMETERS_COLUMN_VIBRATION + " integer," +
                        PARAMETERS_COLUMN_COMMON + " integer," +
                        PARAMETERS_COLUMN_RARE + " integer," +
                        PARAMETERS_COLUMN_EPIC + " integer," +
                        PARAMETERS_COLUMN_LEGENDARY + " integer," +
                        PARAMETERS_COLUMN_LANGUAGE + " integer)"
        );

        db.execSQL(
                "CREATE TABLE " + CARDS_TABLE_NAME +
                        "(" + CARDS_COLUMN_ID + " integer primary key," +
                        CARDS_COLUMN_CONTENT + " text," +
                        CARDS_COLUMN_CONTENT_ES + " text," +
                        CARDS_COLUMN_PLAYERS + " integer," +
                        CARDS_COLUMN_TYPE + " integer," +
                        CARDS_COLUMN_USED + " integer default 0," +
                        CARDS_COLUMN_EXTRA + " integer default 0)"
        );

        db.execSQL(
                "CREATE TABLE " + PLAYERS_TABLE_NAME +
                        "(" + PLAYERS_COLUMN_ID +" integer primary key," +
                        PLAYERS_COLUMN_NAME + " text)"
        );

//        db.execSQL(
//                "CREATE TABLE IF NOT EXISTS " + EXTRA_TABLE_NAME +
//                        "(" + EXTRA_COLUMN_ID +" integer primary key," +
//                        EXTRA_COLUMN_EXTRA1 + " integer default 0," +
//                        EXTRA_COLUMN_EXTRA1_E + " integer default 0, " +
//                        EXTRA_COLUMN_EXTRA2 + " integer default 0," +
//                        EXTRA_COLUMN_EXTRA2_E + " integer default 0, " +
//                        EXTRA_COLUMN_EXTRA3 + " integer default 0," +
//                        EXTRA_COLUMN_EXTRA3_E + " integer default 0, " +
//                        EXTRA_COLUMN_EXTRA4 + " integer default 0," +
//                        EXTRA_COLUMN_EXTRA4_E + " integer default 0 " + ")"
//        );

        insertParameterData(db);
//        insertExtra(db);

        insertDataFromFile(context, R.raw.insertdata, db);
        insertDataFromFile(context,R.raw.dlc,db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PARAMETERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CARDS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PLAYERS_TABLE_NAME);

//        ExtraData extraData = saveExtraTable(db);
//        db.execSQL("DROP TABLE IF EXISTS " + EXTRA_TABLE_NAME);
        onCreate(db);
//        loadExtraTable(db, extraData);
//        insertExtrasDatas(db);
    }

//    private class ExtraData{
//        private ArrayList<Integer> extras;
//        private ArrayList<Integer> setextras;
//
//        private ExtraData() {
//            extras = new ArrayList<>();
//            setextras = new ArrayList<>();
//        }
//
//        private void addExtras(int extra){
//            extras.add(extra);
//        }
//
//        private void addSetextras(int extra){
//            setextras.add(extra);
//        }
//    }
//
//    private ExtraData saveExtraTable(SQLiteDatabase db){
//        ExtraData extraData = new ExtraData();
//
//        for(int i = 1; i <= 4; i++){
//            extraData.addExtras(getExtra(false,i,db));
//            extraData.addSetextras(getExtra(true,i,db));
//        }
//
//        return extraData;
//    }
//
//    private void loadExtraTable(SQLiteDatabase db, ExtraData extraData){
//        for(int i = 0; i < extraData.extras.size(); i++){
//            if(extraData.extras.get(i) == 1){
//                dbsetExtra(i+1,false,extraData.extras.get(i),db);
//                dbsetExtra(i+1,true,extraData.setextras.get(i),db);
//            }
//        }
//    }
//
//    private void insertExtrasDatas(SQLiteDatabase db){
//        insertDataExtra(context,db,1);
//        insertDataExtra(context,db,2);
//        insertDataExtra(context,db,3);
//        insertDataExtra(context,db,4);
//    }
    /*---------------PARAMETERS------------*/

    public int numberOfParameters(){
        SQLiteDatabase db = getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, PARAMETERS_TABLE_NAME);
    }

    private void insertParameterData(SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(PARAMETERS_COLUMN_SOUND,1);
        contentValues.put(PARAMETERS_COLUMN_VIBRATION,1);
        contentValues.put(PARAMETERS_COLUMN_COMMON,1);
        contentValues.put(PARAMETERS_COLUMN_RARE,1);
        contentValues.put(PARAMETERS_COLUMN_EPIC,1);
        contentValues.put(PARAMETERS_COLUMN_LEGENDARY,1);
        contentValues.put(PARAMETERS_COLUMN_LANGUAGE, 1);
        db.insert(PARAMETERS_TABLE_NAME,null, contentValues);
    }

    public ParameterReturn getParameters(){
        Cursor cursor;
        ParameterReturn parameterReturn;
        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.rawQuery("select * from " + PARAMETERS_TABLE_NAME,null);
        if(cursor != null && cursor.moveToFirst()){
            parameterReturn = new ParameterReturn(
                    cursor.getInt(cursor.getColumnIndex(PARAMETERS_COLUMN_SOUND)),
                    cursor.getInt(cursor.getColumnIndex(PARAMETERS_COLUMN_VIBRATION)),
                    cursor.getInt(cursor.getColumnIndex(PARAMETERS_COLUMN_COMMON)),
                    cursor.getInt(cursor.getColumnIndex(PARAMETERS_COLUMN_RARE)),
                    cursor.getInt(cursor.getColumnIndex(PARAMETERS_COLUMN_EPIC)),
                    cursor.getInt(cursor.getColumnIndex(PARAMETERS_COLUMN_LEGENDARY)),
                    cursor.getInt(cursor.getColumnIndex(PARAMETERS_COLUMN_LANGUAGE))
            );
            cursor.close();
            return parameterReturn;
        }
        return null;
    }

    public void setParameter(int index,int value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        switch (index){
            case 1:
                contentValues.put(PARAMETERS_COLUMN_SOUND,Integer.toString(value));
                break;
            case 2:
                contentValues.put(PARAMETERS_COLUMN_VIBRATION,Integer.toString(value));
                break;
            case 3:
                contentValues.put(PARAMETERS_COLUMN_COMMON,Integer.toString(value));
                break;
            case 4:
                contentValues.put(PARAMETERS_COLUMN_RARE,Integer.toString(value));
                break;
            case 5:
                contentValues.put(PARAMETERS_COLUMN_EPIC,Integer.toString(value));
                break;
            case 6:
                contentValues.put(PARAMETERS_COLUMN_LEGENDARY,Integer.toString(value));
                break;
            case 7:
                contentValues.put(PARAMETERS_COLUMN_LANGUAGE,Integer.toString(value));
                break;
        }

        db.update(PARAMETERS_TABLE_NAME,contentValues,null,null);
    }

    /*----------PLAYERS-------------*/
    public int numberOfPlayers(){
        SQLiteDatabase db = getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, PLAYERS_TABLE_NAME);
    }

    public ArrayList<String> getPlayers(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + PLAYERS_TABLE_NAME,null);
        ArrayList<String> players = new ArrayList<>();
        while(cursor.moveToNext()){
            players.add(cursor.getString(cursor.getColumnIndex(PLAYERS_COLUMN_NAME)));
        }
        cursor.close();
        return players;
    }

    public Integer insertPlayer(Player player){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYERS_COLUMN_NAME,player.getName());
        return (int) db.insert(PLAYERS_TABLE_NAME,null, contentValues);
    }

    public Integer deletePlayer(Player player){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(PLAYERS_TABLE_NAME,PLAYERS_COLUMN_NAME +" = ?", new String[] {player.getName()});
    }

    private ArrayList<String> getRandomPlayers(int n){
        ArrayList<String> players = getPlayers();
        int randomNum;

        int numPlayers = players.size();

        if(n > numPlayers || n <= 0) return null;
        ArrayList<String> playersFinal = new ArrayList<>();

        for(int i = 0; i < n; i++){
            numPlayers = players.size();
            randomNum = getRandomNum(0,numPlayers-1);
            playersFinal.add(players.get(randomNum));
            players.remove(randomNum);
        }

        return playersFinal;
    }

    /*----------CARDS-------------*/
    public int numberOfCards(){
        SQLiteDatabase db = getReadableDatabase();
        int i = (int) DatabaseUtils.queryNumEntries(db, CARDS_TABLE_NAME);
        db.close();
        return i;
    }

    private int numberOfCardTypes(int card_type){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + CARDS_TABLE_NAME + " where type=" +
                card_type+"",null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    private int insertDataFromFile(Context context, int resourceId,SQLiteDatabase db) {
        // Reseting Counter
        int result = 0;

        // Open the resource
        Scanner scan = new Scanner(context.getResources().openRawResource(resourceId));
        scan.useDelimiter(Pattern.compile(";"));
        // Iterate through lines (assuming each insert has its own line and theres no other stuff)
        while (scan.hasNext()) {
            String insertStmt = scan.next();
            try{
                db.execSQL(insertStmt);
                result++;
            }catch (SQLiteException e){
                e.printStackTrace();
            }

        }
        scan.close();

        // returning number of inserted rows
        return result;
    }


    private Cursor getCardsByType(int cardType,int maxPlayers){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + CARDS_TABLE_NAME + " where " + CARDS_COLUMN_TYPE + "=" + cardType + " AND "
                + CARDS_COLUMN_PLAYERS + "<= " + maxPlayers + " AND "
                + CARDS_COLUMN_USED + "=" + 0 + "",null);
    }

    public DataReturn getCard(int cardType, int maxplayers){
        Cursor cursor;
        int randomNum;
        int maxCards;
        int min = 0;
        DataReturn dataReturn;
        String content;
        if(cardType < 0 || cardType > 4) return null;

        cursor = getCardsByType(cardType,maxplayers);
        if(cursor.getCount() == 0){
            updateAllUsedCards(cardType);
            cursor = getCardsByType(cardType,maxplayers);
        }

        maxCards = cursor.getCount()- 1;
        randomNum =getRandomNum(min,maxCards);
        if(cursor.moveToPosition(randomNum)){
            int numPlayers = cursor.getInt(cursor.getColumnIndex(CARDS_COLUMN_PLAYERS));
            String language = context.getResources().getConfiguration().locale.getLanguage();
            if (language.equals("es") ) {
                content = cursor.getString(cursor.getColumnIndex(CARDS_COLUMN_CONTENT_ES));
            }else{
                content = cursor.getString(cursor.getColumnIndex(CARDS_COLUMN_CONTENT));
            }
            int type = cursor.getInt(cursor.getColumnIndex(CARDS_COLUMN_TYPE));
            updateUsedCard(cursor.getInt(cursor.getColumnIndex(CARDS_COLUMN_ID)));
            dataReturn = new DataReturn(numPlayers,content,type);

            cursor.close();
            return dataReturn;
        }
        return null;
}

    private void updateUsedCard(int id){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CARDS_COLUMN_USED,1);
        db.update(CARDS_TABLE_NAME,cv,"id=?",new String[]{String.valueOf(id)});
    }

    private void updateAllUsedCards(int cardtype){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CARDS_COLUMN_USED,0);
        db.update(CARDS_TABLE_NAME,cv,CARDS_COLUMN_TYPE +"=?",new String[]{String.valueOf(cardtype)});
    }


    /*-----------EXTRA---------------*/
//    private void insertDataExtra(Context context,SQLiteDatabase db,int index){
//        Cursor cursor = db.rawQuery("select * from "+EXTRA_TABLE_NAME,null);
//        cursor.moveToFirst();
//        switch (index){
//            case 1:
//                if(cursor.getInt(cursor.getColumnIndex(EXTRA_COLUMN_EXTRA1)) == 1
//                        && cursor.getInt(cursor.getColumnIndex(EXTRA_COLUMN_EXTRA1_E)) == 1){
//
//                    insertDataFromFile(context,R.raw.extra1,db);
//                }
//                break;
//            case 2:
//                if(cursor.getInt(cursor.getColumnIndex(EXTRA_COLUMN_EXTRA2)) == 1
//                        && cursor.getInt(cursor.getColumnIndex(EXTRA_COLUMN_EXTRA2_E)) == 1){
//                    insertDataFromFile(context,R.raw.extra2,db);
//                }
//                break;
//            case 3:
//                if(cursor.getInt(cursor.getColumnIndex(EXTRA_COLUMN_EXTRA3)) == 1
//                        && cursor.getInt(cursor.getColumnIndex(EXTRA_COLUMN_EXTRA3_E)) == 1){
//                    insertDataFromFile(context,R.raw.extra3,db);
//                }
//                break;
//            case 4:
//                if(cursor.getInt(cursor.getColumnIndex(EXTRA_COLUMN_EXTRA4)) == 1
//                        && cursor.getInt(cursor.getColumnIndex(EXTRA_COLUMN_EXTRA4_E)) == 1){
//                    insertDataFromFile(context,R.raw.extra4,db);
//                }
//                break;
//        }
//
//        cursor.close();
//    }
//
//    private void insertExtra(SQLiteDatabase db){
//        Cursor cursor = db.rawQuery("select * from " + EXTRA_TABLE_NAME, null);
//        if(cursor.getCount() == 0) {
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(EXTRA_COLUMN_EXTRA1,0);
//            contentValues.put(EXTRA_COLUMN_EXTRA1_E,0);
//            contentValues.put(EXTRA_COLUMN_EXTRA2,0);
//            contentValues.put(EXTRA_COLUMN_EXTRA2_E,0);
//            contentValues.put(EXTRA_COLUMN_EXTRA3,0);
//            contentValues.put(EXTRA_COLUMN_EXTRA3_E,0);
//            contentValues.put(EXTRA_COLUMN_EXTRA4,0);
//            contentValues.put(EXTRA_COLUMN_EXTRA4_E,0);
//            db.insert(EXTRA_TABLE_NAME,null,contentValues);
//        }
//
//        cursor.close();
//
//    }
//
//    public void unlockExtra(int index){
//        SQLiteDatabase db = getWritableDatabase();
//
//        unlockExtra(index,db);
//    }
//
//    private void unlockExtra(int index, SQLiteDatabase db){
//        ContentValues contentValues = new ContentValues();
//        switch (index){
//            case 1:
//                contentValues.put(EXTRA_COLUMN_EXTRA1,1);
//                contentValues.put(EXTRA_COLUMN_EXTRA1_E,1);
//                break;
//            case 2:
//                contentValues.put(EXTRA_COLUMN_EXTRA2,1);
//                contentValues.put(EXTRA_COLUMN_EXTRA2_E,1);
//                break;
//            case 3:
//                contentValues.put(EXTRA_COLUMN_EXTRA3,1);
//                contentValues.put(EXTRA_COLUMN_EXTRA3_E,1);
//                break;
//            case 4:
//                contentValues.put(EXTRA_COLUMN_EXTRA4,1);
//                contentValues.put(EXTRA_COLUMN_EXTRA4_E,1);
//                break;
//        }
//        db.update(EXTRA_TABLE_NAME,contentValues,null,null);
//        insertDataExtra(context,db,index);
//    }
//
//    private void dbsetExtra(int index,boolean enabled,int value, SQLiteDatabase db){
//        ContentValues contentValues = new ContentValues();
//        switch (index){
//            case 1:
//                if(enabled){
//                    contentValues.put(EXTRA_COLUMN_EXTRA1_E,value);
//                }else{
//                    contentValues.put(EXTRA_COLUMN_EXTRA1,value);
//                }
//                break;
//            case 2:
//                if(enabled){
//                    contentValues.put(EXTRA_COLUMN_EXTRA2_E,value);
//                }else{
//                    contentValues.put(EXTRA_COLUMN_EXTRA2,value);
//                }
//                break;
//            case 3:
//                if(enabled){
//                    contentValues.put(EXTRA_COLUMN_EXTRA3_E,value);
//                }else{
//                    contentValues.put(EXTRA_COLUMN_EXTRA3,value);
//                }
//                break;
//            case 4:
//                if(enabled){
//                    contentValues.put(EXTRA_COLUMN_EXTRA4_E,value);
//                }else{
//                    contentValues.put(EXTRA_COLUMN_EXTRA4,value);
//                }
//                break;
//        }
//        db.update(EXTRA_TABLE_NAME,contentValues,null,null);
//    }
//
//    public void setExtra(int value, int index){
//        SQLiteDatabase db = getWritableDatabase();
//        setExtra(value,index,db);
//
//    }
//
//    private void setExtra(int value, int index, SQLiteDatabase db){
//        ContentValues contentValues = new ContentValues();
//        switch (index){
//            case 1:
//                contentValues.put(EXTRA_COLUMN_EXTRA1_E,value);
//                db.update(EXTRA_TABLE_NAME,contentValues,null,null);
//                if(value == 1) insertDataExtra(context,db,index);
//                else db.delete(CARDS_TABLE_NAME,CARDS_COLUMN_EXTRA + "= "+ 1,null);
//                break;
//            case 2:
//                contentValues.put(EXTRA_COLUMN_EXTRA2_E,value);
//                db.update(EXTRA_TABLE_NAME,contentValues,null,null);
//                if(value == 1) insertDataExtra(context,db,index);
//                else db.delete(CARDS_TABLE_NAME,CARDS_COLUMN_EXTRA + "= "+ 2,null);
//                break;
//            case 3:
//                contentValues.put(EXTRA_COLUMN_EXTRA3_E,value);
//                db.update(EXTRA_TABLE_NAME,contentValues,null,null);
//                if(value == 1) insertDataExtra(context,db,index);
//                else db.delete(CARDS_TABLE_NAME,CARDS_COLUMN_EXTRA + "= "+ 3,null);
//                break;
//            case 4:
//                contentValues.put(EXTRA_COLUMN_EXTRA4_E,value);
//                db.update(EXTRA_TABLE_NAME,contentValues,null,null);
//                if(value == 1) insertDataExtra(context,db,index);
//                else db.delete(CARDS_TABLE_NAME,CARDS_COLUMN_EXTRA + "= "+ 4,null);
//                break;
//        }
//    }
//
//    public void reverseExtra(){
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(EXTRA_COLUMN_EXTRA1,0);
//        contentValues.put(EXTRA_COLUMN_EXTRA1_E,0);
//        contentValues.put(EXTRA_COLUMN_EXTRA2,0);
//        contentValues.put(EXTRA_COLUMN_EXTRA2_E,0);
//        contentValues.put(EXTRA_COLUMN_EXTRA3,0);
//        contentValues.put(EXTRA_COLUMN_EXTRA3_E,0);
//        contentValues.put(EXTRA_COLUMN_EXTRA4,0);
//        contentValues.put(EXTRA_COLUMN_EXTRA4_E,0);
//        db.update(EXTRA_TABLE_NAME,contentValues,null,null);
//        db.delete(CARDS_TABLE_NAME,CARDS_COLUMN_EXTRA + ">= "+ 1,null);
//    }
//
//    public int getExtra(boolean e, int index){
//        SQLiteDatabase db = getReadableDatabase();
//
//        return getExtra(e,index,db);
//    }
//
//    private int getExtra(boolean e, int index, SQLiteDatabase db){
//        Cursor cursor;
//        int dato;
//
//        cursor = db.rawQuery("select * from "+ EXTRA_TABLE_NAME ,null);
//        cursor.moveToFirst();
//        switch (index){
//            case 1:
//                if(!e) dato = cursor.getInt(cursor.getColumnIndex(EXTRA_COLUMN_EXTRA1));
//                else dato = cursor.getInt(cursor.getColumnIndex(EXTRA_COLUMN_EXTRA1_E));
//                break;
//            case 2:
//                if(!e) dato = cursor.getInt(cursor.getColumnIndex(EXTRA_COLUMN_EXTRA2));
//                else dato = cursor.getInt(cursor.getColumnIndex(EXTRA_COLUMN_EXTRA2_E));
//                break;
//            case 3:
//                if(!e) dato = cursor.getInt(cursor.getColumnIndex(EXTRA_COLUMN_EXTRA3));
//                else dato = cursor.getInt(cursor.getColumnIndex(EXTRA_COLUMN_EXTRA3_E));
//                break;
//            case 4:
//                if(!e) dato = cursor.getInt(cursor.getColumnIndex(EXTRA_COLUMN_EXTRA4));
//                else dato = cursor.getInt(cursor.getColumnIndex(EXTRA_COLUMN_EXTRA4_E));
//                break;
//            default: dato = 0;
//        }
//        cursor.close();
//        return dato;
//    }

    public static int getRandomNum(int min, int max){
            return min + (int)(Math.random() * ((max - min) + 1));
    }
    /*---------------CLASES DE RETORNO DE DATOS ----------------------*/

    class DataReturn{
        public int numPlayers;
        public ArrayList<String> players;
        public String text;
        public int type;

        private DataReturn(int numPlayers, String text, int type){
            this.numPlayers = numPlayers;
            this.players = getRandomPlayers(numPlayers);
            this.text = text;
            this.type = type;
        }
    }

    class ParameterReturn{
        public int sound;
        public int vibration;
        public int common;
        public int rare;
        public int epic;
        public int legendary;
        public int language;

        private ParameterReturn(int sound, int vibration, int common, int rare, int epic, int legendary, int language) {
            this.sound = sound;
            this.vibration = vibration;
            this.common = common;
            this.rare = rare;
            this.epic = epic;
            this.legendary = legendary;
            this.language = language;
        }
    }
}
