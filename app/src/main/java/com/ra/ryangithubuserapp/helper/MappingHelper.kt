package com.ra.ryangithubuserapp.helper

import android.database.Cursor
import com.ra.ryangithubuserapp.data.UserData
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.AVATAR
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.COMPANY
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.FOLLOWERS
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.FOLLOWING
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.LOCATION
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.NAME
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.REPOSITORY
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.USERNAME

object MappingHelper {
    fun mapCursorToArrayList(favCursor: Cursor?):ArrayList<UserData>{
        val favoriteList = ArrayList<UserData>()

        favCursor?.apply {
            while (moveToNext()){
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val name = getString(getColumnIndexOrThrow(NAME))
                val following = getString(getColumnIndexOrThrow(FOLLOWING))
                val followers = getString(getColumnIndexOrThrow(FOLLOWERS))
                val repository = getString(getColumnIndexOrThrow(REPOSITORY))
                val location = getString(getColumnIndexOrThrow(LOCATION))
                val avatar = getString(getColumnIndexOrThrow(AVATAR))
                val company = getString(getColumnIndexOrThrow(COMPANY))

                favoriteList.add(UserData(username= username,name = name,photo =  avatar, company = company, following = following, followers = followers, repository = repository, location = location))
            }
        }
        return favoriteList
    }
}