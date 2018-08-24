package com.leo.qrcodeapp.ui;


import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.leo.qrcodeapp.R;
import com.leo.qrcodeapp.db.DatabaseHelper;
import com.leo.qrcodeapp.events.EventStatus;
import com.leo.qrcodeapp.models.Event;
import com.leo.qrcodeapp.ui.adapters.ListOverviewAdapter;
import com.leo.qrcodeapp.utils.AppUtilities;
import com.leo.qrcodeapp.utils.CommonFlags;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends ListFragment {

    int PROCESS_MODE = EventStatus.INSTANCE.ACTION_LIST;
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
            listData();
        }
    }


    /**
     * Called anytime when a fragment is resumed from any state.
     * Initialize this fragment's topbar title, icons and other settings.
     */
    @Override
    public void onResume(){
        // Set active screen
        EventStatus.INSTANCE.setScreenView(
                EventStatus.INSTANCE.SCR_LIST_EVENTS,
                EventStatus.INSTANCE.ACTION_LIST,
                R.string.top_title_list_events);

        super.onResume();
        // Set title bar and topbar icons
        ((MainQRActivity) getActivity()).setActionBarTitleFragment(R.string.top_title_list_events);
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
    public void listData(){
        int layout = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

        ArrayList<String> test = new ArrayList<>();
        mapTree = loadData();

        for(Integer key : mapTree.keySet()){
            Log.d(TAG, "assigning " + key + "");
            test.add(AppUtilities.INSTANCE.numberToString(key));

        }

        setListAdapter(new ListOverviewAdapter(getContext(), layout, mapTree, test));
    }


    public HashMap<Integer, HashMap<String, String>> loadData(){
        HashMap<Integer, HashMap<String, String>> map = new HashMap<>();
        Cursor c = null;

        c = dbConnector.searchDbColumns(Event.tablename,
                AppUtilities.INSTANCE.stringArrayAppend(DatabaseHelper.getClassFieldNames(Event.class), new String[]{"_id"}),
                new String[]{Event.acct_id},
                new String[]{
                        "'" + CommonFlags.INSTANCE.CURRENT_USER.getId() + "'"});

        if(c.moveToFirst()){
            do{
                HashMap<String, String> temp = new HashMap<>();
                temp.put(CommonFlags.INSTANCE.LIST_HEADER, "Event: " + c.getString((c.getColumnIndex(Event.name))));
                temp.put(CommonFlags.INSTANCE.LIST_SUBTOPIC_01, "Venue: " + c.getString(c.getColumnIndex(Event.venue)));
                temp.put(CommonFlags.INSTANCE.LIST_SUBTOPIC_02, "Date: " + c.getString(c.getColumnIndex(Event.date_event)));
                temp.put(CommonFlags.INSTANCE.LIST_SUBTOPIC_03, "Created: " + c.getString(c.getColumnIndex(Event.date_created)));
                temp.put(CommonFlags.INSTANCE.LIST_ID, c.getString(c.getColumnIndex("_id")));
                map.put(map.size(), temp);
            } while(c.moveToNext());
        }
        c.close();
        return map;
    }
}
