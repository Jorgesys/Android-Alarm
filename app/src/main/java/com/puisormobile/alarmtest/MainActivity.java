package com.puisormobile.alarmtest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //AlarmManager alarmManager;
    private static final String TAG = "Alarm!";
    private static final int NOTIFICATION_ID = 12345;
    private static final String MSG_ALARM_CANCELED = "<br><b><font color='#FF0000'>Alarm canceled!</font></b>";
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         textView = (TextView)findViewById(R.id.tvMessage);
        Button btnCancel = (Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cancelling alarm!
                Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                PendingIntent sender = PendingIntent.getBroadcast(getBaseContext(), NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.cancel(sender);
                Log.i(TAG, "Cancelling Alarm with id :: " + NOTIFICATION_ID);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textView.setText(Html.fromHtml(MSG_ALARM_CANCELED, Html.FROM_HTML_MODE_LEGACY));
                }else{
                    textView.setText(Html.fromHtml(MSG_ALARM_CANCELED));
                }
            }
        });

        try {
            setAlarm(NOTIFICATION_ID);
        } catch (ParseException e) {
            Log.e(TAG, "EXCEPTION ::: " + e.getMessage());
        }

    }

    private void setAlarm(int notification_id) throws ParseException {
        Log.i(TAG, "setAlarm() ::: Start configuration.");

        Date dat = new Date();
        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();
        cal_now.setTime(dat);
        cal_alarm.setTime(dat);

        if(cal_alarm.before(cal_now)){
            cal_alarm.add(Calendar.SECOND, 30); //30 seconds more than the actual date!
        }

        Log.i(TAG, "Alarm date setted :: " + cal_alarm.getTime());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml("<br><b><font color='#00FF00'>Alarm setted @ " + cal_alarm.getTime()+"</font></b>",Html.FROM_HTML_MODE_LEGACY));
        }else{
            textView.setText(Html.fromHtml("<br><b><font color='#00FF00'>Alarm setted @ " + cal_alarm.getTime()+"</font></b>"));
        }
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);

        intent.putExtra("title", "Alarm by Jorgesys!");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(),
                notification_id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), pendingIntent);
        Log.i(TAG, "setAlarm() ::: end Alarm configuration.");
    }


}
