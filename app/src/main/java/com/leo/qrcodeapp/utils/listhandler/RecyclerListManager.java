package com.leo.qrcodeapp.utils.listhandler;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import com.leo.qrcodeapp.R;
import com.leo.qrcodeapp.utils.AppUtilities;


/**
 * Created by mbarua on 11/2/2017.
 * A generic class for managing RecyclerView lists' simple and complex data UI display and user input.
 * Maps real time user input to corresponding field name
 * Manages the InputRecyclerAdapter|KeyValuePair.
 */

public class RecyclerListManager {
    //INSTANCE;

    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter = null;
    public RecyclerView.LayoutManager mLayoutManager;
    private String TAG = "--RecyclerListMngr";

    /**
     * Common interface for organizing and setting RecyclerListManager's
     * resources and display data
     */
    public interface setDataListener{
        /**
         * Load UI, get the set of xml string labels, column field names, data values and settings
         * for RecyclerListManager's adapter
         * @param processMode   Flag if UI display is in View or Edit mode
         * @return
         */
        public HashMap<Integer, KeyValuePair> getViewData(int processMode);
    }


    public RecyclerListManager(){}


    /**
     * Initialize the RecyclerView list holder
     * @param view      Inflated View
     * @param activity  Calling Activity
     */
    public RecyclerListManager(View view, Activity activity){
        initRecyclerListManager(view, activity);
    }


    /**
     * Initialize the RecyclerView list holder, with specified resource ID for the RecyclerView
     * @param view      Inflated View
     * @param activity  Calling Activity
     * @param recyclerResId Resource ID for a specific RecyclerView target
     */
    public RecyclerListManager(View view, Activity activity, int recyclerResId){
        initRecyclerListManager(view, activity, recyclerResId);
    }


    /**
     * Initialize RecyclerListManager's xml layout using only inflated view and Activity
     * @param view      Inflated View
     * @param activity  Calling Activity
     */
    public void initRecyclerListManager(View view, Activity activity){
        //RecyclerListManager.INSTANCE.flush();

        // RecyclerView reference for the list UI
        mRecyclerView = view.findViewById(R.id.recyler_list);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // new!
        mRecyclerView.setLayoutFrozen(true);
    }


    /**
     * Load UI, get the set of xml string labels, column field names, data values and settings
     * for RecyclerListManager's adapter. Added RecyclerList's integer resource ID
     * @param view          View with the RecyclerList for listing
     * @param activity      Calling activitie; context
     */
    public void initRecyclerListManager(View view, Activity activity, int recyclerResId){
        //RecyclerListManager.INSTANCE.flush();

        // RecyclerView reference for the list UI
        mRecyclerView = view.findViewById(recyclerResId);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // new!
        mRecyclerView.setLayoutFrozen(true);
    }


    /**
     * Attaches a callback listener to InputRecyclerAdapter items in the HashMap
     * @param data      a HashMap<Integer, KeyValuePair> of data to be displayed in UI
     * @param batchViewType     flag used for batch rendering UI elements either for viewing or editing:
     * @param callback          a context from the calling Activity/Fragment to attach for clickable items in the data
     */
    public void setAdapter(HashMap<Integer, KeyValuePair> data, int batchViewType, InputRecyclerAdapter.InputRecyclerListener callback){
        // data = label, value
        mAdapter = new InputRecyclerAdapter(data, batchViewType, callback);
        mRecyclerView.setAdapter(mAdapter);
    }


    public void setAdapter(HashMap<Integer, KeyValuePair> data, int batchViewType,
                           InputRecyclerAdapter.InputRecyclerListener callback, CheckBoxAdapter.CheckBoxAdapterListener cbCallback){
        // data = label, value
        mAdapter = new InputRecyclerAdapter(data, batchViewType, callback, cbCallback);
        mRecyclerView.setAdapter(mAdapter);
    }


    /**
     * Set the Recycler adapter's data for a view operation on data: view or edit
     * @param data      a HashMap<Integer, KeyValuePair> of data to be displayed in UI
     * @param batchViewType flag used for batch rendering UI elements either for viewing or editing:
     *                      InputRecyclerAdapter.TYPE_LABEL, TYPE_EDIT, TYPE_LIST
     */
    public void setAdapter(HashMap<Integer, KeyValuePair> data, int batchViewType){
        // data = label, value
        Log.d(TAG, "BatchViewType " + AppUtilities.INSTANCE.numberToString(batchViewType) + "!");
        mAdapter = new InputRecyclerAdapter(data, batchViewType);
        mRecyclerView.setAdapter(mAdapter);
    }


    /**
     * Get the string input value of an adapter object at position
     * @param position  index of data to be retrieved from the adapter
     * @return
     */
    public String getValueAt(int position){
        return ((InputRecyclerAdapter) mAdapter).getContent(position);
    }


    /**
     * Get the string firebase/sqlite field nameof an adapter object at position
     * @param position  index of data's field name to be retrieved from the adapter
     * @return
     */
    public String getFieldNameAt(int position){
        return ((InputRecyclerAdapter) mAdapter).getObjectAt(position)
                .getColumnField();
    }


    /**
     * Get the KeyValuePair object of an adapter at position
     * @param position  index of data to be retrieved in the adapter
     * @return
     */
    public KeyValuePair getObjectAt(int position){
        return ((InputRecyclerAdapter) mAdapter).getObjectAt(position);
    }


    /**
     * Get all column field names from the adapter
     * @return  String[] array of field names
     */
    public String[] getColumnFields(){
        ArrayList<String> columns = new ArrayList<>();
        for(int i=0; i<mAdapter.getItemCount(); i++){
            columns.add(getObjectAt(i).getColumnField());
        }

        String[] cols = columns.toArray(new String[0]);
        return cols;
    }


    /**
     * Get all input field values from adapter
     * @return  String array of field values that correspond with field names
     */
    public String[] getFieldValues(){
        ArrayList<String> columns = new ArrayList<>();
        for(int i=0; i<mAdapter.getItemCount(); i++){
            if(getObjectAt(i).getEditTextType() == KeyValuePair.EDIT_TYPE_BUTTON_TEXT){
                Log.d(TAG, "--is EDIT_TYPE_BUTTON_TEXT from getFields::");
            }
            columns.add(getObjectAt(i).getValue());
        }

        String[] cols = columns.toArray(new String[0]);
        return cols;
    }


    /**
     * Set a (normal) item's data value from the adapter using its index
     * Used for items of type EDIT_TYPE_INPUT, EDIT_TYPE_TEXTAREA, EDIT_TYPE_BUTTON
     * @param position  Index of data to be retrieved in the adapter
     * @param value     New value for item
     */
    public void setDataAtPos(int position, String value){
        // update display custom text value on EDIT_TYPE_BUTTON_TEXT's TextView UI only
        if(mAdapter.getItemViewType(position) == KeyValuePair.EDIT_TYPE_BUTTON_TEXT){
            ((InputRecyclerAdapter.ItemButtonTextViewHolder)
                    mRecyclerView.findViewHolderForLayoutPosition(position)).textContent.setText(value);
        }
        else if(mAdapter.getItemViewType(position) == KeyValuePair.EDIT_TYPE_INPUT_GROUP){
            ((InputRecyclerAdapter.ItemEditGroupViewHolder)
                    mRecyclerView.findViewHolderForLayoutPosition(position)).inputViewAdapter.setSelectedItems("Kind of Fertilizer", "hello");
        }
        else if(mAdapter.getItemViewType(position) == KeyValuePair.EDIT_TYPE_INPUT_LOCKED){
            ((InputRecyclerAdapter.ItemEditLockedViewHolder)
                    mRecyclerView.findViewHolderForLayoutPosition(position)).textView.setText(value);
        }

        // normal TextView/EditText display values on the adapter
        ((InputRecyclerAdapter) mRecyclerView.getAdapter()).setData(position, value);
    }


    /**
     * Set all groupDataValue for a page, for all items of type EDIT_TYPE_INPUT_GROUP
     * @param position
     * @param newValues
     * @param update
     */
    public void setDataAtPos(int position, String[] newValues, boolean update){
        InputRecyclerAdapter adapter = ((InputRecyclerAdapter) mRecyclerView.getAdapter());
        adapter.getObjectAt(position).setGroupDataValue(newValues);

        if(update)
            adapter.notifyDataSetChanged();
    }


    /**
     * Set a (normal) item's data value from the adapter.
     * Used for items of type EDIT_TYPE_INPUT, EDIT_TYPE_TEXTAREA, EDIT_TYPE_BUTTON
     * @param key       firebase/sqlite field name of data to be replaced in the adapter
     * @param value     New value for item
     */
    public void setDataAtKey(String key, String value){
        // normal TextView/EditText display values on the adapter
        ((InputRecyclerAdapter) mRecyclerView.getAdapter()).setData(key, value);
    }


    /**
     * Update the adapter. Render all item content modifications to UI
     */
    public void updateAdapter(Boolean foreUpdate){
        InputRecyclerAdapter adapter = ((InputRecyclerAdapter) mRecyclerView.getAdapter());
        adapter.notifyDataSetChanged();

        if(foreUpdate != null && foreUpdate) {
            adapter.notifyItemRangeChanged(0, 3);
            mRecyclerView.invalidate();
        }
    }


    /**
     * Set the groupDataValue of a single EDIT_TYPE_INPUT_GROUP item in a page
     * @param position
     * @param key
     * @param value
     */
    public void setDataAtPos(int position, String key, String value){
        Log.d(TAG, "--updating sub-item " + position + ", key: " + key + " ==> " + value);
        InputRecyclerAdapter adapter = ((InputRecyclerAdapter) mRecyclerView.getAdapter());

        // get existing groupData values
        String[] values = adapter.getObjectAt(position).getGroupDataValue();

        if(values == null){
            values = AppUtilities.INSTANCE.stringArrayBlanks(adapter.getObjectAt(position).getGroupData().length, null);
        }

        values[position] = value;
        adapter.getObjectAt(position).setGroupDataValue(values);
        adapter.notifyDataSetChanged();
    }


    /**
     * Insert a new item of KeyValuePair.EDIT_TEXT_TYPE into this adapter's InputRecyclerAdapter.content
     * @param KEYVALUEPAIR_INPUT_TYPE   any type for KeyValuePair.EDIT_TEXT_TYPE
     * @param item      Pre-configured KeyValuePair item
     * @param columnFieldName   Common name identification for the newly-created item
     */
    public void insertData(int KEYVALUEPAIR_INPUT_TYPE, KeyValuePair item, String columnFieldName, String[] fields, String[] values) {
        Log.d(TAG, "item inserted");
        String ID = (columnFieldName != null) ? columnFieldName : "new_group";
        InputRecyclerAdapter adapter = ((InputRecyclerAdapter) mRecyclerView.getAdapter());

        switch(KEYVALUEPAIR_INPUT_TYPE){
            case KeyValuePair.EDIT_TYPE_INPUT_GROUP: {
                KeyValuePair groupedView = new KeyValuePair(
                        AppUtilities.INSTANCE.numberToString(adapter.countNewGrouped),   // label
                        ID,     // column field name
                        fields  // data (field-names)
                );

                if(values != null){
                    groupedView.setGroupDataValue(values);
                }

                Log.d(TAG, "added NEW FERTILIZER No. " + AppUtilities.INSTANCE.numberToString(adapter.countNewGrouped) +
                        " at LIST POSITION " + AppUtilities.INSTANCE.numberToString(adapter.content.size()));
                adapter.content.put(adapter.content.size(), groupedView);
                adapter.countNewGrouped++;
            } break;
            default: break;
        }

        // update existing items
        adapter.notifyDataSetChanged();
    }

    /**
     * Creates HashMap<> input for InputRecyclerAdapter.
     * Maps resource string-arrays for label texts with corresponding column names, with or without database values
     * String-array must be in the order they will display in UI; so are their matching column names
     * @param resourceLabels    String[] array of informative labels/headers to be displayed over text boxes
     * @param columnNames       Database name / identifier counterpart for each entry in resourceLabels[]
     * @param values            Values to be displayed on the UI for each columnNames[]
     * @param settings          Customizable HashMap<String, Integer[]> settings for certain columnName
     *                          See KeyValuePair's 3rd constructor for using custom .
     *                          String = column field name, Integer[] = KeyValuePair.EDIT_TYPE_TEXTAREA, CLICKABLE_FALSE|CLICKABLE_TRUE
     * @return                  HashMap<Integer, KeyvaluePair> of mapped-out resource labels, database names and default values
     */
    public HashMap<Integer, KeyValuePair> uiDataBuilder(String[] resourceLabels, String[] columnNames, String[] values, HashMap<String, Integer[]> settings){
        HashMap<Integer, KeyValuePair> map = new HashMap<>();

        for(int i=0; i<resourceLabels.length; i++){
            // use textarea, clickable settings for certain objects
            if(settings != null) {
                if (settings.containsKey(columnNames[i])) {
                    // initialize normal input type flag settings (textbox area, button, etc.)
                    // Log.d("--recyclermngr", columnNames[i]);
                    map.put(i, new KeyValuePair(resourceLabels[i], columnNames[i], values[i], settings.get(columnNames[i])));
                }
                else{
                    // initialize normal input types
                    map.put(i, new KeyValuePair(resourceLabels[i], columnNames[i], values[i]));
                }
            }
            else{
                // initialize normal input types
                map.put(i, new KeyValuePair(resourceLabels[i], columnNames[i], values[i]));
            }
        }

        return map;
    }


    /**
     * Creates HashMap<> input for InputRecyclerAdapter.
     * Maps resource string-arrays for label texts with corresponding column names, with or without database values
     * String-array must be in the order they will display in UI; so are their matching column names
     * @param resourceLabels    String[] array of informative labels/headers to be displayed over text boxes
     * @param columnNames       Database name / identifier counterpart for each entry in resourceLabels[]
     * @param values            Values to be displayed on the UI for each columnNames[]
     * @param settings          Customizable HashMap<String, Integer[]> settings for certain columnName
     *                          See KeyValuePair's 3rd constructor for using custom .
     *                          String = column field name, Integer[] = KeyValuePair.EDIT_TYPE_TEXTAREA, CLICKABLE_FALSE|CLICKABLE_TRUE
     * @param arrayData         HashMap<String, String[]> where key=column field name and String[] is an array of String values for the
     *                          key column field name. Usually used for grouped multiple choices for the key (i.e., ItemCheckboxGroupViewHolder)
     * @param arrayDataValues   HashMap<String, String[]> where key=column field name and String[] is an array of String values for the
     *                          key column field name in addarData. Usually used for grouped multiple choices item values/status for the key (i.e., ItemCheckboxGroupViewHolder)
     * @return                  HashMap<Integer, KeyvaluePair> of mapped-out resource labels, database names and default values
     */
    public HashMap<Integer, KeyValuePair> uiDataBuilder(String[] resourceLabels, String[] columnNames, String[] values,
                                                        HashMap<String, Integer[]> settings, HashMap<String, String[]> arrayData, HashMap<String, String[]> arrayDataValues){
        InputRecyclerAdapter adapter = ((InputRecyclerAdapter) mRecyclerView.getAdapter());

        HashMap<Integer, KeyValuePair> map = new HashMap<>();
        for(int i=0; i<resourceLabels.length; i++){
            // use textarea, clickable settings for certain objects
            if(settings != null) {
                if (settings.containsKey(columnNames[i])) {
                    //Log.d("--recyclermngr", columnNames[i]);

                    // initialize grouped checkbox data
                    if((settings.get(columnNames[i])[0] == KeyValuePair.EDIT_TYPE_CHECKBOX || settings.get(columnNames[i])[0] == KeyValuePair.EDIT_TYPE_INPUT_GROUP )&& arrayData != null){
                        Log.d(TAG, "--inserting multiple choice selections items " + columnNames[i]);

                        if(settings.get(columnNames[i])[0] == KeyValuePair.EDIT_TYPE_CHECKBOX){
                            KeyValuePair pair = new KeyValuePair(resourceLabels[i], columnNames[i], values[i], settings.get(columnNames[i]));
                            pair.setGroupData(arrayData.get(columnNames[i]));


                            // toggle check mark on item/s if EDIT_TYPE_CHECKBOX, EDIT_TYPE_INPUT_GROUP
                            if(arrayDataValues != null)
                                pair.setGroupDataValue(arrayDataValues.get(columnNames[i]));

                            map.put(i, pair);
                        }
                        else if(settings.get(columnNames[i])[0] == KeyValuePair.EDIT_TYPE_INPUT_GROUP){
                            KeyValuePair pair = new KeyValuePair(resourceLabels[i], columnNames[i], values[i], settings.get(columnNames[i]));
                            pair.setGroupData(arrayData.get(columnNames[i]));

                            // toggle check mark on item/s if EDIT_TYPE_CHECKBOX, EDIT_TYPE_INPUT_GROUP
                            if(arrayDataValues != null)
                                pair.setGroupDataValue(arrayDataValues.get(columnNames[i]));

                            map.put(i, pair);
                        }

                    }
                    else{
                        // initialize normal input type flag settings (textbox area, button, etc.)
                        map.put(i, new KeyValuePair(resourceLabels[i], columnNames[i], values[i], settings.get(columnNames[i])));
                    }
                }
                else{
                    // initialize normal input types
                    map.put(i, new KeyValuePair(resourceLabels[i], columnNames[i], values[i]));
                }
            }
            else{
                // initialize normal input types
                map.put(i, new KeyValuePair(resourceLabels[i], columnNames[i], values[i]));
            }
        }

        return map;
    }


    /**
     * Reset all internal members
     */
    public void flush(){
        mRecyclerView = null;
        mAdapter = null;
        mLayoutManager = null;
    }
}
