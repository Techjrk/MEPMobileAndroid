package com.lecet.app.utility;

import android.graphics.Typeface;
import android.widget.TextView;

import com.lecet.app.enums.LacetFont;

/**
 * Created by Josué Rodríguez on 20/10/2016.
 */

public class TextViewUtility {

    public static void changeTypeface(TextView textView, LacetFont font) {
        Typeface tf = Typeface.createFromAsset(textView.getContext().getAssets(), font.getFont());
        textView.setTypeface(tf);
    }
}
