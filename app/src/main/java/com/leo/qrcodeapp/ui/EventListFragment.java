package com.leo.qrcodeapp.ui;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.leo.qrcodeapp.db.DatabaseHelper;
import com.leo.qrcodeapp.utils.AppUtilities;
import com.leo.qrcodeapp.utils.CommonFlags;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends ListFragment {

    int PROCESS_MODE = CommonFlags.INSTANCE.ACTION_LIST;
    DatabaseHelper dbConnector;
    private String TAG = "--list";
    private int TITLE = -1;

    // event list
    HashMap<Integer, HashMap<String, String>> mapTree = new HashMap<>();

    public EventListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // get filter argumments
        Bundle args = getArguments();

        if(args != null){
            PROCESS_MODE = args.getInt(CommonFlags.INSTANCE.INTENT_PROCESS_MODE);
            TITLE = args.getInt(CommonFlags.INSTANCE.INTENT_TOOLBAR_TITLE, -1);
            CommonFlags.INSTANCE.ACTIVE_SCREEN = args.getInt(CommonFlags.INSTANCE.INTENT_ACTIVE_SCREEN);
            Log.d(TAG, AppUtilities.INSTANCE.numberToString(PROCESS_MODE) + ", title: " + TITLE) ;
        }
    }


    /**
     * Main page program start, where UI elements are ready to be initialized.
     * Initialize UI layout elements and database handlers here
     */
    @Override
    public void onStart(){
        super.onStart();

        // disable back button click to return to login activity
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // prevent back button press
                if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
                    return true;
                }
                return false;
            }
        });

        dbConnector = DatabaseHelper.getsInstance(getContext());
        if(mapTree != null && mapTree.size() == 0){
            Log.d(TAG, "--listing data!");
            listData(CommonFlags.INSTANCE.CURRENT_YEAR);
        }
    }


    /**
     * Called anytime when a fragment is resumed from any state.
     * Initialize this fragment's topbar title, icons and other settings.
     */
    @Override
    public void onResume(){
        // Set active screen
        CommonFlags.INSTANCE.ACTIVE_SCREEN = CommonFlags.INSTANCE.SCREEN_LIST_EVENTS;

        super.onResume();
        // Set title bar and topbar icons
        ((MainQRActivity) getActivity()).setActionBarTitleFragment(TITLE);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        // left navigation drawer menu click handler
        getListView().setItemChecked(position, true);
        Log.d(TAG, "clicked " + mapTree.get(position).get(CommonFlags.INSTANCE.LIST_ID));
    }


    /**
     * Initiate the listing of database-loaded data to UI list on screen
     */
    public void listData(String year){
        int layout = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

        ArrayList<String> test = new ArrayList<>();

        for(Integer key : mapTree.keySet())
            test.add(AppUtilities.INSTANCE.numberToString(key));

        setListAdapter(new ListOverviewAdapter(getContext(), layout, mapTree, test));
    }
}
