package com.leo.qrcodeapp.utils;

import com.leo.qrcodeapp.R;
import com.leo.qrcodeapp.models.Account;


/**
 * Created by mbarua on 10/19/2017.
 * A list of constant global variables and references
 */

public enum CommonFlags {
    INSTANCE;

    public boolean PAGE_UI_LOADED = false;
    public String USERID = null;

    public Account CURRENT_USER;

    // Counter for db and journal file upload success
    public int SYNC_FILE_COUNT = 0;

    
    // logout screen
    // TODO: 10/27/2017 remove later?
    public final int SCREEN_LOGOUT = 22;

    // year selection
    public final int ACTION_SELECT_YEAR = 23;

    public final String LIST_HEADER = "header";             // event name
    public final String LIST_SUBTOPIC_01 = "subtopic_01";   // event date
    public final String LIST_SUBTOPIC_02 = "subtopic_02";   // user/encoder
    public final String LIST_ID = "_did";
    public final String LIST_ISSYNCED = "_isSynced";
    public final String LIST_YEAR = "_year";

    /**
     * Running app status
     */
    public final int PERMISSIONS_CAMERA = 27;
    public final int REQUEST_IMAGE_CAPTURE = 28;
    public final int BTN_ACTION_CAMERA = 29;
    public final int BTN_ACTION_CLEAR = 30;

    public final int LIFECYCLE_PAUSED = 31;
    public final int LIFECYCLE_RESUMED = 32;
    public int LIFECYCLE_STATE = LIFECYCLE_RESUMED;

    public final int APP_ACTIVE = 33;
    public final int APP_IDLE = 34;
    public final int APP_TERMINATED = 35;
    public int APP_VISIBILITY = APP_TERMINATED;
}
