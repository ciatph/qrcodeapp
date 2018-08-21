package com.leo.qrcodeapp.utils.listhandler;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import java.lang.Object;
import com.leo.qrcodeapp.R;
import com.leo.qrcodeapp.utils.AppUtilities;

/**
 * An external accessor used for accessing and traversing internal InputRecyclerAdapter adapters from RecyclerListManagers.
 * Used when a RecyclerView is loaded from an external reference outside RecyclerListManager.
 * Used when there are simultaneous RecyclerListManagers instances, i.e. in ViewPagers and FragmentStatePagerAdapters.
 * Similar to RecyclerListManager's fields and value accessors
 * Created by mbarua on 11/8/2017.
 */
// TODO: 11/8/2017 add this method in a common class handler
public enum AdapterHandler {
    INSTANCE;

    private String TAG = "--AdapterHandler::";

    /** Returnn a default value for blank or null items */
    private boolean returnDefaultValue = true;

    /** Default character or String that is returned for empty items */
    private String defaultValue = "";


    /**
     * Get an instance of the RecyclerView's InputRecyclerAdapter from a given page no. inside a ViewPager
     * @param viewPager ViewPager class that holds several fragments (pages) each with an InputRecyclerAdapters/RecyclerListManager member
     * @param pageNo    Page no. from the viewPager
     * @return  Snapshot of InputRecyclerAdapter from a ViewPager's layout
     */
    public com.leo.qrcodeapp.utils.listhandler.InputRecyclerAdapter getCurrentAdapterState(ViewPager viewPager, int pageNo){
        return ((com.leo.qrcodeapp.utils.listhandler.InputRecyclerAdapter)
                    ((RecyclerView)(viewPager.getChildAt(pageNo).findViewById(R.id.recyler_list))).getAdapter());
    }


    /**
     * Get all column field names from the adapter
     * @param mAdapter  Snapshot of InputRecyclerAdapter from the current or parent layout
     * @return  String[] array of field names
     */
    public String[] getColumnFields(InputRecyclerAdapter mAdapter){
        ArrayList<String> columns = new ArrayList<>();
        for(int i=0; i<mAdapter.getItemCount(); i++){
            columns.add(mAdapter.getObjectAt(i).getColumnField());
            Log.d(TAG, "typeof " + mAdapter.getObjectAt(i).getColumnField() + " == " +
                    AppUtilities.INSTANCE.numberToString(mAdapter.getObjectAt(i).getParamType()));
        }

        String[] cols = columns.toArray(new String[0]);
        return cols;
    }


    /**
     * Get all column field names from the adapter
     * @param viewPager     ViewPager class
     * @param pageNo        Page no. from the viewPager
     * @return  String[] array of field values
     */
    public String[] getColumnFields(ViewPager viewPager, int pageNo){
        return getColumnFields(getCurrentAdapterState(viewPager, pageNo));
    }


    /**
     * Get all user-input field values from adapter
     * @param mAdapter  Snapshot of InputRecyclerAdapter from the current or parent layout
     * @return  String array of field values that correspond with field names
     */
    public String[] getFieldValues(InputRecyclerAdapter mAdapter){
        Log.d(TAG, "adapter item count: " + mAdapter.getItemCount());
        ArrayList<String> columns = new ArrayList<>();
        for(int i=0; i<mAdapter.getItemCount(); i++){
            if(returnDefaultValue){
                if (mAdapter.getObjectAt(i).getValue() == null) {
                    columns.add(defaultValue);
                } else {
                    columns.add((mAdapter.getObjectAt(i).getValue().trim().equals("") || mAdapter.getObjectAt(i).getValue().equals(null)) ?
                            defaultValue : mAdapter.getObjectAt(i).getValue().trim());
                }
            }
            else{
                columns.add(mAdapter.getObjectAt(i).getValue());
            }
        }

        String[] cols = columns.toArray(new String[0]);
        return cols;
    }


    /**
     * Get all user-input field values from adapter
     * @param viewPager     ViewPager class
     * @param pageNo        Page no. from the viewPager
     * @return  String array of field values that correspond with field names
     */
    public String[] getFieldValues(ViewPager viewPager, int pageNo){
        return getFieldValues(getCurrentAdapterState(viewPager, pageNo));
    }


    /**
     * Get the the class's N-numbered sub-items of all main node (field name)
     * labeled as grouped (KeyValuePair.EDIT_TYPE_INPUT_GROUP)
     */
    public HashMap<String, Object> getFieldValuesSubItems(ViewPager viewPager, int pageNo){
        InputRecyclerAdapter mAdapter = getCurrentAdapterState(viewPager, pageNo);

        ArrayList<String> columns = new ArrayList<>();
        HashMap<String, Object> subitems = new HashMap<>();

        for(int i=0; i<mAdapter.getItemCount(); i++){
            columns.add(mAdapter.getObjectAt(i).getColumnField());

            if(mAdapter.getObjectAt(i).getEditTextType() == KeyValuePair.EDIT_TYPE_INPUT_GROUP){
                Log.d(TAG, "typeof!! " + mAdapter.getObjectAt(i).getColumnField() + " == " +
                        AppUtilities.INSTANCE.numberToString(mAdapter.getObjectAt(i).getParamType()) + " :: value = " + mAdapter.getContent(i));

                subitems = mAdapter.getObjectAt(i).getGroupDataMap();
                for(String key : subitems.keySet()){
                    Log.d(TAG, "key " + key + " == " + subitems.get(key));
                }
            }
        }

        return subitems;
    }


    /**
     * Get the class's N-numbered sub-items of a particularly-specified main node (fieldName) from a direct ViewPager instance
     * The field names are string-equivalents of the original node names
     * labeled as grouped (KeyValuePair.EDIT_TYPE_INPUT_GROUP)
     * @param viewPager     ViewPager object that holds all sub pages
     * @param pageNo        Page number to view in a viewpager
     * @param parentFieldName     Common field name of all similar-named ItemEditGroupViewHolder
     */
    public HashMap<String, Object> getFieldValuesSubItems(ViewPager viewPager, int pageNo, String parentFieldName){
        // get the InputRecyclerAdapter reference
        InputRecyclerAdapter mAdapter = getCurrentAdapterState(viewPager, pageNo);
        HashMap<String, Object> subitems = new HashMap<>();

        for(int i=0; i<mAdapter.getItemCount(); i++){
            // get the matching fieldName
            if(mAdapter.getObjectAt(i).getColumnField().equals(parentFieldName) && mAdapter.getObjectAt(i).getEditTextType() == KeyValuePair.EDIT_TYPE_INPUT_GROUP) {
                Log.d(TAG, "typeof!! " + mAdapter.getObjectAt(i).getColumnField() + " == " +
                        AppUtilities.INSTANCE.numberToString(mAdapter.getObjectAt(i).getEditTextType()));

                subitems = mAdapter.getObjectAt(i).getGroupDataMap();

                if(returnDefaultValue) {
                    for (String key : subitems.keySet()) {
                        if (subitems.get(key) == null) {
                            subitems.put(key, defaultValue);
                        } else {
                            subitems.put(key,
                                    (subitems.get(key).toString().trim().equals("") || subitems.get(key).equals(null)) ?
                                            defaultValue : subitems.get(key).toString().trim());
                        }
                        Log.d(TAG, "key " + key + " == " + subitems.get(key));
                    }
                }
            }
            else if(mAdapter.getObjectAt(i).getColumnField().equals(parentFieldName) && mAdapter.getObjectAt(i).getEditTextType() == KeyValuePair.EDIT_TYPE_CHECKBOX) {
                Log.d(TAG, "cbox typeof!! " + mAdapter.getObjectAt(i).getColumnField() + " == " +
                        AppUtilities.INSTANCE.numberToString(mAdapter.getObjectAt(i).getEditTextType()));

                subitems = mAdapter.getObjectAt(i).getGroupDataMap();

                Log.d(TAG, "-cboxItems " + subitems.size() + ", cboxValues " /*+ checkboxValues.length + ""*/);
                for(String key : subitems.keySet()){
                    Log.d(TAG, "--cbox reading... " + key);
                }
            }
        }

        return subitems;
    }


    /**
     * Get the class's N-numbered sub-items of a specified SIMILAR main node (group fieldName)
     * The field names are string-equivalents of the original node names
     * labeled as grouped (KeyValuePair.EDIT_TYPE_INPUT_GROUP)
     * @param viewPager     ViewPager object that holds all sub pages
     * @param pageNo        Page number to view in a viewpager
     * @param fieldName     Common field name of all similar-named ItemEditGroupViewHolder
     */
    public ArrayList<HashMap<String, Object>> getFieldValuesSubItemsDynamic(ViewPager viewPager, int pageNo, String fieldName){
        // get the InputRecyclerAdapter reference
        InputRecyclerAdapter mAdapter = getCurrentAdapterState(viewPager, pageNo);

        ArrayList<HashMap<String, Object>> items = new ArrayList<>();
        HashMap<String, Object> subitems = new HashMap<>();

        for(int i=0; i<mAdapter.getItemCount(); i++){
            // get all items with matching fieldName
            if(mAdapter.getObjectAt(i).getColumnField().contains(fieldName) && mAdapter.getObjectAt(i).getEditTextType() == KeyValuePair.EDIT_TYPE_INPUT_GROUP) {
                Log.d(TAG, "typeof!! " + mAdapter.getObjectAt(i).getColumnField() + " == " +
                        AppUtilities.INSTANCE.numberToString(mAdapter.getObjectAt(i).getEditTextType()));

                //subitems = mAdapter.getObjectAt(i).getGroupDataMap();
                items.add(mAdapter.getObjectAt(i).getGroupDataMap());
                for (String key : subitems.keySet()) {
                    Log.d(TAG, "key " + key + " == " + subitems.get(key));
                }
            }
        }

        Log.d(TAG, "found " + AppUtilities.INSTANCE.numberToString(items.size()) + " of NEW_GROUP");
        return items;
    }
}
