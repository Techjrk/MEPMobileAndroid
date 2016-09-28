package com.lecet.app.utility;

import android.databinding.BindingAdapter;
import android.support.design.widget.TextInputLayout;

/**
 * File: BindingAdapter Created: 8/23/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */
public class LecetBindingAdapter {

    @BindingAdapter("app:errorText")
    public static void setErrorMessage(TextInputLayout view, String errorMessage) {
        view.setError(errorMessage);
    }

}
