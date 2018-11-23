package com.riyol.permission;

import android.content.Context;
import android.support.v4.content.FileProvider;

public class ContentFileProvider extends FileProvider {

    public static String authority(Context context) {
        return context.getPackageName() + ".file.provider";
    }
}
