package android.lifeistech.com.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.google.gson.Gson;

/**
 * Created by matsumotokomei on 2017/06/07.
 */

public class AlarmAdapter extends ArrayAdapter<Alarm> {

    private OnAlarmEnabledListener listener;

    private List<Alarm> mAlarms;

    public void setListener(OnAlarmEnabledListener listener) {
        this.listener = listener;
    }

    public AlarmAdapter(Context context, int layoutResourceId, List<Alarm> objects) {

        super(context, layoutResourceId, objects);

        mAlarms = objects;
    }

    @Override
    public int getCount() {
        return mAlarms.size();
    }

    @Override
    public Alarm getItem(int position) {
        return mAlarms.get(position);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.alarm, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Alarm item = getItem(position);


        if (item != null) {
            // set data
            final int time = (item.hour * 60 * 60 + item.minute * 60) * 1000;
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            String timeFormatted = formatter.format(time);

            viewHolder.timeTextView.setText(timeFormatted);
            viewHolder.contentTextView.setText(item.content);
//
            viewHolder.mSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Context context = getContext();
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    if (isChecked) {
                        if (listener != null) listener.onAlarmEnabled(item);
                        item.isEnabled = true;

//                        Calendar c = Calendar.getInstance();
//                        c.setTimeInMillis(System.currentTimeMillis());
//
//                        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
//                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//                        c.set(Calendar.HOUR_OF_DAY, item.hour);
//                        c.set(Calendar.MINUTE, item.minute);
//                        c.set(Calendar.SECOND, 0);
//                        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
//                        item.pendingIntent = pendingIntent;
//
//
//                        Toast.makeText(context, "登録されました", Toast.LENGTH_SHORT).show();

                    } else {
                        alarmManager.cancel(item.pendingIntent);
                    }

                }
            });
//
        }

        return convertView;
    }

    private class ViewHolder {
        TextView timeTextView;
        TextView contentTextView;
        Switch mSwitch;

        public ViewHolder(View view) {
            timeTextView = (TextView) view.findViewById(R.id.alarmTime);
            contentTextView = (TextView) view.findViewById(R.id.alarmContent);
            mSwitch = (Switch) view.findViewById(R.id.alarmSwitch);
        }
    }

    interface OnAlarmEnabledListener {
        public void onAlarmEnabled(Alarm item);
    }
}
