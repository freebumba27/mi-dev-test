package com.mi.mitestanirban.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by AJ on 1/23/16.
 */
public class MyDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "android_device_db.db";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_ID = "id";
    private static final String KEY_ANDROID_ID = "androidId";
    private static final String KEY_IMAGE_URL = "imageUrl";
    private static final String KEY_NAME = "name";
    private static final String KEY_SNIPPET = "snippet";
    private static final String TABLE_DEVICE = "device_tbl";

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public long insertDevices(int id, int androidId, String imageUrl, String name, String snippet) {
        SQLiteDatabase db = getReadableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, id);
        initialValues.put(KEY_ANDROID_ID, androidId);
        initialValues.put(KEY_IMAGE_URL, imageUrl);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_SNIPPET, snippet);
        return db.insert(TABLE_DEVICE, null, initialValues);
    }

    public Cursor getAllDevice() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_DEVICE, null);
        return res;
    }

    public void deleteAllDevice() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from " + TABLE_DEVICE);
    }

}