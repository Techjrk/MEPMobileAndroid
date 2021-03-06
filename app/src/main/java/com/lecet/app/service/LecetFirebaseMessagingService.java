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

import com.lecet.app.R;
import com.lecet.app.content.ProjectDetailActivity;
import com.lecet.app.utility.Log;

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
        Log.d(TAG, "RemoteMessage: " + remoteMessage.toString());
        sendNotification("LECET", "TEST RECEIVED", 322385);

        // Check if the message contains a data payload
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        }

        /*
                NSDictionary *aps = userInfo[@"aps"];
       id payLoad = aps[@"alert"];

       NSString *message = @"";
       if ([payLoad isKindOfClass:[NSDictionary class]]) {

           NSDictionary *alert = aps[@"alert"];
           NSDictionary *body = alert[@"body"];

           NSString *type = body[@"type"];

           NSString *title = alert[@"title"];
           NSString *detail = @"";

           if (![type isEqualToString:@"updatedProject"]) {
               detail = body[@"text"];
           }

           NSString *address = @"";

           NSString *address1 = [DerivedNSManagedObject objectOrNil:body[@"address1"]];
           NSString *address2 = [DerivedNSManagedObject objectOrNil:body[@"address2"]];
           NSString *city = [DerivedNSManagedObject objectOrNil:body[@"city"]];
           NSString *state = [DerivedNSManagedObject objectOrNil:body[@"state"]];
           NSString *zip = [DerivedNSManagedObject objectOrNil:body[@"zip5"]];

           if ( address1!= nil) {

               address = [[address stringByAppendingString:address1] stringByAppendingString:@" "];
               address = [address stringByAppendingString:[self addComma:address2]];
           }


           if (address2 != nil) {
               address = [[address stringByAppendingString:address2] stringByAppendingString:@" "];
               address = [address stringByAppendingString:[self addComma:city]];
           }

           if (city != nil) {
               address = [[address stringByAppendingString:city] stringByAppendingString:@" "];
               address = [address stringByAppendingString:[self addComma:state]];
           }

           if (state != nil) {
               address = [[address stringByAppendingString:state] stringByAppendingString:@" "];
               address = [address stringByAppendingString:[self addComma:zip]];

           }

           if (zip != nil) {
               address = [address stringByAppendingString:zip];
           }

           if (detail.length>0) {
               title = [[title stringByAppendingString:@"\n"] stringByAppendingString:detail];
           }

           message = [NSString stringWithFormat:@"%@\n%@", title, address];

       } else {
           message = payLoad;
       }
        [[DataManager sharedManager] promptMessage:message];
    }
         */

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
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

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
