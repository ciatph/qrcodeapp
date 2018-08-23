package com.leo.qrcodeapp.utils;

import com.leo.qrcodeapp.R;

/**
 * Created by mbarua on 11/2/2017.
 * ActionMenuHelper is a definition of enabled and disabled menu icon layout resource ID's references for
 * AppDataCollects's ActionMenu topbar, on different screen pages.
 * {@code SCREEN_FARMERLIST_ENABLED_ICONS} action menu icon set for Farmer Data List
 * {@code SCREEN_CREATE_ENABLED_ICONS} action menu icon set for {@code create/add} page
 * {@code SCREEN_VIEW_ENABLED_ICONS} action menu icon set for {@code viewing} a single saved data entry page
 * {@code SCREEN_EDIT_ENABLED_ICONS} action menu icon set for edit data entry page
 */

public enum ActionMenuHelper {
    INSTANCE;

    // action icon set for the list fragment
    public int [] SCREEN_FARMERLIST_ENABLED_ICONS = { R.id.action_app_add };
    public int[] SCREEN_FARMERLIST_DISABLED_ICONS = { R.id.action_app_save, R.id.action_app_cancel, R.id.action_app_delete, R.id.action_app_edit };

    // action icon set for the plot List fragment
    public int [] SCREEN_PLOTLIST_ENABLED_ICONS = { R.id.action_app_add };
    public int[] SCREEN_PLOTLIST_DISABLED_ICONS = { R.id.action_app_save, R.id.action_app_cancel, R.id.action_app_delete, R.id.action_app_edit };

    // action icon set for Add new data
    public int [] SCREEN_CREATE_ENABLED_ICONS = { R.id.action_app_save, R.id.action_app_cancel };
    public int[] SCREEN_CREATE_DISABLED_ICONS = { R.id.action_app_add, R.id.action_app_delete, R.id.action_app_edit };

    // action icon set for Edit data
    public int [] SCREEN_EDIT_ENABLED_ICONS = { R.id.action_app_save, R.id.action_app_cancel };
    public int[] SCREEN_EDIT_DISABLED_ICONS = { R.id.action_app_add, R.id.action_app_edit, R.id.action_app_delete };

    // action icon set for View (Firebase unsaved) data, if there is network connection. Sync is enabled
    public int [] SCREEN_VIEW_ENABLED_ICONS = { R.id.action_app_edit,  R.id.action_app_cancel,  R.id.action_app_delete };
    public int[] SCREEN_VIEW_DISABLED_ICONS = { R.id.action_app_add, R.id.action_app_save };

    // action icon set for View (Firebase unsaved) data, if there is network connection. Sync is enabled but delete is disabled, at least 2014 or 2015 data has been synced
    public int [] SCREEN_VIEW_TOSYNC_ENABLED_ICONS = { R.id.action_app_edit,  R.id.action_app_cancel };
    public int[] SCREEN_VIEW_TOSYNC_DISABLED_ICONS = { R.id.action_app_add, R.id.action_app_save };

    // action icon set for View (Firebase unsaved) data, if there is no network connection. Sync is disabled
    public int [] SCREEN_VIEW_NETERR_ENABLED_ICONS = { R.id.action_app_delete, R.id.action_app_edit,  R.id.action_app_cancel };
    public int[] SCREEN_VIEW_NETERR_DISABLED_ICONS = { R.id.action_app_add, R.id.action_app_save };

    // action icon set for View (Firebase unsaved) data, if there is no network connection. Sync is disabled, at least 2014 or 2015 data has been synced
    public int [] SCREEN_VIEW_NETERR_TOSYNC_ENABLED_ICONS = { R.id.action_app_edit,  R.id.action_app_cancel };
    public int[] SCREEN_VIEW_NETERR_TOSYNC_DISABLED_ICONS = { R.id.action_app_add, R.id.action_app_save };

    // action icon set for View (Firebase saved) data
    public int [] SCREEN_VIEW_SYNCED_ENABLED_ICONS = { R.id.action_app_cancel };
    public int[] SCREEN_VIEW_SYNCED_DISABLED_ICONS = { R.id.action_app_add, R.id.action_app_save, R.id.action_app_delete, R.id.action_app_edit };

    // action icon set for Plot view pages
    public int [] SCREEN_PLOT_ENABLED_ICONS = { R.id.action_app_edit,  R.id.action_app_cancel, R.id.action_app_delete };
    public int[] SCREEN_PLOT_DISABLED_ICONS = { R.id.action_app_add, R.id.action_app_save };

    // action icon set for Plot view pages unsynced
    public int [] SCREEN_PLOT_UNSYNCED_ENABLED_ICONS = { R.id.action_app_edit,  R.id.action_app_cancel, R.id.action_app_delete };
    public int[] SCREEN_PLOT_UNSYNCED_DISABLED_ICONS = { R.id.action_app_add, R.id.action_app_save };

    // action icon set for Standalone pages
    public int [] SCREEN_SOLO_ENABLED_ICONS = { R.id.action_app_cancel };
    public int[] SCREEN_SOLO_DISABLED_ICONS = { R.id.action_app_add, R.id.action_app_save, R.id.action_app_delete };
}
