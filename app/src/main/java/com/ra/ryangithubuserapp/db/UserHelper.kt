package com.ra.ryangithubuserapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.AVATAR
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.COMPANY
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.FOLLOWERS
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.FOLLOWING
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.LOCATION
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.NAME
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.REPOSITORY
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.TABLE_NAME
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.USERNAME

internal class UserHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "dgithubapp"

        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE $TABLE_NAME"+
                " ($USERNAME TEXT PRIMARY KEY NOT NULL,"+
                " $NAME TEXT NOT NULL,"+
                " $FOLLOWING TEXT NOT NULL,"+
                " $FOLLOWERS TEXT NOT NULL,"+
                " $REPOSITORY TEXT NOT NULL,"+
                " $LOCATION TEXT NOT NULL,"+
                " $COMPANY TEXT NOT NULL,"+
                " $AVATAR TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


}