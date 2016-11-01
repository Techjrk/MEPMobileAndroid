package com.lecet.app.content;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lecet.app.R;
import com.lecet.app.enums.LacetFont;
import com.lecet.app.utility.TextViewUtility;

/**
 * Created by Josué Rodríguez on 27/10/2016.
 */

public class LacetConfirmDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "LacetConfirmDialogFragment";

    private static final String ARG_MESSAGE = "message";
    private static final String ARG_CONFIRM_MESSAGE = "confirm_message";
    private static final String ARG_CANCEL_MESSAGE = "cancel_message";

    ConfirmDialogListener mListener;
    String message;
    String confirmMessage;
    String cancelMessage;

    public static LacetConfirmDialogFragment newInstance(String message, String confirmMessage, String cancelMessage) {
        LacetConfirmDialogFragment dialogFragment = new LacetConfirmDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        args.putString(ARG_CONFIRM_MESSAGE, confirmMessage);
        args.putString(ARG_CANCEL_MESSAGE, cancelMessage);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            message = getArguments().getString(ARG_MESSAGE);
            confirmMessage = getArguments().getString(ARG_CONFIRM_MESSAGE);
            cancelMessage = getArguments().getString(ARG_CANCEL_MESSAGE);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_confirm_dialog_layout, null, false);
        TextView labelMessage = (TextView) view.findViewById(R.id.label_message);
        TextView buttonConfirm = (TextView) view.findViewById(R.id.button_confirm);
        TextView buttonCancel = (TextView) view.findViewById(R.id.button_cancel);

        TextViewUtility.changeTypeface(labelMessage, LacetFont.LATO_REGULAR);
        TextViewUtility.changeTypeface(buttonConfirm, LacetFont.LATO_REGULAR);
        TextViewUtility.changeTypeface(buttonCancel, LacetFont.LATO_REGULAR);

        labelMessage.setText(message);
        buttonConfirm.setText(confirmMessage);
        buttonCancel.setText(cancelMessage);

        view.findViewById(R.id.button_confirm).setOnClickListener(this);
        view.findViewById(R.id.button_cancel).setOnClickListener(this);

        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.button_confirm) {
            if (mListener != null) {
                mListener.onDialogPositiveClick(this);
            }
        } else if (id == R.id.button_cancel) {
            if (mListener != null) {
                mListener.onDialogNegativeClick(this);
            }
        }
        dismiss();
    }

    public interface ConfirmDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);

        void onDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (ConfirmDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
