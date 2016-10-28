package com.lecet.app.content;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.lecet.app.R;
import com.lecet.app.databinding.FragmentConfirmDialogLayoutBinding;

/**
 * Created by Josué Rodríguez on 27/10/2016.
 */

public class LacetConfirmDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String ARG_MESSAGE = "message";
    private static final String ARG_CONFIRM_MESSAGE = "confirm_message";
    private static final String ARG_CANCEL_MESSAGE = "cancel_message";

    FragmentConfirmDialogLayoutBinding binding;
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
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirm_dialog_layout, null, false);

        builder.setView(binding.getRoot());
        return builder.create();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.labelMessage.setText(message);
        binding.buttonConfirm.setText(confirmMessage);
        binding.buttonCancel.setText(cancelMessage);

        binding.buttonConfirm.setOnClickListener(this);
        binding.buttonCancel.setOnClickListener(this);
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
                dismiss();
            }
        }
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
