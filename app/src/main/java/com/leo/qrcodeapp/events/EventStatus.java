package com.leo.qrcodeapp.events;

import com.leo.qrcodeapp.R;

public enum EventStatus {
    INSTANCE;

    // actions
    public final int ACTION_ADD = 1;

    public final int ACTION_EDIT = 2;

    public final int ACTION_VIEW = 3;

    public final int ACTION_DELETE = 4;

    public final int ACTION_SAVE = 5;

    public final int ACTION_SCAN = 6;

    public final int ACTION_LIST = 7;

    public final int ACTION_BACK = 8;

    public final int ACTION_EXPORT = 9;

    // screens
    public final int SCR_LIST = 1;

    public final int SCR_LIST_EVENTS = 2;

    public final int SCR_VIEW = 3;

    public final int SCR_DATA = 4;

    public final int SCR_ABOUT = 5;

    public final int SCR_LOGIN = 6;

    public final int SCR_REG = 7;

    // app
    public final int APP_FREE = 1;

    public final int APP_LOCKED = 2;

    public final int APP_CORRUPT = 3;

    // screen titles

    public final String TITLE_LIST = "List";

    public final String TITLE_LIST_EVENTS = "Events List";

    public final String TITLE_ADD = "Add";

    public final String TITLE_EDIT = "Edit";

    public final String TITLE_VIEW = "View";

    public final String TITLE_DATA = "Manage Data";

    public final String TITLE_ABOUT = "About";

    // intent

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

    // variables
    public int ACTION = 0;

    public int SCREEN = SCR_LOGIN;

    public int APP_STATUS = APP_FREE;

    public int TITLE = R.string.top_title_list_events;

    // event types: data group thats currently being processed
    public final int DATA_EVENT = 1;
    public final int DATA_GUEST = 2;

    public void setAction(int action){
        ACTION = action;
    }

    public void setScreenView(int screen, int actiom, int titleResourceId){
        SCREEN = screen;
        ACTION = actiom;
        TITLE = titleResourceId;
    }

    public void setScreen(int screen){
        SCREEN = screen;
    }

    public void setAppStatuss(int appstatus){
        APP_STATUS = appstatus;
    }

    public int getAction(){
        return ACTION;
    }

    public int getScreen(){
        return SCREEN;
    }

    public int getAppstatus(){
        return APP_STATUS;
    }

    public boolean isAction(int action){
        return (ACTION == action) ? true : false;
    }

    public boolean isScreen(int screen){
        return (SCREEN == screen) ? true : false;
    }

    public boolean isApp(int appstatus){
        return (APP_STATUS == appstatus) ? true : false;
    }
}
