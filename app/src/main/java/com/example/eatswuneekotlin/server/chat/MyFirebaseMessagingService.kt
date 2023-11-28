package com.example.eatswuneekotlin.server.chat

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.eatswuneekotlin.MainActivity
import com.example.eatswuneekotlin.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    /** 푸시 알림으로 보낼 수 있는 메세지는 2가지
     * 1. Notification: 앱이 실행 중 (포그라운드)일 때만 푸시 알림이 옴
     * 2. Data: 실행 중이거나 백그라운드 (앱이 실행 중이지 않을 때) 알림이 옴 -> TODO: 대부분 사용하는 방식 */

    private val TAG = "FirebaseService"

    /** Token 생성 메서도 (FirebaseInstanceIdService 사라짐) **/
    override fun onNewToken(token: String) {
        Log.d(TAG, "new Token: $token")

        // 토큰 값을 따로 저장
        val pref = this.getSharedPreferences("token", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("token", token).apply()
        editor.commit()
        Log.i(TAG, "성공적으로 토큰을 저장함")
    }

    /** 메세지 수신 메서드 (포그라운드) */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: " + remoteMessage!!.from)

        // Notification 메세지를 수신할 경우
        remoteMessage.notification?.body!! // 여기에 내용이 저장되어 있음
        Log.d(TAG, "Notification Message Body: " + remoteMessage.notification?.body!!)

        // 받은 remoteMessage의 값 출력해 보기. 데이터 메세지 / 알림 메세지
        Log.d(TAG, "Message data : ${remoteMessage.data}")
        Log.d(TAG, "Message noti : ${remoteMessage.notification}")

        if (remoteMessage.notification != null) {
            // 알림 생성
            sendNotification(remoteMessage)
        } else {
            Log.e(TAG, "data가 비어있습니다. 메세지를 수신하지 못했습니다.")
        }
    }

    // Notification 채널 생성 (Oreo 이상에서 필요)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "EatSWU"
            val channelName = "EatSWU Channel"
            val channelDescription = "Channel for EatSWU notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH // 알림 중요도 설정

            val notificationChannel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
                // 다양한 설정 (소리, 진동 등) 추가 가능
                enableVibration(true)
                setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null) // 알림 소리 설정
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    /** 알림 생성 메서드 **/
    private fun sendNotification(remoteMessage: RemoteMessage) {
        // 채널 생성
        createNotificationChannel()

        // 알림 빌더 생성
        val notificationBuilder = NotificationCompat.Builder(this, "EatSWU")
            .setSmallIcon(R.drawable.eatswu_logo_ntext)
            .setContentTitle(remoteMessage.notification?.title)
            .setContentText(remoteMessage.notification?.body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // Intent 및 PendingIntent 설정
        // PendingIntent.FLAG_MUTABLE은 PendingIntent의 내용을 변경할 수 있도록 허용,
        // PendingIntent.FLAG_IMMUTABLE은 PendingIntent의 내용을 변경할 수 없음
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE)
        notificationBuilder.setContentIntent(pendingIntent)

        // NotificationManager를 통해 알림을 표시
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    /** Token 가져오기 **/
    fun getFirebaseToken() {
        // 동기 방식
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d(TAG, "Fetching FCM registration token failed ${task.exception}")
                return@OnCompleteListener
            }
            var deviceToken = task.result
            Log.e(TAG, "token=${deviceToken}")
        })
    }
}