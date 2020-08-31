package com.jojonarte.alarmmanagerspike;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class CustomAlarmReceiver extends BroadcastReceiver {
    private final String CHANNEL_ID = "NOTIFICATION_TSK";
    private final String REQUEST_TAG = "REQUEST_TAG";
    private final String URL = "https://us-central1-fir-study-98929.cloudfunctions.net/test";
    private final int NOTIF_ID = 999;
    private Context context;


    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        // registers NotificationChannel (Required for ANDROID O Above.)
        createNotificationChannel();
        makeRequest();
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "MEME CHANNEL";
            String description = "Channel for memes";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void makeRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        final String currentTime = new Date().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // CREATE NOTIFICATION HERE
                Notification successNotif = createNotification("Sucesss", "Request success at " + currentTime);
                showNotification(successNotif);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Notification errorNotif = createNotification("Error", error.getMessage() + " " + currentTime);
                showNotification(errorNotif);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("time", new Date().toString());
                params.put("message", "Hello from android");
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void showNotification(Notification notification) {
        NotificationManagerCompat notifManager = NotificationManagerCompat.from(this.context);
        notifManager.notify(NOTIF_ID, notification);
    }

    private Notification createNotification(String title, String message) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title).setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        return builder.build();
    }
}
