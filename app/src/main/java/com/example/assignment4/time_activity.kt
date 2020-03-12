package com.example.assignment4

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_time_activity.*
import kotlinx.android.synthetic.main.list_view_items.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import java.util.*

class time_activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_activity)

        button.setOnClickListener {

            val calendar = GregorianCalendar(
                date_picker.year,
                date_picker.month,
                date_picker.dayOfMonth,
                time_picker.currentHour,
                time_picker.currentMinute

            )

            if ((editText.text.toString() !="")  &&
                (calendar.timeInMillis> System.currentTimeMillis())){


                val reminder = Reminder(

                    uid = null,
                    time = calendar.timeInMillis,
                    location = null,
                    message= editText.text.toString()
                )

                doAsync {
                    val db = Room.databaseBuilder(applicationContext,AppDatabase::class.java, "reminders").build()
                    db.reminderDao().insert(reminder)

                    setAlarm(reminder.time!!, reminder.message)

                    finish()

                }
            } else{
                toast("wrong data")
            }



        }
    }

 private fun setAlarm(time: Long, Message: String?){
    val intent = Intent(this, ReminderReceiver::class.java)
    intent.putExtra("message",Message)
    val pendingIntent = PendingIntent.getBroadcast(this,1,intent,PendingIntent.FLAG_ONE_SHOT)
    val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        manager.setExact(AlarmManager.RTC,time,pendingIntent)
    }
    runOnUiThread{toast("reminder is created")}
}
}
