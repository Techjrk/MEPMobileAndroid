package com.lecet.app.service;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.lecet.app.R;
import com.lecet.app.content.DashboardIntermediaryActivity;
import com.lecet.app.content.ProjectDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * File: LecetFirebaseMessagingService
 * Created: 5/15/17
 * Author: domandtom
 *
 * This code is copyright (c) 2017 Dom & Tom Inc.
 */

public class LecetFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "LecetFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        // Check if the message contains a data payload
        if (remoteMessage.getData().size() > 0) {

            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            Map<String, String> payload = remoteMessage.getData();

            try {

                StringBuilder titleSb = new StringBuilder();
                titleSb.append(payload.get("title"));

                JSONObject body = new JSONObject(payload.get("body"));

                if (!body.isNull("type") && !body.getString("type").equals("updatedProject")) {
                    titleSb.append("\n");
                    titleSb.append(body.getString("text"));
                }

                StringBuilder addySb = new StringBuilder();
                if (!body.isNull("address1")) {
                    addySb.append(body.getString("address1"));
                    addySb.append(" ");
                }

                if (!body.isNull("address2")) {
                    addySb.append(body.getString("address2"));
                    addySb.append(" ");
                }

                if (!body.isNull("city")) {
                    addySb.append(body.getString("city"));
                    addySb.append(" ");
                }

                if (!body.isNull("state")) {
                    addySb.append(body.getString("state"));
                    addySb.append(" ");
                }

                if (!body.isNull("zip")) {
                    addySb.append(body.getString("zip"));
                    addySb.append(" ");
                }

                long projectId = -1;

                if (!body.isNull("projectId")) {

                    projectId = body.getLong("projectId");
                }

                sendNotification(titleSb.toString(), addySb.toString(), projectId);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param title FCM title received
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String title, String messageBody, long projectId) {

        Intent intent = new Intent(this, ProjectDetailActivity.class);
        intent.putExtra(ProjectDetailActivity.PROJECT_ID_EXTRA, projectId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DashboardIntermediaryActivity.class);
        stackBuilder.addNextIntentWithParentStack(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
