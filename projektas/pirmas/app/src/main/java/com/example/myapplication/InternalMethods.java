package com.example.myapplication;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class InternalMethods {
    public static void setProfilePic(Context context, Uri ImageUri, ImageView imageView)
    {
        Glide.with(context).load(ImageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
    public static void setBorderBackground(View view, int backgroundColor, int borderColor, float cornerRadius, int borderWidth) {
        GradientDrawable borderDrawable = new GradientDrawable();
        borderDrawable.setShape(GradientDrawable.RECTANGLE);
        borderDrawable.setCornerRadius(cornerRadius);
        borderDrawable.setColor(backgroundColor);
        borderDrawable.setStroke(borderWidth, borderColor);
        view.setBackground(borderDrawable);
    }
}
