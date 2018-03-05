package android.lifeistech.com.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by matsumotokomei on 2017/06/07.
 */

public class AlarmAdapter extends ArrayAdapter<Alarm> {

    public static final String TAG = "AlarmAdapter";

    SharedPreferences aPref;
    SharedPreferences.Editor aEditor;

    private List<Alarm> mAlarms;

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

                        item.isEnabled = true;

                        Calendar c = Calendar.getInstance();
                        c.setTimeInMillis(System.currentTimeMillis());
                        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, item.requestCode, intent, 0);
                        c.set(Calendar.HOUR_OF_DAY, item.hour);
                        c.set(Calendar.MINUTE, item.minute);
                        c.set(Calendar.SECOND, 0);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

                        Toast.makeText(context, "登録されました", Toast.LENGTH_SHORT).show();
                    } else {
                        item.isEnabled = false;

                        // アラームのキャンセル
                        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, item.requestCode, intent, 0);

                        pendingIntent.cancel();
                        alarmManager.cancel(pendingIntent);
                    }

                    // 表示を更新する
                    notifyDataSetChanged();
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
}
