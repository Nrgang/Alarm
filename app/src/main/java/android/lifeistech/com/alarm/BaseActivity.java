package android.lifeistech.com.alarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Date;
import java.util.List;
import java.util.Timer;

/**
 * Created by matsumotokomei on 2017/11/22.
 */

public class BaseActivity extends AppCompatActivity {

    private final String PREF_KEY = "alarm";
    List<Alarm> mAlarms = MainActivity.mAlarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    @Override
    protected void onPause() {
        super.onPause();

//        List<Alarm> list = mAlarms;
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("key_alarm", gson.toJson(mAlarms));
        editor.commit();
    }
}