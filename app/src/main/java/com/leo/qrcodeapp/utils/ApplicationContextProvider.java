package com.leo.qrcodeapp.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by mbarua on 10/11/2017.
 * Provides application-wide context reference. But not applicable for layouts
 */

public class ApplicationContextProvider extends Application {

    /**
     * Keeps a reference of the application context
     */
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();

    }

    /**
     * Returns the application context
     *
     * @return application context
     */
    public static Context getContext() {
        return sContext;
    }

}
