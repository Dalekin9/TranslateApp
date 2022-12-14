package com.example.translateapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import com.example.translateapp.R
import com.example.translateapp.database.Dao
import com.example.translateapp.database.DicoApplication
import com.example.translateapp.database.entity.Mot
import java.lang.Integer.min
import kotlin.random.Random

class NotificationsService : LifecycleService() {

    private val CHANNEL_ID = "message urgent"
    private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    val pendingFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        PendingIntent.FLAG_IMMUTABLE
    else
        PendingIntent.FLAG_UPDATE_CURRENT

    private val currentIdList = mutableListOf<Int>()
    private val currentMotList = mutableListOf<Mot>()
    lateinit var dao: Dao
    private var data: List<Mot>? = null


    override fun onCreate() {
        super.onCreate()
        Log.d("NOTIF", "dans OnCreate")
        dao = (application as DicoApplication).database.MyDao()
        createNotificationChannel()

        dao.loadAllMotsNeedToBeLearn(true).observe(this) {
            Log.i("INFO", "dans loadAll observer")
            data = it
        }

        dao.loadAllMotsNeedToBeLearn(true)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d("NOTIF", "dans OnStartCommand")
        /*
        TODO update currentIdList quand suppression de notif
        */

        Log.d("NOTIF", "avant le if")
        if (data != null) {
            Log.d("NOTIF", "pas null")

            //val nbNotifsInitial = intent!!.getIntExtra("nbNotif", 10)
            val nbNotifs = min(6, data!!.size - 1)

            for (i in 0 until nbNotifs) {
                if (!currentIdList.contains(i)) {

                    Log.d("NOTIF", "envoie dune notification")
                    var x = Random.nextInt(data!!.size)
                    var mot = data!![x]
                    while (currentMotList.contains(mot)) {
                        x = Random.nextInt(data!!.size)
                        mot = data!![x]
                    }

                    val message = "Traduis : ${mot.word}"
                    val monIntent = Intent(Intent.ACTION_VIEW)
                    monIntent.data = Uri.parse(mot.urlTransl)

                    val pendingIntent = PendingIntent.getActivity(
                        this, 0, monIntent,
                        PendingIntent.FLAG_IMMUTABLE
                    )

                    val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.small)
                        .setContentTitle("Do you remember well ?")
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)


                    with(NotificationManagerCompat.from(this)) {
                        notify(i, notification.build())
                    }
                    currentIdList.add(i)
                    currentMotList.add(mot)
                }
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /* les notifications doivent posséder un channel */
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //val name = getString(R.string.channel_name)
            //val descriptionText = getString(R.string.channel_description)

            val name = "channel_name"
            val descriptionText = "channel_description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }
    }


    /*
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("NOTIF", "dans OnStartCommand")

            for (i in 0 until 4 ) {


                    val message = "Traduis "
                    val monIntent = Intent(this, MainActivity::class.java)

                    val pendingIntent = PendingIntent.getActivity(
                        this, 0, monIntent,
                        PendingIntent.FLAG_IMMUTABLE
                    )

                    val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.small)
                        .setContentTitle("Do you remember well ?")
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)

                    with(NotificationManagerCompat.from(this)) {
                        notify(i, notification.build())
                    }
        }
        return START_NOT_STICKY
    }
     */

}