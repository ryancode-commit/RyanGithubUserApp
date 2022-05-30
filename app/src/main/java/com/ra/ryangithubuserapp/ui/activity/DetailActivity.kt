package com.ra.ryangithubuserapp.ui.activity

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.contentValuesOf
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ra.ryangithubuserapp.R
import com.ra.ryangithubuserapp.adapter.SectionsPagerAdapter
import com.ra.ryangithubuserapp.data.UserData
import com.ra.ryangithubuserapp.db.FavoriteHelper
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.AVATAR
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.COMPANY
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.CONTENT_URI
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.FOLLOWERS
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.FOLLOWING
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.LOCATION
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.NAME
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.REPOSITORY
import com.ra.ryangithubuserapp.db.UserContract.Favorite.Companion.USERNAME
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {
    private var statusFavorite = false
    private var favorite : UserData? = null
    private lateinit var favoriteHelper: FavoriteHelper
    companion object{
        const val EXTRA_USER = "extra_user"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        favorite = intent.getParcelableExtra(EXTRA_USER)

         val dataUser = intent.getParcelableExtra<UserData>(EXTRA_USER) as UserData
        supportActionBar?.title = dataUser.username
        tv_name.text = dataUser.name
        tv_lokasi.text = dataUser.location
        tv_company.text = dataUser.company
        tv_following.text = dataUser.following
        tv_followers.text = dataUser.followers
        tv_repository.text = dataUser.repository
        Glide.with(this)
                .load(dataUser.photo)
                .apply(RequestOptions().override(200,200))
                .into(img_avatar)



        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = dataUser.username
        view_pager.adapter = sectionsPagerAdapter
        tabLayout.setupWithViewPager(view_pager)

        supportActionBar?.elevation = 0f



        setStatusFavorite(statusFavorite)
        btn_favorite.setOnClickListener{
            if (!statusFavorite){
                val values = contentValuesOf(
                        USERNAME to dataUser.username,
                        NAME to dataUser.name,
                        FOLLOWING to dataUser.following,
                        FOLLOWERS to dataUser.followers,
                        REPOSITORY to dataUser.repository,
                        LOCATION to dataUser.location,
                        COMPANY to dataUser.company,
                        AVATAR to dataUser.photo
                )
                contentResolver.insert(CONTENT_URI, values)
                Toast.makeText(this, R.string.add_favorite, Toast.LENGTH_SHORT).show()
                statusFavorite = !statusFavorite
                setStatusFavorite(statusFavorite)
            }else{
                Toast.makeText(this, R.string.delete_favorite, Toast.LENGTH_SHORT).show()
                favoriteHelper.deleteById(favorite?.username.toString())
                statusFavorite = !statusFavorite
                setStatusFavorite(statusFavorite)
            }
        }

        val cursor :Cursor = favoriteHelper.queryById(favorite?.username.toString())
        if (cursor.moveToNext()){
            statusFavorite = true
            setStatusFavorite(statusFavorite)
        }



    }

    private fun setStatusFavorite(statusFavorite:Boolean){
        if (statusFavorite){
            btn_favorite.setImageResource(R.drawable.ic_favorite)
        }else {
            btn_favorite.setImageResource(R.drawable.ic_not_favorite)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_favorite ->{
                val mIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(mIntent)
            }

            R.id.action_reminder->{
                val mIntent = Intent(this, NotificationActivity::class.java)
                startActivity(mIntent)
            }

            R.id.action_change_language ->{
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }




}