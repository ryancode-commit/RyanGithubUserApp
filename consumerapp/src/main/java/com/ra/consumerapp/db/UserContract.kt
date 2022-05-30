package com.ra.consumerapp.db

import android.net.Uri
import android.provider.BaseColumns

object  UserContract {
    const val AUTHORITY = "com.ra.ryangithubuserapp"
    const val SCHEME = "content"

     class Favorite:BaseColumns{
        companion object{
            const val TABLE_NAME = "favorite"
            const val USERNAME ="username"
            const val NAME ="name"
            const val FOLLOWING ="following"
            const val FOLLOWERS ="followers"
            const val REPOSITORY ="repository"
            const val LOCATION ="location"
            const val COMPANY ="company"
            const val AVATAR = "avatar"

            val CONTENT_URI:Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}