package com.example.assignment4

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import androidx.core.app.NotificationCompat
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_1.setOnClickListener{
            startActivity(Intent(applicationContext,time_activity::class.java))

        }

        button3.setOnClickListener {
            startActivity(Intent(applicationContext,map_activity::class.java))
        }

        floatingActionButton4.setOnClickListener {
            startActivity(Intent(applicationContext,time_activity::class.java))
        }
        floatingActionButton2.setOnClickListener {
            startActivity(Intent(applicationContext,map_activity::class.java))
        }
        floatingActionButton3.setOnClickListener {
            startActivity(Intent(applicationContext,list_activity::class.java))
        }





    }

    override fun onResume(){
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        doAsync {
            val db = Room.databaseBuilder(applicationContext,AppDatabase::class.java,"reminders").build()
            val reminders=db.reminderDao().getReminders()
            db.close()

            uiThread {

                if (reminders.isNotEmpty()){
                    val adapter = ReminderAdaptor(applicationContext,reminders)
                    list.adapter = adapter
                } else {
                    toast("No reminders yet")
                }

            }
        }
    }

    companion object{
        val CHANNEL_ID = "REMINDER_CHANNEL-ID"
        val NotificationID=1567
        fun showNotification(context: Context,message: String){
            var notificationBuilder = NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm_24px)
                .setContentTitle("Reminder").setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            var notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                val channel = NotificationChannel(CHANNEL_ID,context.getString(R.string.app_name)
                    ,NotificationManager.IMPORTANCE_DEFAULT
                ).apply{description=context.getString(R.string.app_name)}
                notificationManager.createNotificationChannel(channel)
            }
            val notification = NotificationID+ Random(NotificationID).nextInt(1,30)
            notificationManager.notify(notification,notificationBuilder.build())
        }

    }
}
