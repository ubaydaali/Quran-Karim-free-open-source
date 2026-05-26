package net.onws.alquranalkarim.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import net.onws.alquranalkarim.MainActivity
import net.onws.alquranalkarim.R

object NotificationHelper {
    const val CHANNEL_ID = "quran_reminder_channel"
    const val NOTIFICATION_ID = 1001

    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "تذكير بقراءة القرآن",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "تذكير يومي بقراءة القرآن الكريم"
            enableVibration(true)
            setShowBadge(true)
        }
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun showReminderNotification(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle("تذكير بقراءة القرآن 📖")
            .setContentText("حان وقت قراءة وردك اليومي من القرآن الكريم")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("اللهم ذكرنا بما ينفعنا وانفعنا بما علمتنا\nحان وقت قراءة وردك اليومي من القرآن الكريم"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }
}