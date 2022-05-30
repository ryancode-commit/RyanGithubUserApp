package com.ra.ryangithubuserapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.TABLE_NAME
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.USERNAME
import java.sql.SQLException

class FavoriteHelper(context: Context) {
    private var dataBaseHelper: UserHelper = UserHelper(context)
    private lateinit var database: SQLiteDatabase



    companion object{
        private const val DATABASE_TABLE = TABLE_NAME

        private var INSTANCE: FavoriteHelper? = null
        fun getInstance(context: Context):FavoriteHelper = INSTANCE?: synchronized(this){
            INSTANCE?: FavoriteHelper(context)
        }

    }

    init {
        dataBaseHelper = UserHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }
    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                "$USERNAME ASC")
    }

    fun queryById(id: String): Cursor {
        return database.query(
                DATABASE_TABLE,
                null,
                "$USERNAME = ?",
                arrayOf(id),
                null,
                null,
                null,
                null)
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$USERNAME = '$id'", null)
    }
}
