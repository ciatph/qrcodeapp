package com.leo.qrcodeapp.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import java.io.File;

/**
 * Created by mbarua on 1/16/2018.
 */

public class RemovableStorage {
    public static File getConfirmedRemovableSDCardDirectory(Context context){
        if(Environment.isExternalStorageRemovable()){
            return Environment.getExternalStorageDirectory();
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            for(File directory : ContextCompat.getExternalFilesDirs(context, null)){
                if(Environment.isExternalStorageRemovable(directory)){
                    return directory;
                }
            }
        }

        return null;
    }


    public static File[] getPossibleRemovableSDCardDirectories(Context context){
        File confirmedLocation = getConfirmedRemovableSDCardDirectory(context);
        if(confirmedLocation != null){
            return new File[]{confirmedLocation};
        }
        return ContextCompat.getExternalFilesDirs(context, null);
    }
}
