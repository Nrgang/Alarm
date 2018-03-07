package android.lifeistech.com.alarm;

import java.io.Serializable;

/**
 * Created by matsumotokomei on 2017/06/07.
 */

public class Alarm implements Serializable {

    int hour;
    int minute;
    String content;
    boolean isEnabled;

    int requestCode;

    public Alarm(int hour, int minute, String content, int requestCode) {

        this.hour = hour;
        this.minute = minute;
        this.content = content;
        this.requestCode = requestCode;
    }
}
