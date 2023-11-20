package com.example.eatswuneekotlin.server.chat

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.eatswuneekotlin.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    /** 푸시 알림으로 보낼 수 있는 메세지는 2가지
     * 1. Notification: 앱이 실행 중 (포그라운드)일 때만 푸시 알림이 옴
     * 2. Data: 실행 중이거나 백그라운드 (앱이 실행 중이지 않을 때) 알림이 옴 -> TODO: 대부분 사용하는 방식 */

    private val TAG = "FirebaseService"

    override fun onMessageReceived(msg: RemoteMessage) {
        Log.i("### msg : ", msg.toString())
        if (msg.data.isNotEmpty()) {
            Log.i("### data : ", msg.data.toString())
            sendTopNotification(msg.data["title"], msg.data["content"])
            if (true) {
                scheduleJob()
            } else {
                handleNow()
            }
        }
        if (msg.notification != null) {
            Log.i("### notification : ", msg.notification!!.body!!)
            sendTopNotification(msg.notification!!.title, msg.notification!!.body)
        }
    }

    /**
     * Schedule async work using WorkManager.
     */
    private fun scheduleJob() {
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .build()
        WorkManager.getInstance().beginWith(work).enqueue()
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    private fun sendTopNotification(title: String?, content: String?) {
        val CHANNEL_DEFAULT_IMPORTANCE = "channel_id"
        val ONGOING_NOTIFICATION_ID = 1
        val notificationIntent = Intent(this, MyFirebaseMessagingService::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE)
        val notification = Notification.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.setting_switch_thumb_on)
            .setContentIntent(pendingIntent)
            .build()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_DEFAULT_IMPORTANCE,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(ONGOING_NOTIFICATION_ID, notification)
    }

    companion object {
        private const val TAG = "FCMService"
    }
}