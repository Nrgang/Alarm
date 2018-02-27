package android.lifeistech.com.alarm;

import android.app.PendingIntent;
import android.widget.Switch;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by matsumotokomei on 2017/06/07.
 */

public class Alarm implements Serializable {

    int hour;
    int minute;
    String content;
    boolean isEnabled;
    PendingIntent pendingIntent;

    public Alarm(int hour, int minute, String content, boolean isEnabled){

        this.hour = hour;
        this.minute = minute;
        this.content = content;
        this.isEnabled = isEnabled;
    }
}
