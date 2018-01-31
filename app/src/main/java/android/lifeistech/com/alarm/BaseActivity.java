package android.lifeistech.com.alarm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.Timer;

/**
 * Created by matsumotokomei on 2017/11/22.
 */

public class BaseActivity extends AppCompatActivity {

    static SharedPreferences pref;
    SharedPreferences.Editor editor;

    Timer timer;
    Handler handler;

    static boolean alarmOn;

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
        alarmOn = pref.getBoolean("isAlarm", false);

        if (alarmOn == true) {
            Date date = new Date(System.currentTimeMillis());
            editor.putLong("endTime", date.getTime());
            editor.commit();

//            Toast.makeText(this, "時間を取得しました！", Toast.LENGTH_LONG).show();

            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
    }
}
