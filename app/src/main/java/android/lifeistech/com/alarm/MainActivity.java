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
import java.util.Timer;

public class MainActivity extends BaseActivity {

    List<Alarm> mAlarms;
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

    Timer timer;
    int time;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences("time", MODE_PRIVATE);
        editor = pref.edit();
        mAlarms = loadList();
        handler = new Handler();
        pointText = (TextView)findViewById(R.id.pointText);
        point = pref.getInt("point", 0);

        if (mAlarms == null) {
            mAlarms = new ArrayList<>();
        }

        mListView = (ListView) findViewById(R.id.alarmList);

        mAlarmAdapter = new AlarmAdapter(this, R.layout.alarm, mAlarms);
        mListView.setAdapter(mAlarmAdapter);

        Alarm al = (Alarm) getIntent().getSerializableExtra("alarm");

        if (al != null) {
            mAlarms.add(al);
            Log.d("Size=", mAlarms.size() + "");
            mAlarmAdapter.notifyDataSetChanged();
        }

        saveList();

        mAlarmAdapter.setListener(new AlarmAdapter.OnAlarmEnabledListener() {
            @Override
            public void onAlarmEnabled(Alarm item) {
                editor.putBoolean("isAlarm", true);
                editor.commit();

                Context context = MainActivity.this;
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(System.currentTimeMillis());

                Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
                // Intentをタイミングを見て他アプリに渡すPendingIntent

                int bid = intent.getIntExtra("intentId",0);
                Intent notificationIntent = new Intent(context, MainActivity.class);
                PendingIntent pendingIntentMain = PendingIntent.getActivity(context, bid, notificationIntent, 0);

                c.set(Calendar.HOUR_OF_DAY, item.hour); // itemから時間を取得、セット
                c.set(Calendar.MINUTE, item.minute); // 分
                c.set(Calendar.SECOND, 0); // 秒
                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent); // (スリープ状態でも起こす, )
                item.pendingIntent = pendingIntent;

                Date date = new Date(System.currentTimeMillis());
                long alarmTime = c.getTimeInMillis();
                long alarmOnTime = date.getTime();
                if (alarmTime - alarmOnTime >= 0){
                    long finalTimeDiff = (alarmTime - alarmOnTime) / (1000 * 60);
                }else {
                    long finalTimeDiff = 1440 - ((alarmOnTime - alarmTime) / (1000 * 60));
                }
                editor.putLong("alarmOnTime", alarmOnTime);
                editor.commit();

                Toast.makeText(context, "登録されました", Toast.LENGTH_SHORT).show();

                NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_add_button)
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(pendingIntentMain)
                        .build();

                // 通知
                notificationManager.notify(R.string.app_name, notification);
            }
        });
    }



    public void add(View v) {
        Intent intent = new Intent(this, ImputFormActivity.class);
        startActivity(intent);
    }

    private List<Alarm> loadList() {

        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("alarm", Context.MODE_PRIVATE);
        List<Alarm> data = gson.fromJson(sharedPreferences.getString("key_alarm", null), new TypeToken<List<Alarm>>() {
        }.getType());
        return data;
    }

    private void saveList() {
        SharedPreferences sharedPreferences = getSharedPreferences("alarm", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key_alarm", gson.toJson(mAlarms));
        editor.apply();
    }

    @Override
    protected void onResume(){
        super.onResume();
        boolean alarmOn = pref.getBoolean("isAlarm", false);//メインからアラームのOnOffを受け取る

        if (alarmOn == true) {
            Date date = new Date(System.currentTimeMillis());
            long endTime = pref.getLong("endTime", 1);
            long resTime = date.getTime();
            long alarmOnTime = pref.getLong("alarmOnTime", 0);

//            long dayDiff = (resTime - endTime) / (1000 * 60);
            long dayDiff = (resTime - alarmOnTime) / (1000 * 60);

            int diffInt = (int)dayDiff;
            double coef;

            if (diffInt >= 360){
                coef = Math.sqrt(5);
            }else if (diffInt >= 240){
                coef = Math.sqrt(4);
            }else if (diffInt >= 120){
                coef = Math.sqrt(3);
            }else if (diffInt >= 60){
                coef = Math.sqrt(2);
            }else {
                coef = Math.sqrt(1);
            }

//            BigDecimal bigDecimal = new BigDecimal(String.valueOf(coef));
//            double aInt = bigDecimal.setScale(0, RoundingMode.FLOOR).doubleValue();

            double oriAddPoint = diffInt * coef;
            nowAddPoint = (int)oriAddPoint;
            Log.d("Point=", String.valueOf(nowAddPoint));

            if (endTime != 1) {
                Toast.makeText(this, String.valueOf(dayDiff) + "分間です！", Toast.LENGTH_LONG).show();
            }

            pointText.setText(String.valueOf(point) + "+" + String.valueOf(nowAddPoint)); //表示
        }
    }
}
