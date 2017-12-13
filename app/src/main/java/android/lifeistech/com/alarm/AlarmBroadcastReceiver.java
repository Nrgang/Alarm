package android.lifeistech.com.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by matsumotokomei on 2017/09/8.
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver {

//    SharedPreferences pref;

    @Override
    public void onReceive(Context context, Intent intent) {
        // intentID (requestCode) を取り出す
        int bid = intent.getIntExtra("intentId",0);
        // ReceiverからMainActivityを起動させる
        Intent intent2 = new Intent(context, MainActivity.class);
//        SharedPreferences.Editor editor = pref.edit();
//        long alarmOnTime = pref.getLong("alarmOnTime", 0);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, bid, intent2, 0);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_add_button)
                .setTicker("時間です")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("TestAlarm")
                .setContentText("時間になりました")
                // 音、バイブレート、LEDで通知
                .setDefaults(Notification.DEFAULT_ALL)
                // 通知をタップした時にMainActivityを立ち上げる
                .setContentIntent(pendingIntent)
                .build();

        // 古い通知を削除
        notificationManager.cancelAll();
        // 通知
        notificationManager.notify(R.string.app_name, notification);

        Toast.makeText(context, "Received ", Toast.LENGTH_LONG).show();
    }
}
