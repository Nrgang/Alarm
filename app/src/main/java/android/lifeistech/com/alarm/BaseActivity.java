package android.lifeistech.com.alarm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by matsumotokomei on 2017/11/22.
 */

public class BaseActivity extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Timer timer;
    Handler handler;
    int time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences("time", MODE_PRIVATE);
        editor = pref.edit();
        handler = new Handler();
    }

    @Override
    protected void onPause() {
        super.onPause();
        boolean alarmOn = pref.getBoolean("isAlarm", false);

        if (alarmOn == true) {
            Date date = new Date(System.currentTimeMillis());
            editor.putLong("endTime", date.getTime());
            editor.commit();

            Toast.makeText(this, "時間を取得しました！", Toast.LENGTH_LONG).show();

            if (timer != null) {
                timer.cancel();
                timer = null;
            }

        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        boolean alarmOn = pref.getBoolean("isAlarm", false);//メインからアラームのOnOffを受け取る

        if (alarmOn == true) {
            Date date = new Date(System.currentTimeMillis());
            editor.putLong("resTime", date.getTime());
            editor.commit();
            long endTime = pref.getLong("endTime", 1);
            long resTime = pref.getLong("resTime", 1);
            long dayDiff = (resTime - endTime) / (1000 * 60);

            int diffInt = (int)dayDiff;

            editor.putInt("diffTimeInt", diffInt);
            editor.commit();

            //TODO:時間とポイントの計算(関係)は？

            if (endTime != 1) {
                Toast.makeText(this, String.valueOf(dayDiff) + "分間です！", Toast.LENGTH_LONG).show();
            }
        }
    }
}
