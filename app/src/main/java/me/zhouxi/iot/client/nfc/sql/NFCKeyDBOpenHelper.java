package me.zhouxi.iot.client.nfc.sql;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by zhouxi on 25/10/2017.
 */

public class NFCKeyDBOpenHelper extends SQLiteOpenHelper{

    private static final String DEBUG_TAG = "NFCKeyDBOpenHelper";

    private static final String DB_NAME = "nfc_key.db";

    public static final String TABLE_NAME = "nfc_key";

    private static final int DB_VERSION = 1;

    public static final String PK = "create_time";

    public static final String KEY_NFC_KEY = "nfc_a_key";

    public NFCKeyDBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION, new DatabaseErrorHandler() {
            @Override
            public void onCorruption(SQLiteDatabase sqLiteDatabase) {
                this.onCorruption(sqLiteDatabase);
            }
        });
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" + PK + " TEXT PRIMARY KEY, " + KEY_NFC_KEY + " TEXT)";
        Log.e(DEBUG_TAG,"onCreate:"+sql);
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.e(DEBUG_TAG,"onUpgrade"+i + "to" + i1);
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public void onCorruption(SQLiteDatabase sqLiteDatabase) {
        Log.e(DEBUG_TAG,"onCorruption");

    }
}
