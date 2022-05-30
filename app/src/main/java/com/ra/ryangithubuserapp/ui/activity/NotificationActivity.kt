package com.ra.ryangithubuserapp.ui.activity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.ra.ryangithubuserapp.R
import com.ra.ryangithubuserapp.alarm.AlarmReceiver
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : AppCompatActivity() {

    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var mSharedPreferences: SharedPreferences

    companion object{
        const val PREFS_NAME = "SettingPref"
        private const val DAILY = "daily"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.setting)

        alarmReceiver = AlarmReceiver()
        mSharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        setSwicth()
        swDaily.setOnCheckedChangeListener{_,isChecked->
            if (isChecked){
                alarmReceiver.setDailyReminder(
                    this,
                    AlarmReceiver.TYPE_DAILY,
                    getString(R.string.daily_message)
                )
            }else{
                alarmReceiver.cancelAlarm(this)
            }
            saveChange(isChecked)
        }
    }
    private fun setSwicth(){
        swDaily.isChecked = mSharedPreferences.getBoolean(DAILY,false)
    }

    private fun saveChange(value: Boolean) {
        val editor = mSharedPreferences.edit()
        editor.putBoolean(DAILY, value)
        editor.apply()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}