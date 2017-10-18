package android.lifeistech.com.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ListMenuItemView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Alarm> mAlarms;
    AlarmAdapter mAlarmAdapter;
    ListView mListView;
    SharedPreferences pref;
    boolean bAlarm;
    // boolean 型の変数は動詞で始める。
    // ３人称
    // 例: isOk, hasContent, exits
    // if (item.hasContent) { }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAlarms = loadList();

        if (mAlarms == null) {
            mAlarms = new ArrayList<>();
        }

//        pref = getSharedPreferences("pref_alarm", MODE_PRIVATE);
//        bAlarm = pref.getBoolean("key_alarm", false);

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
                Context context = MainActivity.this;
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(System.currentTimeMillis());

                Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
                c.set(Calendar.HOUR_OF_DAY, item.hour);
                c.set(Calendar.MINUTE, item.minute);
                c.set(Calendar.SECOND, 0);
                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                item.pendingIntent = pendingIntent;

                //TODO (Alarm) item に変更が入ったので、それを保存する
                

                Toast.makeText(context, "登録されました", Toast.LENGTH_SHORT).show();
            }
        });
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        PendingIntent pendingIntent = getPendingIntent();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(alarmTimeMillis, null), pendingIntent);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
//        } else {
//            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
//        }
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
}
