package com.leo.qrcodeapp.ui;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leo.qrcodeapp.R;
import com.leo.qrcodeapp.utils.AppUtilities;
import com.leo.qrcodeapp.utils.CommonFlags;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mbarua on 12/6/2017.
 * A custom adapter for Lists.
 * Uses the view holder pattern, a container for one or more Views.
 * Provides customizable Views than using default ArrayList<String> with android.R.layout.simple_list_item_1 on adapter creation
 * Custom-made for AppDataCollect's Farmer data list
 * credits @http://www.pcsalt.com/android/listview-using-baseadapter-android/
 * Used on tree list
 * From TreeCode, modified
 */

public class ListOverviewAdapter extends ArrayAdapter{
    // list of contained headers data
    HashMap<Integer, HashMap<String, String>> mData = new HashMap<>();

    // list of field names
    ArrayList<String> mFieldNames = new ArrayList<>();

    LayoutInflater inflater;
    private String TAG = "--list";


    /**
     * View holder class for visual UI management
     */
    public static class ViewHolder{
        TextView txtEventName, txtEventDate, txtUser;

        // Icon to indicate if data has been saved to firebase
        ImageView icon_locked;


        public ViewHolder(View view){
            txtEventName = view.findViewById(R.id.title_header);
            txtUser = view.findViewById(R.id.label_content_01);
            txtEventDate = view.findViewById(R.id.label_content_02);
        }

        public void setTextValues(String eventName, String userName, String eventDate){
            txtEventName.setText(eventName);
            txtUser.setText(userName);
            txtEventDate.setText(eventDate);

            // Set locked label icon based on label_content_01
            icon_locked.setImageResource(R.drawable.ic_check_black_24dp);
        }
    }


    /**
     * Initialize the adapter with content data and other needed fields
     * @param context           Context from the calling Fragment or Activity
     * @param layoutResourceId  Layout resource ID (xml view layout) to be used for each adapter item
     * @param data              Data to be displayed in the ViewHolder's (3) TextViews
     * @param fieldNames        field names that correspond to each of the items in @param data
     */
    public ListOverviewAdapter(Context context, int layoutResourceId, HashMap<Integer, HashMap<String, String>> data, ArrayList<String> fieldNames){
        super(context, layoutResourceId, fieldNames);
        inflater = LayoutInflater.from(context);
        mData = data;
        mFieldNames = fieldNames;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;

        if(convertView == null){
            // create a ViewHolder object and save it in a tag of convertView
            convertView = inflater.inflate(R.layout.content_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            // retrieve the instance of ViewHolder from the convertView tag
            holder = (ViewHolder) convertView.getTag();
        }

        // insert data to views
        if(mData.size() > 0){
            Log.d(TAG, "mData size HAS VALUE! position = " + AppUtilities.INSTANCE.numberToString(position));
            holder.setTextValues(
                    mData.get(position).get(CommonFlags.INSTANCE.LIST_HEADER),
                    mData.get(position).get(CommonFlags.INSTANCE.LIST_SUBTOPIC_01),
                    mData.get(position).get(CommonFlags.INSTANCE.LIST_SUBTOPIC_02));
        }
        else{
            Log.d(TAG, "MData size is 0");
        }

        return convertView;
    }
}
