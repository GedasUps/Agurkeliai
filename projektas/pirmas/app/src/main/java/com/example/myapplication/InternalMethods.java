package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class InternalMethods {
    public static void setProfilePic(Context context, Uri ImageUri, ImageView imageView)
    {
        Glide.with(context).load(ImageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
}
