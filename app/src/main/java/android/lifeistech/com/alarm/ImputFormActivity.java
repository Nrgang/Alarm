package android.lifeistech.com.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.io.Serializable;

public class ImputFormActivity extends BaseActivity {

    EditText title;
    int hour;
    int minute;
    String content;

    TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imput_form);

        Intent intent = getIntent();
        String string = intent.getStringExtra("key");
        title = (EditText)findViewById(R.id.titleEditText);
        title.setText(string);
        timePicker = (TimePicker)findViewById(R.id.timePicker);

    }

    public void done(View v) {
        hour = timePicker.getCurrentHour();
        minute = timePicker.getCurrentMinute();
        content = title.getText().toString();

        Alarm al = new Alarm((int) hour, (int) minute, (String) content);

        Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
        objIntent.putExtra("alarm", al);
        startActivity(objIntent);
    }

    public void cancel(View v) {
        finish();
    }
}
