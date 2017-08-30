package android.lifeistech.com.alarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ListMenuItemView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Alarm> mAlarms;
    AlarmAdapter mAlarmAdapter;
    ListView mListView;
    SharedPreferences pref;
    Boolean bAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Alarm al = (Alarm) getIntent().getSerializableExtra("alarm");


        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("alarm", Context.MODE_PRIVATE);
        mAlarms = gson.fromJson(sharedPreferences.getString("key_alarm", null), new TypeToken<List<Alarm>>() {
        }.getType());

        if (mAlarms == null) {
            mAlarms = new ArrayList<>();
        }


        pref = getSharedPreferences("pref_alarm", MODE_PRIVATE);
        bAlarm = pref.getBoolean("key_alarm", false);
        mListView = (ListView) findViewById(R.id.alarmList);
        mAlarmAdapter = new AlarmAdapter(this, R.layout.alarm, mAlarms);
        mListView.setAdapter(mAlarmAdapter);

//        int hour = pref.getInt("key_hour", -1);
//        int minute = pref.getInt("key_minute", -1);
//        String content = pref.getString("key_content", "");
//        Alarm preAl = new Alarm(hour, minute, content);
//
//        if (hour != -1) {
//            mAlarm.add(preAl);
//        }


        if (al != null) {

            mAlarms.add(al);
            Log.d("Size=", mAlarms.size() + "");
            mAlarmAdapter.notifyDataSetChanged();


//            // SharedPreferences sharedPreferences = getSharedPreferences("alarm", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("key_alarm", gson.toJson(mAlarms));
//            editor.apply();

//            SharedPreferences.Editor editor = pref.edit();
//            editor.putInt("key_hour", al.hour);
//            editor.putInt("key_minute", al.minute);
//            editor.putString("key_content", al.content);
//            editor.commit();
        }
    }

    public void add(View v) {
        saveList ();
        Intent intent = new Intent(this, ImputFormActivity.class);
        startActivity(intent);
    }

    private void saveList() {
        SharedPreferences sharedPreferences = getSharedPreferences("alarm", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key_alarm", gson.toJson(mAlarms));
        editor.apply();
    }
}
