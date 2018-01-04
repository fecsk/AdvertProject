package com.example.asus.advertproject.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Amos1 on 2018-01-01.
 */

public class Permissions {
    public static final int PERMISSION_KEY = 254;
    public static final String permissions[] = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Requests user permission to all specified data.
     * In this case location and media.
     *
     * @param context
     * @param permissions array of permissions required
     * @param PERMISSION_KEY
     */
    public static void permissionRequest(Context context, String permissions[], int PERMISSION_KEY) {
        ActivityCompat.requestPermissions((Activity) context, permissions, PERMISSION_KEY);
    }
}