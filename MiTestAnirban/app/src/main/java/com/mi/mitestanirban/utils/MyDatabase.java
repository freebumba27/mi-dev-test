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
    private static final String TABLE_ANDROID_VERSION = "android_version_tbl";
    private static final String KEY_VERSION = "version";
    private static final String KEY_CODENAME = "codename";
    private static final String KEY_TARGET = "target";
    private static final String KEY_DISTRIBUTION = "distribution";

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

    public void deleteAllDevice() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from " + TABLE_DEVICE);

    }

    public long insertAndroidVersions(int id, String name, String version, String codename, String target, String distribution) {
        SQLiteDatabase db = getReadableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, id);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_VERSION, version);
        initialValues.put(KEY_CODENAME, codename);
        initialValues.put(KEY_TARGET, target);
        initialValues.put(KEY_DISTRIBUTION, distribution);
        return db.insert(TABLE_ANDROID_VERSION, null, initialValues);
    }

    public void deleteAllAndroidVersion() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from " + TABLE_ANDROID_VERSION);
    }

    public Cursor getAllDeviceWithAndroidVersion() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT device_tbl.id, device_tbl.imageUrl, device_tbl.name, device_tbl.snippet," +
                " android_version_tbl.id, android_version_tbl.name, android_version_tbl.version " +
                "FROM device_tbl JOIN android_version_tbl ON device_tbl.androidId=android_version_tbl.id;", null);
        return res;
    }

    public Cursor getAndroidVersionAndId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT id, name, version FROM android_version_tbl", null);
        return res;
    }
}