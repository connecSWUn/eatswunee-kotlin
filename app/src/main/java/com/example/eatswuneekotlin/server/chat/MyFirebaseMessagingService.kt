package com.example.eatswuneekotlin.server.chat

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
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

        if (remoteMessage.data.isNotEmpty()) {
            // 알림 생성
            sendNotification(remoteMessage)
        } else {
            Log.e(TAG, "data가 비어있습니다. 메세지를 수신하지 못했습니다.")
        }
    }

    /** 알림 생성 메서드 **/
    private fun sendNotification(remoteMessage: RemoteMessage) {
        // channel 설정
        val channelId = "EatSWU"
        val channelName = "EatSWU"
        val channelDescription = "EatSWU"
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) // 알림 소리
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 버전 이후에는 채널이 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH // 중요도 (HIGH: 상단바 표시 가능)
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            notificationManager.createNotificationChannel(channel)
        }


        // RequestCode, Id를 고유값으로 지정하여 알림이 개별 표시
        val uniId: Int = (System.currentTimeMillis() / 7).toInt()

        // 일회용 PendingIntent : Intent의 실행 권한을 외부의 어플리케이션에게 위임
        val intent = Intent(this, MainActivity::class.java)
        // 각 key, value 추가
        for (key in remoteMessage.data.keys) {
            intent.putExtra(key, remoteMessage.data.getValue(key))
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Activity Stack을 경로만 남김


        // PendingIntent.FLAG_MUTABLE은 PendingIntent의 내용을 변경할 수 있도록 허용,
        // PendingIntent.FLAG_IMMUTABLE은 PendingIntent의 내용을 변경할 수 없음
        // val pendingIntent = PendingIntent.getActivity(this, uniId, intent, PendingIntent.FLAG_ONE_SHOT)
        val pendingIntent = PendingIntent.getActivity(this, uniId, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE)

        // 알림에 대한 UI 정보, 작업
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // 중요도 (HIGH: 상단바 표시 가능)
           .setSmallIcon(R.drawable.eatswu_logo) // 아이콘 설정
            .setContentTitle(remoteMessage.notification.toString()) // 제목
            .setContentText(remoteMessage.notification.toString()) // 메세지 내용
            .setAutoCancel(true) // 알림 클릭 시 삭제 여부
            // .setSound(SoundUri) // 알림 소리
            .setContentIntent(pendingIntent) // 알림 실행 시 Intent

        notificationManager.notify(uniId, notificationBuilder.build())
    }

    /** Token 가져오기 **/
    fun getFirebaseToken() {
        // 비동기 방식
//        FirebaseMessaging.getInstance().token.addOnSuccessListener {
//           Log.d(TAG, "token=${it}")
//        }

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