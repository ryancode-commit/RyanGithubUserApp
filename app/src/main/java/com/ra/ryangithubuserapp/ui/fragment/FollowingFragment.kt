package com.ra.ryangithubuserapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.ra.ryangithubuserapp.BuildConfig
import com.ra.ryangithubuserapp.R
import com.ra.ryangithubuserapp.adapter.ListUserAdapter
import com.ra.ryangithubuserapp.data.UserData
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_following.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class FollowingFragment : Fragment() {

    companion object {
        private val TAG = FollowingFragment::class.java.simpleName
        private val list:ArrayList<UserData> = ArrayList()

        private val ARG_USERNAME = "username"

        fun newInstance(username: String?):Fragment{
            val fragment = FollowingFragment()
            var bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.clear()

        rv_following.setHasFixedSize(true)
        val  username = arguments?.getString(ARG_USERNAME)
        Log.d(TAG, "onViewCreated: $username")
        username?.let { getFollowingUser(it) }


    }

    private fun getFollowingUser(username : String){
        progressBar_following.visibility =View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/following"
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token ${BuildConfig.GITHUB_TOKEN}")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray
            ) {
                progressBar_following.visibility =View.INVISIBLE
                val result = String(responseBody)
                try {

                    val array = JSONArray(result)
                    for (i in 0..array.length()) {
                        val item = array.getJSONObject(i)
                        val usernames = item.getString("login")
                        getDetailUser(usernames)
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                    progressBar_following.visibility =View.INVISIBLE
                }

            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>,
                    responseBody: ByteArray,
                    error: Throwable
            ) {
                progressBar_following.visibility = View.INVISIBLE
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

    private fun getDetailUser(username : String){
        progressBar_following.visibility =View.VISIBLE
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
                progressBar_following.visibility =View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val usernames: String = jsonObject.getString("login")
                    val name: String = jsonObject.getString("name")
                    val avatar: String = jsonObject.getString("avatar_url")

                    val user = UserData()
                    user.username = usernames
                    user.name = name
                    user.photo = avatar
                    list.add(user)
                    showRecyclerlist()



                } catch (e: Exception) {
                    e.printStackTrace()
                    progressBar_following.visibility =View.INVISIBLE
                }

            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>,
                    responseBody: ByteArray,
                    error: Throwable
            ) {
                progressBar_following.visibility =View.INVISIBLE
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

    fun showRecyclerlist(){
        rv_following.layoutManager = LinearLayoutManager(activity)
        val listUserAdapter = ListUserAdapter(list)
        rv_following.adapter = listUserAdapter
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }


}