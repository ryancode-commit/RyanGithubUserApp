package com.ra.ryangithubuserapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.ra.ryangithubuserapp.db.FavoriteHelper
import com.ra.ryangithubuserapp.db.UserContract.AUTHORITY
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.CONTENT_URI
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.TABLE_NAME


class FavProvider:ContentProvider() {
    companion object {
        private const val USER = 1
        private const val USER_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteHelper: FavoriteHelper
    }

    init {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)

        sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", USER_ID)
    }

    override fun onCreate(): Boolean {
        favoriteHelper = FavoriteHelper.getInstance(context as Context)
        favoriteHelper.open()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return when(sUriMatcher.match(uri)){
            USER -> favoriteHelper.queryAll()
            USER_ID -> favoriteHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
       val added:Long = when(USER){
           sUriMatcher.match(uri) -> favoriteHelper.insert(values)
           else -> 0
       }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted : Int = when(USER_ID){
            sUriMatcher.match(uri) -> favoriteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }
}