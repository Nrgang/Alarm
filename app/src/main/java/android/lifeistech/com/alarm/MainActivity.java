package android.lifeistech.com.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String PREF_KEY = "alarm";

    static List<Alarm> mAlarms;
    AlarmAdapter mAlarmAdapter;
    ListView mListView;
    SharedPreferences pref;
    // boolean 型の変数は動詞で始める。
    // ３人称
    // 例: isOk, hasContent, exits
    // if (item.hasContent) { }

    int point;
    int nowAddPoint;
    TextView pointText;
    Handler handler;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences("time", MODE_PRIVATE);
        mAlarms = loadList();
        handler = new Handler();
        pointText = (TextView) findViewById(R.id.pointText);
        point = pref.getInt("point", 0);

        if (mAlarms == null) {
            mAlarms = new ArrayList<>();
        }

        mListView = (ListView) findViewById(R.id.alarmList);

        mAlarmAdapter = new AlarmAdapter(this, R.layout.alarm, mAlarms);
        mListView.setAdapter(mAlarmAdapter);

        // update
        Alarm al = (Alarm) getIntent().getSerializableExtra("alarm");
        if (al != null) {mAlarms.add(al);
            Log.d("Size=", mAlarms.size() + "");
            mAlarmAdapter.notifyDataSetChanged();
        }
        saveList(mAlarms);
    }


    private Calendar updateCalender(Alarm item, Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, item.hour); // itemから時間を取得、セット
        c.set(Calendar.MINUTE, item.minute); // 分
        c.set(Calendar.SECOND, 0); // 秒
        return c;
    }

    public void add(View v) {
        Intent intent = new Intent(this, ImputFormActivity.class);
        startActivity(intent);
    }

    private List<Alarm> loadList() {

        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        List<Alarm> data = gson.fromJson(sharedPreferences.getString("key_alarm", null), new TypeToken<List<Alarm>>() {}.getType());
        return data;
    }

    public void saveList(List<Alarm> list) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        Gson gson = new Gson();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key_alarm", gson.toJson(list));
        editor.apply();
    }

    private long finalTimeDiff(Calendar c) {
        Date date = new Date(System.currentTimeMillis());
        long alarmTime = c.getTimeInMillis();
        long alarmOnTime = date.getTime();
        long leaveTime;
        if (alarmTime - alarmOnTime >= 0) {
            leaveTime = (alarmTime - alarmOnTime) / (1000 * 60);//何分間放置するか
        } else {
            //時間が過去の場合明日にする
            leaveTime = 60 * 24 - ((alarmOnTime - alarmTime) / (1000 * 60));
        }

        SharedPreferences.Editor editor = pref.edit();
        editor.putLong("alarmOnTime", alarmOnTime);
        editor.commit();
        return leaveTime;
    }

    private void alwaysNotification(Context context, PendingIntent pendingIntent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_add_button)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .build();

        // 通知
        notificationManager.notify(R.string.app_name, notification);
    }

    //update
    @Override
    protected void onPause(){
        super.onPause();

        saveList(mAlarms);
    }


    @Override
    protected void onResume() {
        super.onResume();
        boolean alarmOn = pref.getBoolean("isAlarm", false);//メインからアラームのOnOffを受け取る

        if (alarmOn == true) {
            Date date = new Date(System.currentTimeMillis());
            long resTime = date.getTime();
            long alarmOnTime = pref.getLong("alarmOnTime", 0);

            long dayDiff = (resTime - alarmOnTime) / (1000 * 60);

            int diffInt = (int) dayDiff;
            double coef;

            if (diffInt >= 360) {
                coef = Math.sqrt(5);
            } else if (diffInt >= 240) {
                coef = Math.sqrt(4);
            } else if (diffInt >= 120) {
                coef = Math.sqrt(3);
            } else if (diffInt >= 60) {
                coef = Math.sqrt(2);
            } else {
                coef = Math.sqrt(1);
            }

            double oriAddPoint = diffInt * coef;
            nowAddPoint = (int) oriAddPoint;
            Log.d("Point=", String.valueOf(nowAddPoint));

            pointText.setText(String.valueOf(point) + "+" + String.valueOf(nowAddPoint)); //表示
        }
    }
}