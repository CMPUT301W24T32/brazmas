package com.CMPUT301W24T32.brazmascheckin.helper;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

/**
 * This class represents the device ID entity.
 */
public class DeviceID {

    /**
     * This method returns the device ID
     * @param context information about the application environment
     * @return the device ID
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceID(Context context) {
        String deviceID = null;
        if(context != null) {
            deviceID = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        return deviceID;
    }
}
