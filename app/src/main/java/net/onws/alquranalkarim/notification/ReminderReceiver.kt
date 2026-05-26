package net.onws.alquranalkarim.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        NotificationHelper.showReminderNotification(context)
        // Reschedule for the next day
        AlarmScheduler.rescheduleIfNeeded(context)
    }
}