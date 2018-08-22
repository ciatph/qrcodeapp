package com.leo.qrcodeapp.utils;

import com.leo.qrcodeapp.R;
import com.leo.qrcodeapp.models.Account;


/**
 * Created by mbarua on 10/19/2017.
 * A list of constant global variables and references
 */

public enum CommonFlags {
    INSTANCE;

    public boolean USER_SIGNED_IN = false;
    public boolean PAGE_UI_LOADED = false;
    public String USERID = null;
    public int MAX_PLOT_COUNT = 3;

    public Account CURRENT_USER;
    public boolean GUEST_USER = true;

    // Timeout delay when saving new plots data
    public int TIMEOUT_SAVE_DATA = 3000;

    // Timeout delay to wait for network connection when syncing a data record
    public int TIMEOUT_SYNC_DATA = 60000; // 60000 = 1min

    // Timeout delay when syncing a single data record
    public int TIMEOUT_SYNC_RECORD = 1900;

    // Counter for db and journal file upload success
    public int SYNC_FILE_COUNT = 0;

    /** Current year settings */
    public String YEAR_2014 = "2014";
    public String YEAR_2015 = "2015";
    public String CURRENT_YEAR = YEAR_2014;

    public int CURRENT_FARMER_INDEX = 0;

    // Current farmer _did that is being synced
    public String CURRENT_SYNC_ID = "";
    public String PREVIOUS_SYNC_ID = "";

    // Current year that is being synced
    public String CURRENT_SYNC_YEAR = "";

    public final int SYNC_MODE_BATCH = 1;
    public final int SYNC_MODE_SOLO = 2;
    public int SYNC_MODE = SYNC_MODE_SOLO;

    public int SYNC_BATCH_COUNTER = 0;
    public int SYNC_BATCH_COUNTER_MAX = 0;

    /** Last visited plots-related fragment page */
    public int CURRENT_PAGE = 0;

    /**
     * Data process ID's
     */
    // user action for data entry is add new data
    public final int ACTION_ADD_FARMER = 1;

    public final int ACTION_ADD_PLOT = 2;

    // user action for data entry is edit existing farmer's plot(s) data
    public final int ACTION_EDIT = 3;

    // user action for data entry is edit existing farmer information
    public final int ACTION_EDIT_FARMER = 4;

    // user action for data is view existing data
    public final int ACTION_VIEW = 5;

    // user action for data entry is delete existing data
    public final int ACTION_DELETE = 6;

    // user action is view a summary list of existing data
    public final int ACTION_LIST = 7;

    // user action for data entry sync/upload/save to online firebase
    public final int ACTION_SYNC = 8;

    // user action for data entry is export data to file
    // TODO: 10/27/2017 to add later
    public final int ACTION_EXPORT = 9;

    // user action for data entry is press the back button (added for fragment control)
    public final int ACTION_BACK = 10;


    /**
     * Page UI screen ID's
     */
    // login screen
    public final int SCREEN_LOGIN = 11;

    // account registration screen
    public final int SCREEN_REGISTER = 12;

    // entered farmer information summary list screen
    public final int SCREEN_LIST_FARMER = 13;

    // entered farmer information summary list screen
    public final int SCREEN_LIST_PLOT = 14;

    // offline to online firebase  synchronization screen
    public final int SCREEN_DATA_SYNC = 15;

    // screen status for add, view, edit data farmer information data
    public final int SCREEN_DATA_FARMER = 16;

    // screen status for add, view, edit data farmer's plot(s) data
    public final int SCREEN_DATA_PLOT = 17;

    // user account/profile management screen
    public final int SCREEN_ACCOUNT = 18;

    // list of plots for a farmer
    public final int SCREEN_FARMER_DATA_LIST = 19;

    // search page
    public final int SCREEN_SEARCH = 20;

    // about page
    public final int SCREEN_ABOUT = 21;

    // Screens that are not the main farmer list screen
    public final int MISC_SCREENS[] = { SCREEN_DATA_SYNC, SCREEN_ACCOUNT, SCREEN_SEARCH, SCREEN_ABOUT };
    
    // logout screen
    // TODO: 10/27/2017 remove later?
    public final int SCREEN_LOGOUT = 22;

    // year selection
    public final int ACTION_SELECT_YEAR = 23;

    public final String LIST_HEADER = "header";             // fname + lname
    public final String LIST_SUBTOPIC_01 = "subtopic_01";   // isSynced
    public final String LIST_SUBTOPIC_02 = "subtopic_02";   // mobile #
    public final String LIST_SUBTOPIC_03 = "subtopic_03";   // date created
    public final String LIST_ID = "_did";
    public final String LIST_ISSYNCED = "_isSynced";
    public final String LIST_YEAR = "_year";

    public final String EXTRA_TREE_PHOTO = "com.example.treecode.TREE_PHOTO";

    /**
     * Running app status
     */
    public final int APP_FREE = 24;
    public final int APP_LOCKED = 25;
    public final int APP_CORRUPT = 26;
    public int APP_STATUS = APP_FREE;

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

    public String mCurrentphoto;


    /**
     * Date conversion output
     */
    public final int DATE_SIMPLE = 1;
    public final int DATE_UNIX = 2;
    public final int DATE_READABLE = 3;


    /**
     * Topbar Titles. Default is EN, change to appropriate language via strings at runtime
     * strings.xml: 'topbar titles'
     */
    public int TTTLE_LOGIN = R.string.top_title_login;
    public int TTTLE_REGISTRATION = R.string.top_title_registration;
    public int TTTLE_ADD = R.string.top_title_add;
    public int TTTLE_VIEW = R.string.top_title_view;
    public int TTTLE_EDIT = R.string.top_title_edit;
    public int TTTLE_DELETE = R.string.top_title_delete;
    public int TTTLE_LIST = R.string.top_title_list;


    /**
     * Current global status values
     */
    // the current active screen
    public int ACTIVE_SCREEN = SCREEN_LIST_FARMER;

    // the current active user's data processing action
    public int PROCESS_MODE = ACTION_LIST;

    // the current topbar title
    public int TOPBAR_TITLE = TTTLE_LIST;


    /**
     * Intent flags and filters
     */
    // pairs with the current ACTION_MODE
    public String INTENT_PROCESS_MODE = "PROCESS_MODE";

    // pairs with the current ACTIVE_SCREEN
    public String INTENT_ACTIVE_SCREEN = "INTENT_ACTIVE_SCREEN";

    // pairs with the current ACTIVE_SCREEN
    public String INTENT_TOOLBAR_TITLE = "INTENT_TOOLBAR_TITLE";

    // data ID
    public String INTENT_DATA_ID = "INTENT_DATA_ID";

    // user firebase/sqlite ID
    public String INTENT_USER_ID = "INTENT_USER_ID";

    // year associated with this data
    public String INTENT_DATA_YEAR = "INTENT_DATA_YEAR";

    // data is synced with Firebase
    public String INTENT_DATA_SAVED_ONLINE = "INTENT_DATA_SAVED_ONLINE";

    public String DATA_ARG_KEY = "DATA_ARG_KEY";

    public final int PAGER_FARMER = 0;
    public final int PAGER_FARM = 1;

    public void setCurrentScreen(int screenId){
        CommonFlags.INSTANCE.ACTIVE_SCREEN = screenId;
    }


    /**
     * Initialize current settings with default values.
     * User is assumed to have logged in.
     */
    public void reset(){
        // screen status
        ACTIVE_SCREEN = SCREEN_LIST_FARMER;

        // data process status
        PROCESS_MODE = ACTION_LIST;

        PAGE_UI_LOADED = false;
    }


    /**
     * Set the flags for app visibility
     * @param appVisibility
     */
    public void setAppVisibility(int appVisibility){
        if(appVisibility == APP_ACTIVE || appVisibility == APP_IDLE)
            APP_VISIBILITY = appVisibility;
    }


    /**
     * Get the current app visibility status
     * @return
     */
    public int getAppViisibility(){
        return APP_VISIBILITY;
    }


    /**
     * Set the apps' sync mode
     * @param mode  SYNC_BATCH|SYNC_MODE_SOLO
     */
    public void setSyncMode(int mode){
        SYNC_MODE = mode;
    }


    /**
     * Get the current sync mode
     * @return
     */
    public int getSyncMode(){
        return SYNC_MODE;
    }
}
