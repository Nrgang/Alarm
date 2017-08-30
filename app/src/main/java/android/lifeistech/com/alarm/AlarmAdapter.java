package android.lifeistech.com.alarm;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by matsumotokomei on 2017/06/07.
 */

public class AlarmAdapter extends ArrayAdapter<Alarm> {

    TextView textView;

    List<Alarm> mAlarm;

    public AlarmAdapter(Context context, int layoutResourceId, List<Alarm> objects) {

        super(context, layoutResourceId, objects);

        mAlarm = objects;
    }

    @Override
    public int getCount() {
        return mAlarm.size();
    }

    @Override
    public Alarm getItem(int position) {
        return  mAlarm.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final ViewHolder viewHolder;

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.alarm, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Alarm item = getItem(position);



        if (item != null){
            //set data
//            viewHolder.timeTextView.setText(item.hour + ":" + item.minute);

            int time = (item.hour * 60 * 60 + item.minute * 60) * 1000;
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            String timeFormatted = formatter.format(time);

            viewHolder.timeTextView.setText(timeFormatted);
            viewHolder.contentTextView.setText(item.content);


            };

        return convertView;
    }

    private class ViewHolder {
        TextView timeTextView;
        TextView contentTextView;

        public ViewHolder(View view){
            timeTextView = (TextView) view.findViewById(R.id.alarmTime);
            contentTextView = (TextView) view.findViewById(R.id.alarmContent);
        }
    }

}
