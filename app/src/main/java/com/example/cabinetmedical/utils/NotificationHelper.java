package com.example.cabinetmedical.utils;

import static android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.example.cabinetmedical.R;
import com.example.cabinetmedical.activities.MainActivity;
import com.example.cabinetmedical.data.local.entity.Appointment;
import com.example.cabinetmedical.receiver.NotificationReceiver;

import java.text.SimpleDateFormat;

public class NotificationHelper {
    private static final String CHANNEL_ID = "appointments_channel";
    private static final String CHANNEL_NAME = "Appointment Notifications";

    private final Context context;
    private final NotificationManager notificationManager;

    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notifications for upcoming appointments");
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void scheduleNotification(Appointment appointment, long triggerAtMillis, String title) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                // Request permission
                requestAlarmPermission();
                return;
            }
        }

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                (int) appointment.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        String contentText = "You have an appointment for " + appointment.getPurpose() +
                " at " + new SimpleDateFormat("h:mm a").format(appointment.getAppointmentTime());

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        // Create unique request code for each notification time
        int requestCode = (int) (appointment.getId() + triggerAtMillis % Integer.MAX_VALUE);

        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        notificationIntent.putExtra("notification", notification);
        notificationIntent.putExtra("id", requestCode);

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    alarmPendingIntent);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.S)
    private void requestAlarmPermission() {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            Intent intent = new Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            activity.startActivity(intent);
        }
    }
    public void cancelNotification(long appointmentId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                (int) appointmentId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
    }
}