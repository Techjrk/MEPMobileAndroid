package com.lecet.app.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StyleRes;

/**
 * Created by alionque on 6/15/17.
 */

public class SimpleLecetDefaultAlert extends AlertDialog {
    public static final int HTTP_CALL_ERROR = 10;
    public static final int NETWORK_FAILURE = 15;
    public static final int REALM_ERROR = 20;
    public static final int CUSTIOM_DIALOG = 999;
    private String message;

    protected SimpleLecetDefaultAlert(Context context) {
        super(context);
    }

    protected SimpleLecetDefaultAlert(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected SimpleLecetDefaultAlert(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public static SimpleLecetDefaultAlert newInstance(Activity activity, int dialogType, OnClickListener clickListener, OnDismissListener dismissListener){
        SimpleLecetDefaultAlert instance = new SimpleLecetDefaultAlert(activity);

        instance.message = getMessage(dialogType);
        instance.setMessage(instance.message);
        instance.setTitle("Error");
        instance.setButton(BUTTON_NEUTRAL, instance.message, clickListener);
        instance.setOnDismissListener(dismissListener);

        return instance;
    }

    public static SimpleLecetDefaultAlert newInstance(final Activity activity, int dialogType, final OnClickListener listener){
        final SimpleLecetDefaultAlert instance = new SimpleLecetDefaultAlert(activity);

        instance.message = getMessage(dialogType);
        instance.setMessage(instance.message);
        instance.setTitle("Error");
        instance.setButton(BUTTON_NEUTRAL, instance.message, listener);
        instance.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                listener.onClick(instance, AlertDialog.BUTTON_NEUTRAL);
            }
        });

        return instance;
    }

    public static SimpleLecetDefaultAlert newInstance(final Activity activity, int dialogType){
        SimpleLecetDefaultAlert instance = new SimpleLecetDefaultAlert(activity);

        instance.message = getMessage(dialogType);
        instance.setMessage(instance.message);
        instance.setTitle("Error");
        instance.setButton(BUTTON_NEUTRAL, instance.message, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });
        instance.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                activity.finish();
            }
        });

        return instance;
    }

    private static String getMessage(int dialogType){
        String result = "";
        switch (dialogType){
            case HTTP_CALL_ERROR:
                result = "Server Error Code: (" + HTTP_CALL_ERROR + ")\nThis process " +
                        "could not be finished";
                break;
            case NETWORK_FAILURE:
                result = "Network Failure:\nThis process could not be finished";
                break;
            case REALM_ERROR:
                result = "Realm Error\nThis process could not be finished";
                break;
        }
        return result;
    }
}
