package com.leo.qrcodeapp.utils.listhandler;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;

import com.leo.qrcodeapp.R;


/**
 * Created by mbarua on 11/10/2017.
 */

public class CheckBoxAdapter extends ArrayAdapter {
    ArrayList<String> mDataArray = new ArrayList<>();
    HashMap<String, Boolean> hashMap = new HashMap<>();
    CheckBoxAdapterListener cnCallback;
    int itemHeight = 0;
    String TAG = "--CheckBoxAdapter::";
    private boolean enabled = true;

    public interface CheckBoxAdapterListener{
        public void onCheckBoxItemClicked(String id);
    }


    /**
     * ViewHolder UI layout. Enables click action on checkbox items
     */
    public class ViewHolder {
        CheckBox checkBox;
        EditText deleteMe;
        AlertDialog dialog;
        int position;

        public ViewHolder(View view, final int position){
            checkBox = view.findViewById(R.id.item_checkbox);
            itemHeight = checkBox.getMeasuredHeight();

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cnCallback.onCheckBoxItemClicked(mDataArray.get(position));
                    selectItem(position);
                }
            });

            this.position = position;
        }
        // hashMap.put(mDataArray.get(position), checkBox.isChecked());
    }


    /**
     * Initialization with context fom calling source.
     * @param context           Context fom calling Activity/Fragment source
     * @param layoutResourceId  Resource id of checkbox item from the current layout
     * @param data              String[] array list of data for each checkbox
     */
    public CheckBoxAdapter(Context context, int layoutResourceId, String[] data){
        super(context, layoutResourceId, data);

        for(int i=0; i<data.length; i++){
            mDataArray.add(data[i]);
            hashMap.put(data[i], false);
        }
    }


    /**
     * Initialization with context fom calling source.
     * @param context           Context fom calling Activity/Fragment source
     * @param layoutResourceId  Resource id of checkbox item from the current layout
     * @param data              String[] array list of data for each checkbox
     * @param listener          "this" reference from implementing classes for input callback listeners
     * @param enabled           Indicates if checkbox items for this group are enabled or disabled
     */
    public CheckBoxAdapter(Context context, int layoutResourceId, String[] data, CheckBoxAdapterListener listener, Boolean enabled){
        super(context, layoutResourceId, data);
        cnCallback = listener;

        for(int i=0; i<data.length; i++){
            mDataArray.add(data[i]);
            hashMap.put(data[i], false);
        }

        // Set checkbox items as enabled or disabled
        this.enabled = (enabled != null) ? enabled : true;
    }


    /**
     * Initialization with context fom calling source.
     * @param context           Context fom calling Activity/Fragment source
     * @param layoutResourceId  Resource id of checkbox item from the current layout
     * @param data              String[] array list of data for each checkbox
     * @param listener          "this" reference from implementing classes for input callback listeners
     * @param enabled           Indicates if checkbox items for this group are enabled or disabled
     */
    public CheckBoxAdapter(Context context, int layoutResourceId, String[] data, String[] dataValues, CheckBoxAdapterListener listener, Boolean enabled){
        super(context, layoutResourceId, data);
        cnCallback = listener;

        for(int i=0; i<data.length; i++){
            mDataArray.add(data[i]);
            hashMap.put(data[i], Boolean.parseBoolean(dataValues[i]));
        }

        // Set checkbox items as enabled or disabled
        this.enabled = (enabled != null) ? enabled : true;
    }


    /**
     * Check or remove the check of an item in the checkbox group
     * @param label     Checkbox item key
     * @param check     Flag to check or uncheck an item
     */
    public void checkItem(String label, boolean check){
        hashMap.put(label, check);
    }


    /**
     * Inflate the single CheckBox xml layout
     * @param position      Position of the checkbox item from the adapter's list
     * @param convertView   Instance of ViewHolder
     * @param parent        Parent layout of the checkbox layout items
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if(convertView == null){
            convertView = (LayoutInflater.from(getContext())).inflate(
                    R.layout.list_checkbox, parent, false);
            holder = new ViewHolder(convertView, position);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.checkBox.setText(mDataArray.get(position));
        boolean check = (hashMap.get(mDataArray.get(position)));
        holder.checkBox.setChecked(check);

        if(!this.enabled)
            holder.checkBox.setEnabled(false);
        return convertView;
    }


    /**
     * Get a list of all selected (checked) items
     * @return  String[] array of all selected items
     */
    public String[] getSelectedItems(){
        // get a String[] of selected items
        ArrayList<String> selected = new ArrayList<>();
        for(int i=0; i<mDataArray.size(); i++){
            if(hashMap.get(mDataArray.get(i))){
                selected.add(mDataArray.get(i));
            }
        }

        String[] array = selected.toArray(new String[0]);
        return array;
    }


    /**
     * Toggle a checkbox item's selected flag
     * @param key
     * @param selected
     */
    public void setSelectedItems(String key, boolean selected){
        // toggle selected keys as true|false
        hashMap.put(key, selected);
    }


    /**
     * Remove check/selected flag from all items in the group
     */
    public void resetSelectedItems(){
        // de-select all items
        for(String key : hashMap.keySet()){
            hashMap.put(key, false);
        }
    }


    /**
     * Reset, erase item data value container
     */
    public void flush(){
        // resets the true|false values in hashMap
        for(int i=0; i<mDataArray.size(); i++){
            hashMap.put(mDataArray.get(i), false);
        }
    }


    /**
     * Select/check an item from the checkbox item list
     * @param position
     */
    public void selectItem(int position){
        hashMap.put(mDataArray.get(position), !itemChecked(position));
    }


    /**
     * Check if a checkbox item indexed at position is checked/unchecked
     * @param position  Index of item to check in the adapter list
     * @return
     */
    public boolean itemChecked(int position){
        Log.d(TAG, "checked " + hashMap.get(mDataArray.get(position)));
        return hashMap.get(mDataArray.get(position));
    }

    public int getViewHeight(){ return itemHeight; }
}

