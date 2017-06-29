package com.stgobain.samuha.CustomUserInterface;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.stgobain.samuha.R;


/**
 * Created by vignesh on 15-06-2017.
 */

public class CustomFontTextView extends AppCompatTextView {
    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
        applyCustomFont(context, attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode())
        applyCustomFont(context, attrs);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {
        TypedArray attributeArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.CustomFontTextView);

        String fontName = attributeArray.getString(R.styleable.CustomFontTextView_font);
        int textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);

        Typeface customFont = selectTypeface(context, fontName, textStyle);
        setTypeface(customFont);

        attributeArray.recycle();
    }

    private Typeface selectTypeface(Context context, String fontName, int textStyle) {
        if (fontName.contentEquals(context.getString(R.string.font_name_roboto_bold))) {
            return FontCache.getTypeface("Roboto-Bold.ttf", context);
        } else if (fontName.contentEquals(context.getString(R.string.font_name_roboto_medium))) {
              /*
              information about the TextView textStyle:
              http://developer.android.com/reference/android/R.styleable.html#TextView_textStyle
              */
            switch (textStyle) {
                case Typeface.BOLD: // bold
                    return FontCache.getTypeface("Roboto-Bold.ttf", context);

                case Typeface.ITALIC: // italic
                    return FontCache.getTypeface("Roboto-Italic.ttf", context);

                case Typeface.BOLD_ITALIC: // bold italic
                    return FontCache.getTypeface("Roboto-BoldItalic.ttf", context);

                case Typeface.NORMAL: // regular
                default:
                    return FontCache.getTypeface("Roboto-Medium", context);
            }

        } else if(fontName.contentEquals("SquareFont")){
            switch (textStyle) {
                case Typeface.BOLD: // bold
                    return FontCache.getTypeface("sqr721b.TTF", context);

                case Typeface.ITALIC: // italic
                    return FontCache.getTypeface("sqr721bi.TTF", context);

                case Typeface.BOLD_ITALIC: // bold italic
                    return FontCache.getTypeface("sqr721bi.TTF", context);

                case Typeface.NORMAL: // regular
                default:
                    return FontCache.getTypeface("sqr721n.TTF", context);
            }
        } else {
            // no matching font found
            // return null so Android just uses the standard font (Roboto)
            return null;
        }
    }
}
