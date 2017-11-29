package android.lifeistech.com.alarm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by matsumotokomei on 2017/11/22.
 */

public class BaseActivity extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getSharedPreferences("time", MODE_PRIVATE);
        editor = pref.edit();

        long endTime = pref.getLong("endTime", 1);
        long resTime = pref.getLong("resTime", 1);
        long dayDiff = ( resTime - endTime ) / (1000 * 60);

        if (endTime != 1) {
            Toast.makeText(this, String.valueOf(dayDiff) + "分間です！", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Date date = new Date(System.currentTimeMillis());
        editor.putLong("endTime", date.getTime());
        editor.commit();

        Toast.makeText(this, "時間を取得しました！", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume(){
        super.onResume();
        Date date = new Date(System.currentTimeMillis());
        editor.putLong("resTime", date.getTime());
        editor.commit();
        long endTime = pref.getLong("endTime", 1);
        long resTime = pref.getLong("resTime", 1);
        long dayDiff = ( resTime - endTime ) / (1000 * 60);

        //TODO:時間とポイントの計算(関係)は？

        if(endTime != 1) {
            Toast.makeText(this, String.valueOf(dayDiff) + "分間です！", Toast.LENGTH_LONG).show();
        }
    }
}
