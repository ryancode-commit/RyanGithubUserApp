package com.ra.consumerapp.ui.activity

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.ra.consumerapp.BuildConfig
import com.ra.consumerapp.R
import com.ra.consumerapp.adapter.ListUserAdapter
import com.ra.consumerapp.data.UserData
import com.ra.consumerapp.db.UserContract.Favorite.Companion.CONTENT_URI
import com.ra.consumerapp.helper.MappingHelper
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private val userList = ArrayList<UserData>()
    private val userAdapter = ListUserAdapter(userList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_user.layoutManager = LinearLayoutManager(this)
        rv_user.setHasFixedSize(true)

        userAdapter.notifyDataSetChanged()
        rv_user.adapter = userAdapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadUserAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)


        loadUserAsync()
    }

    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            val deferredUser = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val users = deferredUser.await()
            if (users.size > 0) {
                empty.visibility = View.INVISIBLE
                userAdapter.setUser(users)
                progressBar.visibility = View.INVISIBLE
            } else {
                rv_user.visibility = View.INVISIBLE
                empty.visibility = View.VISIBLE
                progressBar.visibility = View.INVISIBLE
            }
        }
    }



}