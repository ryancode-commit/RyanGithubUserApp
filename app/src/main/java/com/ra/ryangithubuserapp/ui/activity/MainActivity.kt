package com.ra.ryangithubuserapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.ra.ryangithubuserapp.BuildConfig
import com.ra.ryangithubuserapp.R
import com.ra.ryangithubuserapp.adapter.ListUserAdapter
import com.ra.ryangithubuserapp.data.UserData
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
    private val list:ArrayList<UserData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_user.setHasFixedSize(true)
        getListUser()
        searchUser()
    }

    private fun getUserSearch(username: String){
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username"
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token ${BuildConfig.GITHUB_TOKEN}")
        client.get(url, object :AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val result = String(responseBody)
                try {
                    val jsonObject = JSONObject(result)
                    val array = jsonObject.getJSONArray("items")

                    for (i in 0..array.length()){
                        val item = array.getJSONObject(i)
                        val username = item.getString("login")
                        getDetailUser(username)
                    }
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d(TAG, "onFailure: $errorMessage")
                progressBar.visibility =View.INVISIBLE
            }

        })
    }

    private fun getListUser(){
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users"
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token ${BuildConfig.GITHUB_TOKEN}")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray
            ) {
                progressBar.visibility =View.INVISIBLE
                val result = String(responseBody)
                //Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val username = item.getString("login")
                        getDetailUser(username)


                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                    progressBar.visibility =View.INVISIBLE
                }

            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>,
                    responseBody: ByteArray,
                    error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d(TAG, "onFailure: $errorMessage")
                progressBar.visibility =View.INVISIBLE

            }

        })


    }

    private fun getDetailUser(username : String){
        progressBar.visibility =View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token ${BuildConfig.GITHUB_TOKEN}")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray
            ) {
                progressBar.visibility =View.INVISIBLE
                val result = String(responseBody)

                try {
                    val jsonObject = JSONObject(result)
                    val usernames: String = jsonObject.getString("login")
                    val name: String = jsonObject.getString("name")
                    val avatar: String = jsonObject.getString("avatar_url")
                    val company: String = jsonObject.getString("company")
                    val location: String = jsonObject.getString("location")
                    val repository: String = jsonObject.getString("public_repos")
                    val followers: String = jsonObject.getString("followers")
                    val following: String = jsonObject.getString("following")
                    val user = UserData()
                    user.username = usernames
                    user.name = name
                    user.company = company
                    user.photo = avatar
                    user.location = location
                    user.repository = repository
                    user.followers = followers
                    user.following = following
                    list.add(user)
                    showRecyclerList()


                } catch (e: Exception) {
                    e.printStackTrace()
                    progressBar.visibility =View.INVISIBLE
                }

            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>,
                    responseBody: ByteArray,
                    error: Throwable
            ) {
                progressBar.visibility =View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }

                Log.d(TAG, "onFailure: $errorMessage")

            }

        })



    }


    private fun  searchUser(){searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener, androidx.appcompat.widget.SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            if (query.isEmpty()){
                return true
            } else{
                list.clear()
                getUserSearch(query)
            }
            return true
        }

        override fun onQueryTextChange(p0: String): Boolean {
            return false
        }

    })}

    private fun showRecyclerList() {
        rv_user.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(list)
        rv_user.adapter = listUserAdapter


        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserData) {
                val mIntent = Intent(this@MainActivity, DetailActivity::class.java)
                mIntent.putExtra(DetailActivity.EXTRA_USER, data)
                startActivity(mIntent)
            }

        })
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