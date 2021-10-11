package com.example.testtask;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class DBHelperFavorite extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "favoriteDB";
    public static final String TABLE_FAVORITE = "favorite";

    public static final String FAVORITE_ID = "favorite_id";

    public DBHelperFavorite(@Nullable Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + this.TABLE_FAVORITE + "(" + this.FAVORITE_ID + " integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + this.TABLE_FAVORITE);

        this.onCreate(db);
    }

    public boolean checkIsDataAlreadyInDBorNotId(int id) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_FAVORITE + " where " + FAVORITE_ID + " = " + id;
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void deleteId(int id){
        SQLiteDatabase sqldb = this.getWritableDatabase();

        sqldb.execSQL("DELETE FROM `" + TABLE_FAVORITE + "` WHERE `" + FAVORITE_ID + "` = " + id);
    }

    public void insertId(int id){
        SQLiteDatabase sqldb = this.getWritableDatabase();
        sqldb.execSQL("INSERT INTO " + TABLE_FAVORITE + " (" + FAVORITE_ID + ") VALUES (" + id + ")");
    }
}
