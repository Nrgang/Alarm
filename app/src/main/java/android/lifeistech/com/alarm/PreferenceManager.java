package android.lifeistech.com.alarm;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by matsumotokomei on 2018/03/07.
 */

public class PreferenceManager {
    private static final String PREF_KEY = "alarm";

    public static void saveAlarmList(Context context, List<Alarm> alarmList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        Gson gson = new Gson();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key_alarm", gson.toJson(alarmList));
        editor.apply();
    }

    public static void updateAlarmSwitch(Context context, int requestCode, boolean isEnabled) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        List<Alarm> data = gson.fromJson(sharedPreferences.getString("key_alarm", null), new TypeToken<List<Alarm>>() {
        }.getType());

        for (Alarm item : data) {
            if (item.requestCode == requestCode) {
                item.isEnabled = isEnabled;
            }
        }

        saveAlarmList(context, data);
    }
}
