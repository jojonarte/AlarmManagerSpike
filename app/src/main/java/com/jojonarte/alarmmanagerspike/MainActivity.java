package com.jojonarte.alarmmanagerspike;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final int ALARM_REQ_ID = 7777;
    EditText numInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.numInput = findViewById(R.id.num_input);
    }

    public void onScheduleClick(View v) {
        createAlarm();
    }

    private void createAlarm() {
        String numInputText = numInput.getText().toString();

        if (numInputText.isEmpty()) {
            numInput.setError("Plz fill valid number");
            return;
        }
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, CustomAlarmReceiver.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        int numInputValue = Integer.parseInt(numInputText);
        long timeToTrigger = numInputValue * 1000; // minutes to trigger based on passed minute value in input
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                ALARM_REQ_ID, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setExact(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + timeToTrigger,
                pendingIntent);
    }

}
