package android.lifeistech.com.alarm;

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


    public Alarm(int hour, int minute, String content){
        this.hour = hour;
        this.minute = minute;
        this.content = content;
    }
}
