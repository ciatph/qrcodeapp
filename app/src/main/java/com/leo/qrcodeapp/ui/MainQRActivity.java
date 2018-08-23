package com.leo.qrcodeapp.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.leo.qrcodeapp.R;
import com.leo.qrcodeapp.db.DatabaseHelper;
import com.leo.qrcodeapp.events.EventStatus;
import com.leo.qrcodeapp.utils.ActionMenuHelper;
import com.leo.qrcodeapp.utils.AppUtilities;
import com.leo.qrcodeapp.utils.CommonFlags;
import com.leo.qrcodeapp.utils.MsgNotification;

import java.util.ArrayList;

public class MainQRActivity extends AppCompatActivity {

    // topbar
    ActionBar actionBar;

    private String TAG = "--Main";

    // topbar icons
    ArrayList<ActionBarActivatedIcons> actionMenuIconsDisabled = new ArrayList<>();
    ArrayList<ActionBarActivatedIcons> actionMenuIconsEnabled = new ArrayList<>();

    class ActionBarActivatedIcons{
        // TODO: 2/17/2017 remove this later. Use HashMap mapping instead
        int code;
        public ActionBarActivatedIcons(int code){
            this.code = code;
        }
    }

    // Sqlite
    DatabaseHelper dbConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.fragment_pager_container);
        setContentView(R.layout.activity_event_list);

        // Set the app visibility status
        // CommonFlags.INSTANCE.setAppVisibility(CommonFlags.INSTANCE.APP_ACTIVE);

        // toolbar and actionbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);

        switchFragments(new PagerContainer(), R.string.top_title_list_events,
                EventStatus.INSTANCE.SCR_LIST,null);
        // switchToMainScreen();
    }


    /**
     * Initialize the topbar menu icons for a current active screen.
     * Set the active and inactive (hidden) topbar icons
     * @param menu  The xml layout file for the topbar menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // set the current displayed actionbar icons
        getMenuInflater().inflate(R.menu.action_topbar, menu);

        // hide disabled topbar icons for current active screen
        if(actionMenuIconsDisabled.size() > 0) {
            for(int i=0; i<actionMenuIconsDisabled.size(); i++){
                menu.findItem(actionMenuIconsDisabled.get(i).code).setVisible(false);
            }
        }

        // display enabled topbar icons for current active screen
        if(actionMenuIconsEnabled.size() > 0){
            for(int i=0; i<actionMenuIconsEnabled.size(); i++){
                menu.findItem(actionMenuIconsEnabled.get(i).code).setVisible(true);
            }
        }

        return true;
    }


    /**
     * Process the clicked topbar icon actions
     * @param item  The clicked topbar menu item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item){
        if(EventStatus.INSTANCE.isApp(EventStatus.INSTANCE.APP_LOCKED)){
            MsgNotification.INSTANCE.displayMessage(
                    MsgNotification.INSTANCE.getMessage(MsgNotification.INSTANCE.MSG_APP_LOCKED));
            return false;
        }

        switch(item.getItemId()){
            case android.R.id.home: {
                return true;
            }

            case R.id.action_app_add: {
                EventStatus.INSTANCE.setAction(EventStatus.INSTANCE.ACTION_ADD);
                switchFragments(new AddEventFragment(), R.string.top_title_add, EventStatus.INSTANCE.SCR_VIEW,null);
                return true;
            } 

            case R.id.action_app_edit: {
                //EventStatus.INSTANCE.setAction(EventStatus.INSTANCE.ACTION_EDIT);
                return true;
            }
            case R.id.action_app_delete: {
                //EventStatus.INSTANCE.setAction(EventStatus.INSTANCE.ACTION_DELETE);
                return true;
            }
            case R.id.action_app_save: {
                //EventStatus.INSTANCE.setAction(EventStatus.INSTANCE.ACTION_SAVE);
                return true;
            }

            case R.id.action_app_cancel: {
                switchToMainScreen();
                return true;
            }
            default:
                return true;
        }
    }


    /**
     * Convenience function for switching to main (Listing) screen
     */
    public void switchToMainScreen(){
        EventStatus.INSTANCE.setScreen(EventStatus.INSTANCE.SCR_LIST_EVENTS);
        EventStatus.INSTANCE.setAppStatuss(EventStatus.INSTANCE.APP_FREE);

        // switch to the survey data listing UI view by default
        switchFragments(new PagerContainer(), R.string.top_title_list_events,
                EventStatus.INSTANCE.SCR_LIST_EVENTS, null);
    }


    /**
     * Switch the UI view to a sub-fragment.
     * The sub-fragment initiates changing its topbar title and icon upon loading
     * @param fragment      UI Fragment to display
     * @param menuTitle     Resource ID of the String title to set in the UI fragment
     * @param screenId      global SCREEN Id
     * @author mbarua on 10/27/2017.
     */
    public void switchFragments(Fragment fragment, int menuTitle, int screenId, String ID){
        CommonFlags.INSTANCE.PAGE_UI_LOADED = false;

        Log.d(TAG, "switching to frag " + AppUtilities.INSTANCE.numberToString(screenId));
        // set the current active screen
        EventStatus.INSTANCE.setScreen(screenId);

        // always include the "new" page status settings to pass the fragment by default
        /*
        Bundle bundle = new Bundle();

        // initialize the fragment's process mode (add, view, edit)
        bundle.putInt(CommonFlags.INSTANCE.INTENT_PROCESS_MODE, EventStatus.INSTANCE.getAction());

        // initialize the fragment's topbar title
        bundle.putInt(CommonFlags.INSTANCE.INTENT_TOOLBAR_TITLE, menuTitle);

        // initialize the fragment's active screen
        bundle.putInt(CommonFlags.INSTANCE.INTENT_ACTIVE_SCREEN, CommonFlags.INSTANCE.ACTIVE_SCREEN);

        // initialize the logged-in user ID for selected data
        bundle.putString(CommonFlags.INSTANCE.INTENT_USER_ID, getAuthUserId());

        // initialize the fragment's ID for selected data
        if(ID != null)
            bundle.putString(CommonFlags.INSTANCE.INTENT_DATA_ID, ID);

        fragment.setArguments(bundle);
        */
        //if(CommonFlags.INSTANCE.getAppViisibility() == CommonFlags.INSTANCE.APP_ACTIVE) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
        //}
    }


    /**
     * Get the current logged-in user's firebase auth uid
     * @return  logged-in user's firebase auth uid
     */
    public String getAuthUserId(){
        Log.d(TAG, "current user: " + CommonFlags.INSTANCE.CURRENT_USER.getId() + ", " + CommonFlags.INSTANCE.CURRENT_USER.get("username"));
        return CommonFlags.INSTANCE.CURRENT_USER.getId();
    }


    /**
     * Initialize the topbar icons for the current screen
     * @param disabled  array of disabled icon resource ID's from ActionMenuHelper
     * @param enabled   array of enabled icon resource ID's from ActionMenuHelper
     * @param resId     resource ID of string title
     */
    public void initializeActionBarIcons(int[] disabled, int[] enabled, int resId){
        getSupportActionBar().setTitle(resId);

        actionMenuIconsEnabled.clear();
        actionMenuIconsDisabled.clear();

        for(int i=0; i<disabled.length; i++)
            actionMenuIconsDisabled.add(new ActionBarActivatedIcons(disabled[i]));

        for(int i=0; i<enabled.length; i++)
            actionMenuIconsEnabled.add(new ActionBarActivatedIcons(enabled[i]));

        this.invalidateOptionsMenu();
    }


    /**
     * Set the titlebar's title and load associated menu icons.
     * Must be called from Fragments' onResume() method, for proper menu settings on back pressed status and resume
     * @param newTitleID  title for the menu: ACTION_VIEW, ACTION_EDIT, ACTION_LIST from CommonFlags
     */
    public void setActionBarTitleFragment(int newTitleID){
        // set the menu title
        //getSupportActionBar().setTitle(newTitleID);

        // organize topbar icons
        switch(newTitleID){
            case R.string.top_title_login:
            case R.string.top_title_registration:
            case R.string.top_title_delete:
                break;
            case R.string.top_title_list_events:
                initializeActionBarIcons(ActionMenuHelper.INSTANCE.SCREEN_FARMERLIST_DISABLED_ICONS,
                        ActionMenuHelper.INSTANCE.SCREEN_FARMERLIST_ENABLED_ICONS, newTitleID);
                break;
            case R.string.top_title_view:
                // check if data is synced online
                initializeActionBarIcons(ActionMenuHelper.INSTANCE.SCREEN_VIEW_DISABLED_ICONS,
                        ActionMenuHelper.INSTANCE.SCREEN_VIEW_ENABLED_ICONS, newTitleID);
                break;
            case R.string.top_title_add:
                initializeActionBarIcons(ActionMenuHelper.INSTANCE.SCREEN_CREATE_DISABLED_ICONS,
                        ActionMenuHelper.INSTANCE.SCREEN_CREATE_ENABLED_ICONS, newTitleID);
                break;
            case R.string.top_title_edit:
                if(CommonFlags.INSTANCE.CURRENT_USER.getId() == null){
                    MsgNotification.INSTANCE.displayMessage(
                            MsgNotification.INSTANCE.getMessage(MsgNotification.INSTANCE.MSG_NO_USER));
                    return;
                }
                initializeActionBarIcons(ActionMenuHelper.INSTANCE.SCREEN_EDIT_DISABLED_ICONS,
                        ActionMenuHelper.INSTANCE.SCREEN_EDIT_ENABLED_ICONS, newTitleID);
                break;
            case R.string.top_title_about:
            case R.string.top_title_search:
            default: break;
        }

        // TODO: 10/27/2017 set the topbar menu icons
    }
}
