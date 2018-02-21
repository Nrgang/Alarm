package android.lifeistech.com.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

/**
 * Created by matsumotokomei on 2017/06/07.
 */

public class AlarmAdapter extends ArrayAdapter<Alarm> {

    public static final String TAG = "AlarmAdapter";

    SharedPreferences aPref;
    SharedPreferences.Editor aEditor;

    private OnAlarmEnabledListener listener;

//    private OnItemUpdatedListener updatedListener;

    private List<Alarm> mAlarms;

//    public void setUpdatedListener(OnItemUpdatedListener updatedListener) {
//        this.updatedListener = updatedListener;
//    }

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
        Log.d(TAG, String.valueOf(item.isEnabled));

        if (item != null) {
            // set data
            final int time = (item.hour * 60 * 60 + item.minute * 60) * 1000;
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            String timeFormatted = formatter.format(time);

            viewHolder.mTimeTextView.setText(timeFormatted);
            viewHolder.mContentTextView.setText(item.content);
            viewHolder.mSwitch.setChecked(item.isEnabled);

            viewHolder.mSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Context context = getContext();
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                    if (isChecked) {
//                        if (listener != null) item.pendingIntent = listener.onAlarmEnabled(item);
//                        item.isEnabled = true;
                        mAlarms.set(position, item);

//                        aPref = BaseActivity.pref;
//                        aEditor = aPref.edit();
//                        aEditor.putBoolean("isAlarm", true);
//                        aEditor.apply();
//                        item.isEnabled = aPref.getBoolean("isAlarm", false);


                        Calendar c = Calendar.getInstance();
                        c.setTimeInMillis(System.currentTimeMillis());
                        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
                        c.set(Calendar.HOUR_OF_DAY, item.hour);
                        c.set(Calendar.MINUTE, item.minute);
                        c.set(Calendar.SECOND, 0);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                        item.pendingIntent = pendingIntent;

                        Toast.makeText(context, "登録されました", Toast.LENGTH_SHORT).show();
                    } else {

//                        aPref = BaseActivity.pref;
//                        aEditor = aPref.edit();
//                        aEditor.putBoolean("isAlarm", false);
//                        aEditor.apply();
//                        item.isEnabled = aPref.getBoolean("isAlarm", false);
//
//                        BaseActivity.alarmOn=false;

//                        if (item.pendingIntent != null) {
//                            alarmManager.cancel(item.pendingIntent);
//                        }
//                    }
//                    mAlarms.set(position, item);
//
//                    if (updatedListener != null) updatedListener.onUpdated(mAlarms);
//                    // save したい...
                    }
                }
            });
        }

        return convertView;
    }

    private class ViewHolder {
        TextView mTimeTextView;
        TextView mContentTextView;
        Switch mSwitch;

        public ViewHolder(View view) {
            mTimeTextView = (TextView) view.findViewById(R.id.alarmTime);
            mContentTextView = (TextView) view.findViewById(R.id.alarmContent);
            mSwitch = (Switch) view.findViewById(R.id.alarmSwitch);
        }
    }

    interface OnAlarmEnabledListener {
        public PendingIntent onAlarmEnabled(Alarm item);
    }
}
//
//    interface OnItemUpdatedListener {
//        public void onUpdated(List<Alarm> list);
//    }
//}
