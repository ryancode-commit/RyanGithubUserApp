package com.ra.ryangithubuserapp.ui.activity

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ra.ryangithubuserapp.helper.MappingHelper
import com.ra.ryangithubuserapp.R
import com.ra.ryangithubuserapp.adapter.ListUserAdapter
import com.ra.ryangithubuserapp.data.UserData
import com.ra.ryangithubuserapp.db.FavoriteHelper
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.CONTENT_URI
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favoriteHelper: FavoriteHelper
    private val list:ArrayList<UserData> = ArrayList()
    private val adapter = ListUserAdapter(list)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        recycleViewFavorite.layoutManager = LinearLayoutManager(this)
        recycleViewFavorite.setHasFixedSize(true)
        adapter.notifyDataSetChanged()
        recycleViewFavorite.adapter = adapter
        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        loadFavAsync()

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: UserData) {
                val mIntent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                mIntent.putExtra(DetailActivity.EXTRA_USER, data)
                startActivity(mIntent)
            }

        })



    }

    private fun loadFavAsync(){
        GlobalScope.launch(Dispatchers.Main){
            progressBarFav.visibility = View.VISIBLE
            empty.visibility = View.INVISIBLE
            val deferredFav = async(Dispatchers.IO){
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favorites = deferredFav.await()
            if (favorites.size > 0){
                progressBarFav.visibility = View.INVISIBLE
                adapter.setUser(favorites)
            }else{
                progressBarFav.visibility = View.INVISIBLE
                recycleViewFavorite.visibility = View.INVISIBLE
                empty.visibility = View.VISIBLE
            }

        }
    }
}