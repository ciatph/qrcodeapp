package com.leo.qrcodeapp.utils.listhandler;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;

import com.leo.qrcodeapp.R;
import com.leo.qrcodeapp.events.EventStatus;
import com.leo.qrcodeapp.utils.AppUtilities;
import com.leo.qrcodeapp.utils.ApplicationContextProvider;
import com.leo.qrcodeapp.utils.CommonFlags;

/**
 * Created by mbarua on 11/13/2017.
 */

public class InputViewAdapter extends ArrayAdapter {
    /** A list of field/node/column names */
    ArrayList<String> mDataArray = new ArrayList<>();

    /** A list of key-value pair where the keys are also specified in mDataArray */
    HashMap<String, String> hashMap = new HashMap<>();

    InputViewAdapter.InputViewAdapterListener cnCallback;
    String TAG = "--InputViewAdapter::";
    int PROCESS_MODE = EventStatus.INSTANCE.ACTION_ADD;
    InputMethodManager imm;
    private Boolean enabled = true;

    // text color of disabled items
    ColorStateList textColorDisabled;
    Drawable textBackgroundDisabled;


    /**
     * Interface to deliver input data to implementing classes.
     */
    public interface InputViewAdapterListener{
        public void onViewItemTextChanged(String key, String value);
        public void onViewItemClicked(String key, String value);
    }


    /**
     * ViewHolder UI layout. Enables click action on checkbox items
     */
    public class ViewHolder /*implements View.OnClickListener*/{
        final EditText viewUi;
        int position;

        public ViewHolder(View view, final int position){
            viewUi = view.findViewById(R.id.view_edit_hint_content);
            viewUi.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    hashMap.put(mDataArray.get(position), s.toString());
                    cnCallback.onViewItemTextChanged(mDataArray.get(position), s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            this.position = position;
        }
    }


    /**
     * Initialization with context fom calling source.
     * @param context           Context fom calling Activity/Fragment source
     * @param layoutResourceId  Resource id of checkbox item from the current layout
     * @param data              String[] array list of data for each checkbox
     */
    public InputViewAdapter(Context context, int layoutResourceId, String[] data, String[] value, int processMode, Boolean enabled){
        super(context, layoutResourceId, data);
        PROCESS_MODE = processMode;

        for(int i=0; i<data.length; i++){
            mDataArray.add(data[i]);
            hashMap.put(data[i], value[i]);
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
     */
    public InputViewAdapter(Context context, int layoutResourceId, String[] data, String[] value, int processMode, InputViewAdapter.InputViewAdapterListener listener, Boolean enabled){
        super(context, layoutResourceId, data);

        // clear fields
        mDataArray.clear();
        hashMap.clear();

        cnCallback = listener;
        PROCESS_MODE = processMode;

        for(int i=0; i<data.length; i++){
            mDataArray.add(data[i]);
            hashMap.put(data[i], value[i]);
        }

        // Set checkbox items as enabled or disabled
        this.enabled = (enabled != null) ? enabled : true;
    }


    /**
     * Inflate the single CheckBox xml layout
     * @param position      Position of the checkbox item from the adapter's list
     * @param convertView   Instance of ViewHolder
     * @param parent        Parent layout of the checkbox layout items
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        Log.d(TAG, "getting view text = " + mDataArray.get(position));
        final ViewHolder holder;

        if(convertView == null){
            int layout = (this.enabled) ? R.layout.content_hint_edit : R.layout.content_hint_edit_disabled;
             convertView = (LayoutInflater.from(getContext())).inflate(layout, parent, false);
            holder = new InputViewAdapter.ViewHolder(convertView, position);
            convertView.setTag(holder);
        }
        else{
            holder = (InputViewAdapter.ViewHolder) convertView.getTag();
        }

        if(PROCESS_MODE == EventStatus.INSTANCE.ACTION_ADD){
            holder.viewUi.setHint("Enter " + mDataArray.get(position));
            holder.viewUi.setText(hashMap.get(mDataArray.get(position)));
        }
        else if(PROCESS_MODE == EventStatus.INSTANCE.ACTION_VIEW || PROCESS_MODE ==  EventStatus.INSTANCE.ACTION_EDIT){
            //holder.viewUi.setText(mDataArray.get(position));
            holder.viewUi.setHint("Enter " + mDataArray.get(position));
            holder.viewUi.setText(hashMap.get(mDataArray.get(position)));

            // make the edittext enabled or disabled
            if(!this.enabled) {
                holder.viewUi.setEnabled(false);
                // // TODO: 1/22/2018 set text color to dark green
                //((EditText) holder.viewUi.findViewById(R.id.view_edit_hint_content)).setTextColor(textColorDisabled);
                //((EditText) holder.viewUi).setTextColor(textColorDisabled);
                // (holder.viewUi).setTextColor(textColorDisabled);
            }
        }

        // force to display the softkeyboard on user click
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "was clicked convertView!! ---------------");
                imm = (InputMethodManager) ApplicationContextProvider.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                holder.viewUi.setText("");
                cnCallback.onViewItemClicked(AppUtilities.INSTANCE.numberToString(position),
                        hashMap.get(mDataArray.get(position)));
            }
        });

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
            //if(hashMap.get(mDataArray.get(i))){
            selected.add(mDataArray.get(i));
            //}
        }

        String[] array = selected.toArray(new String[0]);
        return array;
    }


    /**
     * Toggle a checkbox item's selected flag
     * @param key
     * @param selected
     */
    public void setSelectedItems(String key, String selected){
        // toggle selected keys as true|false
        hashMap.put(key, selected);
    }


    /**
     * Remove check/selected flag from all items in the group
     */
    public void resetSelectedItems(){
        // de-select all items
        for(String key : hashMap.keySet()){
            hashMap.put(key, "");
        }
    }


    /**
     * Initialize the input text group's disabled item colors
     * @param colorResourceId   Color resource ID for the text font
     * @param backgroundResId   Color resource ID for the edit text background
     */
    public void initializeDisabledColors(Integer colorResourceId, Integer backgroundResId){
        int colorId = (colorResourceId != null) ? (int) colorResourceId : R.color.colorPrimaryDark;
        int backgroundId = (backgroundResId != null) ? (int) backgroundResId : R.drawable.edittext_bg_fine_disabled;

        textColorDisabled = ApplicationContextProvider.getContext().getResources().getColorStateList(colorId);
        textBackgroundDisabled = ApplicationContextProvider.getContext().getResources().getDrawable(backgroundId);
    }


    /**
     * Update the adapter contents with ordered key-value paired data
     * @param keys      field-names or keys that match with item values[]
     * @param values    values to be associated with keys[]
     */
    public void updateData(String[] keys, String[] values){
        // clear fields
        mDataArray.clear();
        hashMap.clear();

        for(int i=0; i<keys.length; i++){
            mDataArray.add(keys[i]);
            hashMap.put(keys[i], values[i]);
        }

        notifyDataSetChanged();
    }


    /**
     * Reset, erase item data value container
     */
    public void flush(){
        // resets the true|false values in hashMap
        hashMap.clear();
        mDataArray.clear();
    }
}

