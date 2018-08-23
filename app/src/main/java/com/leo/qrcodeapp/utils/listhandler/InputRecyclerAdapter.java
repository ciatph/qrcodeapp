package com.leo.qrcodeapp.utils.listhandler;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

import com.leo.qrcodeapp.R;
import com.leo.qrcodeapp.events.EventStatus;
import com.leo.qrcodeapp.utils.AppUtilities;
import com.leo.qrcodeapp.utils.ApplicationContextProvider;
import com.leo.qrcodeapp.utils.CommonFlags;


/**
 * Created by mbarua on 11/2/2017.
 * Item adapter for RecyclerListManager. Contains definitions of valid UI item types.
 * Contains internal classes for constructing valid UI item types
 * Works with KeyValuePair for keeping track of internal data types
 * From BioTrack v.2.0.134, modified
 */

public class InputRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /** adapter item index-KeyValuePair. Contains a list of objects for UI display */
    public HashMap<Integer, KeyValuePair> content = new HashMap<>();
    /** Initerface for callbacks */
    private InputRecyclerListener mCallback;
    /** adapter item settings
     *  string[0] = viewType, string[1] = isClickable */
    private HashMap<String, Integer[]> settings = new HashMap<>();
    private HashMap<String, Integer[]> clickSettings = new HashMap<>();
    /** TextView-TextView item pair (only for viewing data) */
    public static final int TYPE_LABEL = 0;
    /** TextView-EditText item pair (for editing data) */
    public static final int TYPE_EDIT = 1;
    public static final int TYPE_OTHER = 2;
    /** TextView-TextView-TextView (for list overview) */
    public static final int TYPE_LIST = 3;
    /** The default UI view type */
    public static int VIEW_TYPE = TYPE_LABEL;

    // internal counter for new grouped data
    public int countNewGrouped = 0;

    // text color of disabled items
    ColorStateList textColorDisabled;
    Drawable textBackgroundDisabled;

    private String TAG = "--InputItemRecycler";


    /**
     * Interface for handling click events on some items for classes that implement this class
     */
    public interface InputRecyclerListener{
        public void onInputRecyclerItemClicked(String id, String subId);
    }


    /**
     * A layout class containing only a title header
     */
    public class ItemHeaderViewHolder extends RecyclerView.ViewHolder{
        public TextView label;

        public ItemHeaderViewHolder(View view){
            super(view);
            label = view.findViewById(R.id.label_header_title);
        }
    }

    /**
     * A layout class containing a title header and a text content, for viewing only
     */
    public class ItemLabelViewHolder extends RecyclerView.ViewHolder{
        public TextView label, _content;

        public ItemLabelViewHolder(View view){
            super(view);
            label = view.findViewById(R.id.view_item_label);
            _content = view.findViewById(R.id.view_item_content);
        }
    }


    public class ItemDividerHorizontal extends RecyclerView.ViewHolder{
        public ItemDividerHorizontal(View view){
            super(view);
        }
    }


    /**
     * A layout containing a title header and an editbox content, for editing
     * Attaches new text input listeners to an EditBox
     * Attaches single click listeners to an EditBox, if specified
     */
    public class ItemEditViewHolder extends RecyclerView.ViewHolder{
        public TextView label;
        public EditText editText;

        public ItemEditViewHolder(View view){
            super(view);
            label = view.findViewById(R.id.view_item_label);
            editText = view.findViewById(R.id.view_item_content);

            // bind click listener on editbox, if specified
            if (mCallback != null) {
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(content.get(getAdapterPosition()) != null) {
                            if (content.get(getAdapterPosition()).isClickable) {
                                //Log.d(TAG, "--IS clickable EditText! " + content.get(getAdapterPosition()).getColumnField());
                                //Log.d("--was pressed", "adapter=" + AppUtilities.INSTANCE.numberToString(getAdapterPosition()) + ", PARAM=" + AppUtilities.INSTANCE.numberToString(
                                //        content.get(getAdapterPosition()).getParamType()));
                                mCallback.onInputRecyclerItemClicked(AppUtilities.INSTANCE.numberToString(getAdapterPosition()), null);
                            }
                        }
                    }
                });
            }

            // bind text change listener on EditText
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(content.get(getAdapterPosition()) != null) {
                        content.get(getAdapterPosition()).setValue(s.toString());
                        //Log.d("--setting content", content.get(getAdapterPosition()).getValue() +
                        //        ", param " + AppUtilities.INSTANCE.numberToString(content.get(getAdapterPosition()).getColumnField()) +
                        //        ", adapterSize: " + AppUtilities.INSTANCE.numberToString(content.size()));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }


    /**
     * A layout containing a title header and a textView content, to simulate EditBox locked, un-editable status
     * Attaches new text input listeners to a TextView
     * Attaches single click listeners to an TextView, if specified
     */
    public class ItemEditLockedViewHolder extends RecyclerView.ViewHolder{
        public TextView label, textView;

        public ItemEditLockedViewHolder(View view){
            super(view);
            label = view.findViewById(R.id.view_item_label);
            textView = view.findViewById(R.id.view_item_content);

            if (mCallback != null) {
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(content.get(getAdapterPosition()) != null) {
                            if (content.get(getAdapterPosition()).isClickable) {
                                //Log.d("--was pressed", "adapter=" + AppUtilities.INSTANCE.numberToString(getAdapterPosition()) + ", PARAM=" + AppUtilities.INSTANCE.numberToString(
                                //        content.get(getAdapterPosition()).getParamType()));
                                mCallback.onInputRecyclerItemClicked(AppUtilities.INSTANCE.numberToString(getAdapterPosition()), null);
                            }
                        }
                    }
                });
            }
        }
    }


    /**
     * A layout class containing a title header and a text content, for viewing only
     * Has a click listener callback by default
     */
    public class ItemListViewHolder extends RecyclerView.ViewHolder {
        // a layout containing a title header and a text content, for viewing only
        // clickable by default, goes to view list entry page
        public TextView label, _content, footer;

        public ItemListViewHolder(View view){
            super(view);

            if (mCallback != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(content.get(getAdapterPosition()) != null) {
                            if (content.get(getAdapterPosition()).isClickable) {
                                // TODO: 11/8/2017 add mCallback and clickable checks here
                                //Log.d("--was pressed", "adapter=" + AppUtilities.INSTANCE.numberToString(getAdapterPosition()) + ", ID=" + content.get(getAdapterPosition()).getListId());
                                mCallback.onInputRecyclerItemClicked(content.get(getAdapterPosition()).getListId(), null);
                            }
                        }
                    }
                });
            }

            label = view.findViewById(R.id.view_item_label);
            _content = view.findViewById(R.id.view_item_content);
            footer = view.findViewById(R.id.view_item_footer);
        }
    }


    /**
     * Layout class for a single clickable Button
     */
    public class ItemButtonViewHolder extends RecyclerView.ViewHolder{
        public Button button;

        public ItemButtonViewHolder(View view){
            super(view);

            button = view.findViewById(R.id.content_button_input);

            if (mCallback != null) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(content.get(getAdapterPosition()) != null) {
                            if (content.get(getAdapterPosition()).isClickable) {
                                //Log.d("--button", "clicked");
                                mCallback.onInputRecyclerItemClicked(AppUtilities.INSTANCE.numberToString(getAdapterPosition()), null);
                            }
                        }
                    }
                });
            }
        }
    }


    /**
     * Layout class for a single clickable Button with a TextView for displaying logs and values.
     */
    public class ItemButtonTextViewHolder extends RecyclerView.ViewHolder{
        public Button button;
        public TextView textContent;
        public ItemButtonTextViewHolder(View view){
            super(view);

            button = view.findViewById(R.id.content_button_input);
            textContent = view.findViewById(R.id.view_item_content);

            // !IMPORTANT! Update textContent with custom value in
            // onInputRecyclerItemClicked's custom implementation outside of this class
            if(mCallback != null) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(content.get(getAdapterPosition()) != null) {
                            if (content.get(getAdapterPosition()).isClickable) {
                                //Log.d("--buttonText", "clicked");
                                // update View content from calling UI
                                mCallback.onInputRecyclerItemClicked(AppUtilities.INSTANCE.numberToString(getAdapterPosition()), null);

                                // TODO: 11/12/2017 test if value is saved from here
                                //content.get(getAdapterPosition()).setValue(textContent.getText().toString());
                            }
                        }
                    }
                });
            }
        }
    }


    /**
     * Layout class for a single clickable Button with an editable EditText for displaying logs and values.
     */
    public class ItemButtonEditViewHolder extends RecyclerView.ViewHolder{
        public Button button;
        public TextView textContent;
        public ItemButtonEditViewHolder(View view){
            super(view);

            button = view.findViewById(R.id.content_button_input);
            textContent = view.findViewById(R.id.view_item_content);

            // !IMPORTANT! Update textContent with custom value in
            // onInputRecyclerItemClicked's custom implementation outside of this class
            if(mCallback != null) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(content.get(getAdapterPosition()) != null) {
                            if (content.get(getAdapterPosition()).isClickable) {
                                //Log.d("--buttonText", "clicked");
                                // update View content from calling UI
                                mCallback.onInputRecyclerItemClicked(AppUtilities.INSTANCE.numberToString(getAdapterPosition()), null);

                                // TODO: 11/12/2017 test if value is saved from here
                                //content.get(getAdapterPosition()).setValue(textContent.getText().toString());
                            }
                        }
                    }
                });
            }

            // bind text change listener on EditText
            textContent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(content.get(getAdapterPosition()) != null) {
                        content.get(getAdapterPosition()).setValue(s.toString());
                        //Log.d("--setting content", content.get(getAdapterPosition()).getValue() +
                        //        ", param " + AppUtilities.INSTANCE.numberToString(content.get(getAdapterPosition()).getColumnField()) +
                        //        ", adapterSize: " + AppUtilities.INSTANCE.numberToString(content.size()));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }


    /**
     * Layout class for displaying a group of checkbox items
     */
    public class ItemCheckboxGroupViewHolder extends RecyclerView.ViewHolder implements CheckBoxAdapter.CheckBoxAdapterListener{
        CheckBoxAdapter checkBoxAdapter;
        public TextView label;
        public ListView listView;


        /**
         * Implement CheckBoxAdapter's listener for clicked items
         * @param value
         */
        @Override
        public void onCheckBoxItemClicked(String value){
            Log.d(TAG, "i was CLICKED!!! " + value);
            content.get(getAdapterPosition()).setValue(value);
        }

        public ItemCheckboxGroupViewHolder(View view){
            super(view);
            label = view.findViewById(R.id.view_item_label);
            listView = view.findViewById(R.id.list_checkbox_selection);
        }

        /**
         * Initialize the checkbox list adapter, checkbox list window size
         * depending on the number of data to display
         */
        public void displayGroupData(Boolean enabled){
            if(content.get(getAdapterPosition()).getGroupDataValue() == null){
                checkBoxAdapter = new CheckBoxAdapter(
                        ApplicationContextProvider.getContext(),
                        R.id.item_checkbox,
                        content.get(getAdapterPosition()).getGroupData(),
                        this, enabled);

            }
            else{
                checkBoxAdapter = new CheckBoxAdapter(
                        ApplicationContextProvider.getContext(),
                        R.id.item_checkbox,
                        content.get(getAdapterPosition()).getGroupData(),
                        content.get(getAdapterPosition()).getGroupDataValue(),
                        this, enabled);
            }

            // set width and height of checkbox window
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) listView.getLayoutParams();
            WindowManager wm = (WindowManager) ApplicationContextProvider.getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            lp.width = display.getWidth();

            final float scale = ApplicationContextProvider.getContext().getResources().getDisplayMetrics().scaledDensity;
            lp.height = AppUtilities.INSTANCE.getActionBarSize() * (content.get(getAdapterPosition()).getGroupData().length);

            // set checkbox list adapter, with modified window size
            listView.setAdapter(checkBoxAdapter);
        }
    }


    /**
     * Layout class for displaying a single checkboxes
     */
    public class ItemCheckboxViewHolder extends RecyclerView.ViewHolder{
        public CheckBox checkBox;

        public ItemCheckboxViewHolder(View view){
            super(view);
            //label = view.findViewById(R.id.view_item_label);
            checkBox = view.findViewById(R.id.item_checkbox);

            if (mCallback != null) {
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(content.get(getAdapterPosition()) != null) {
                            //Log.d("--checkBox", "clicked");
                            mCallback.onInputRecyclerItemClicked(AppUtilities.INSTANCE.numberToString(getAdapterPosition()), null);
                        }
                    }
                });
            }
        }
    }


    /**
     * Layout class for displaying and taking user-input from a group of (EditBox, TextView) fields
     */
    public class ItemEditGroupViewHolder extends RecyclerView.ViewHolder implements InputViewAdapter.InputViewAdapterListener{
        public InputViewAdapter inputViewAdapter;
        public TextView label;
        public ListView listView;


        /**
         * Implement InputViewAdapter's listener for text-changed on selected items
         * Store newly-written subitems data into mapGroupData
         * @param value
         */
        @Override
        public void onViewItemTextChanged(String key, String value){
            //Log.d(TAG, "i was CHANGED!!! " + key + " >> " + value);
            content.get(getAdapterPosition()).setGroupDataMap(key, value);
        }


        /**
         * Implement InputViewAdapter's listener for clicked items
         * @param value
         */
        public void onViewItemClicked(String position, String value){
            if(mCallback != null){
                mCallback.onInputRecyclerItemClicked(AppUtilities.INSTANCE.numberToString(getAdapterPosition()), position);
            }
        }


        public ItemEditGroupViewHolder(View view) {
            super(view);
            label = view.findViewById(R.id.view_item_label);
            listView = view.findViewById(R.id.list_editbox_selection);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Log.d(TAG, "--was clicked inside adapt at pos " + position + ", main pos " + getAdapterPosition() + "");
                }
            });
        }


        /**
         * Initialize the checkbox list adapter, checkbox list window size
         * depending on the number of data to display
         */
        public void displayGroupData(Boolean enabled){
            // InputViewAdapter only accepts ACTION_ADD_EVENT, ACTION_EDIT or ACTION_VIEW types
            // display a fresh group of blank data
            //Log.d(TAG, "getting KeyPair at adapt position " + getAdapterPosition() + "");
            if(content.get(getAdapterPosition()).getGroupDataValue() == null){
                //Log.d(TAG, "EDIT-GROUP is NULL");
                inputViewAdapter = new InputViewAdapter(
                        ApplicationContextProvider.getContext(),
                        R.id.view_edit_hint_content,
                        content.get(getAdapterPosition()).getGroupData(),
                        new String[content.get(getAdapterPosition()).getGroupData().length],
                        EventStatus.INSTANCE.getAction(), //VIEW_TYPE
                        this, enabled);
            }
            else{
                //Log.d(TAG, "EDIT-GROUP has VALUES: " + content.get(getAdapterPosition()).getGroupDataValue()[0]);
                /*
                inputViewAdapter = new InputViewAdapter(
                        ApplicationContextProvider.getContext(),
                        R.id.view_edit_hint_content,
                        content.get(getAdapterPosition()).getGroupData(),
                        content.get(getAdapterPosition()).getGroupDataValue(),
                        EventStatus.INSTANCE.getAction(), //VIEW_TYPE
                        this, enabled);
                        */
                if(inputViewAdapter == null){
                    inputViewAdapter = new InputViewAdapter(
                            ApplicationContextProvider.getContext(),
                            R.id.view_edit_hint_content,
                            content.get(getAdapterPosition()).getGroupData(),
                            content.get(getAdapterPosition()).getGroupDataValue(),
                            EventStatus.INSTANCE.getAction(), //VIEW_TYPE
                            this, enabled);
                    //Log.d(TAG, "--creating new inner adapter");
                }
                else{
                    //Log.d(TAG, "--updating adapter!");
                    inputViewAdapter.updateData(content.get(getAdapterPosition()).getGroupData(), content.get(getAdapterPosition()).getGroupDataValue());
                }
            }

            // set width and height of checkbox window
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) listView.getLayoutParams();
            WindowManager wm = (WindowManager) ApplicationContextProvider.getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            lp.width = display.getWidth();

            final float scale = ApplicationContextProvider.getContext().getResources().getDisplayMetrics().scaledDensity;
            float mTextSizeP = ApplicationContextProvider.getContext().getResources().getDimensionPixelSize(R.dimen.label_medium) / scale;

            //Log.d(TAG, "CHECKBOX TextSize " + AppUtilities.INSTANCE.numberToString(mTextSizeP) + ", height:: " + AppUtilities.INSTANCE.numberToString(lp.height));

            lp.height = AppUtilities.INSTANCE.getActionBarSize() * (content.get(getAdapterPosition()).getGroupData().length) +
                    ((content.get(getAdapterPosition()).getGroupData().length)  * Math.round(mTextSizeP));
            // set checkbox list adapter, with modified window size
            listView.setAdapter(inputViewAdapter);
            listView.setItemsCanFocus(true);
        }
    }



    /**
     * Constructor for displaying a list of simple label-value TextView pair
     * @param data          Consolidated HashMap<Integer, KeyValuePair> containing data and xml layout definitions
     * @param batchViewType Type of UI to load for data:
     *                      TYPE_LABEL, TYPE_EDIT, TYPE_OTHER, TYPE_LIST
     */
    public InputRecyclerAdapter(HashMap<Integer, KeyValuePair> data, int batchViewType){
        content = data;
        //VIEW_TYPE = batchViewType;
        VIEW_TYPE = (batchViewType == EventStatus.INSTANCE.ACTION_VIEW) ? TYPE_LABEL : batchViewType;
        initTextColorDisabled(null, null);

        // update countNewGrouped counter for EDIT_TYPE_INPUT_GROUP items in data
        for(Integer key : data.keySet()){
            if(data.get(key).getEditTextType() == KeyValuePair.EDIT_TYPE_INPUT_GROUP){
                countNewGrouped++;
            }
        }
    }


    /**
     * Constructor for displaying a list of simple label-value TextView pair.
     * Expects a context callback for processing UI click actions.
     * @param data          Consolidated HashMap<Integer, KeyValuePair> containing data and xml layout definitions
     * @param batchViewType Type of UI to load for data:
     *                      TYPE_LABEL, TYPE_EDIT, TYPE_OTHER, TYPE_LIST
     * @param callback      Context from calling Activity or Fragment.
     *                      Use 'this' or getContect(), see which works best.
     *                      Implement InputRecyclerListener for classes that will receive callback action
     */
    public InputRecyclerAdapter(HashMap<Integer, KeyValuePair> data, int batchViewType, InputRecyclerListener callback){
        content = data;
        VIEW_TYPE = batchViewType;
        mCallback = callback;
        initTextColorDisabled(null, null);
    }


    public InputRecyclerAdapter(HashMap<Integer, KeyValuePair> data, int batchViewType, InputRecyclerListener callback,
                                CheckBoxAdapter.CheckBoxAdapterListener cbCallback){
        content = data;
        VIEW_TYPE = batchViewType;
        mCallback = callback;
        //checkboxCallback = cbCallback;
        initTextColorDisabled(null, null);
    }


    /**
     * Constructor for displaying a list of simple label-value TextView pair.
     * Expects a context callback for processing UI click actions.
     * @param data          Consolidated HashMap<Integer, KeyValuePair> containing data and xml layout definitions
     * @param batchViewType Type of UI to load for data:
     *                      TYPE_LABEL, TYPE_EDIT, TYPE_OTHER, TYPE_LIST
     * @param callback      Context from calling Activity or Fragment.
     *                      Use 'this' or getContect(), see which works best.
     *                      Implement InputRecyclerListener for classes that will receive callback action
     * @param clickSettings flags for clickable-items in the adapter
     */
    public InputRecyclerAdapter(HashMap<Integer, KeyValuePair> data, int batchViewType, InputRecyclerListener callback, HashMap<String, Integer[]> clickSettings){
        content = data;
        VIEW_TYPE = batchViewType;
        mCallback = callback;
        this.clickSettings = clickSettings;
        initTextColorDisabled(null, null);
    }


    /**
     * Constructor for displaying a list of mixed label-value TYPE_LABEL / TYPE_EDIT pairs;
     *                      with settings = label-settings[0]=VIEW_TYPE or label-settings[0]=isClickable
     * @param data          Consolidated HashMap<Integer, KeyValuePair> containing data and xml layout definitions
     * @param settings      Display mixed TYPE_LABEL, TYPE_LABEL pairs; with settings = label-settings[0]=VIEW_TYPE
     */
    public InputRecyclerAdapter(HashMap<Integer, KeyValuePair> data, HashMap<String, Integer[]> settings){
        content = data;
        this.settings = settings;
        initTextColorDisabled(null, null);
    }


    /**
     * Set the text color of disabled, unfocusable items
     * @param colorResourceId   color layout resource ID
     */
    public void initTextColorDisabled(Integer colorResourceId, Integer backgroundResId){
        int colorId = (colorResourceId != null) ? (int) colorResourceId : R.color.colorPrimaryDark;
        int backgroundId = (backgroundResId != null) ? (int) backgroundResId : R.drawable.edittext_bg_fine_disabled;

        textColorDisabled = ApplicationContextProvider.getContext().getResources().getColorStateList(colorId);
        textBackgroundDisabled = ApplicationContextProvider.getContext().getResources().getDrawable(backgroundId);
    }


    /**
     * Get the
     * @return
     */
    public ColorStateList getTextColorDisabled(){
        return textColorDisabled;
    }


    /**
     * Get the drawable background for EditTextbox'sS
     * @return
     */
    public Drawable getTextBackgroundDisabled(){
        return textBackgroundDisabled;
    }


    /**
     * Initialize the xml layout and classes to be used for this list
     * @param parent    Parent container
     * @param viewType  Custom-assigned UI viewing type, derived from default @Override getItemViewType
     * @return          Returns an inflated, usable, and viewable xml layout with attached code logic
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // default xml layout: has header for label and text body for content
        int layout = R.layout.content_label_text;

        // determine the XML layout to use for UI, from KeyValuePair
        switch(VIEW_TYPE){
            // default xml layout
            case TYPE_LABEL: //layout = R.layout.content_label_text;
            {
                // Allow displaying of TextBox on EDIT_TYPE_INPUT_GROUP during ACTION_VIEW
                /*
                if(viewType == KeyValuePair.EDIT_TYPE_INPUT_GROUP)
                    layout = R.layout.content_edit_group;
                else
                    layout = R.layout.content_label_text;
                    */
                if(viewType == KeyValuePair.EDIT_TYPE_HEADER){
                    //Log.d(TAG, "---TITLE header set!");
                    layout = R.layout.content_label_header;
                }
                else if(viewType == KeyValuePair.EDIT_TYPE_DIVIDER_HORIZONTAL){
                    layout = R.layout.content_separator;
                }
                else if(viewType == KeyValuePair.EDIT_TYPE_BUTTON){
                    layout = R.layout.content_button;
                }
                else{
                    layout = R.layout.content_label_text;
                }
            }
                break;
            case TYPE_EDIT:{
                // large text area xml layout
                if(viewType == KeyValuePair.EDIT_TYPE_TEXTAREA)
                    layout = R.layout.content_label_edit_textarea;
                // large full-screen ttext area xml layout
                else if(viewType == KeyValuePair.EDIT_TYPE_TEXTAREA_FULL)
                    layout = R.layout.content_label_edit_textarea_full;
                // button layout
                else if(viewType == KeyValuePair.EDIT_TYPE_BUTTON)
                    layout = R.layout.content_button;
                // button with textview layout
                else if(viewType == KeyValuePair.EDIT_TYPE_BUTTON_TEXT)
                    layout = R.layout.content_button_text;
                    // button with textview layout
                else if(viewType == KeyValuePair.EDIT_TYPE_BUTTON_TEXT_EDIT)
                    layout = R.layout.content_button_text_edit;
                // check box group
                else if(viewType == KeyValuePair.EDIT_TYPE_CHECKBOX)
                    layout = R.layout.content_checkbox_group;
                // check box single
                else if(viewType == KeyValuePair.EDIT_TYPE_CHECKBOX_SINGLE)
                    layout = R.layout.list_checkbox;
                // group of editboxes
                else if(viewType == KeyValuePair.EDIT_TYPE_INPUT_GROUP)
                    layout = R.layout.content_edit_group;
                else if(viewType == KeyValuePair.EDIT_TYPE_INPUT_LOCKED)
                    layout = R.layout.content_label_edit_locked;
                else if(viewType == KeyValuePair.EDIT_TYPE_LABEL)
                    layout = R.layout.content_label_text;
                else
                    // normal sized one-liner editBox
                    layout = R.layout.content_label_edit;
                break;
            }
            case TYPE_LIST:
                // a container for a long list
                layout = R.layout.content_list_overview;
                break;
            default: break;
        }

        // initialize the xml layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);

        // attach code logic to the inflated xml layout
        if(VIEW_TYPE == TYPE_LABEL){
            // Allow displaying of TextBox on EDIT_TYPE_INPUT_GROUP during ACTION_VIEW
            /*
            if(viewType == KeyValuePair.EDIT_TYPE_INPUT_GROUP){
                ItemEditGroupViewHolder item = new ItemEditGroupViewHolder(view);
                return item;
            }
            else{
                ItemLabelViewHolder item = new ItemLabelViewHolder(view);
                // TODO: 5/10/2017 add button input here
                return item;
            }
            */
            if(viewType == KeyValuePair.EDIT_TYPE_HEADER){
                //Log.d(TAG, "---TITLE header set!");
                ItemHeaderViewHolder item = new ItemHeaderViewHolder(view);
                return item;
            }
            else if(viewType == KeyValuePair.EDIT_TYPE_DIVIDER_HORIZONTAL){
                //Log.d(TAG, "---TITLE HORIZONTAL set!");
                ItemDividerHorizontal item = new ItemDividerHorizontal(view);
                return item;
            }
            else if(viewType == KeyValuePair.EDIT_TYPE_BUTTON){
                //Log.d(TAG, "---BUTTON ELEMENT set!");
                ItemButtonViewHolder item = new ItemButtonViewHolder(view);
                return item;
            }
            else{
                //Log.d(TAG, "---TITLE KEY-VALUE set!");
                ItemLabelViewHolder item = new ItemLabelViewHolder(view);
                return item;
                // TODO: 5/10/2017 add button input here
            }
        }
        else if(VIEW_TYPE == TYPE_EDIT){
            if(viewType == KeyValuePair.EDIT_TYPE_BUTTON){
                ItemButtonViewHolder item = new ItemButtonViewHolder(view);
                return item;
            }
            else if(viewType == KeyValuePair.EDIT_TYPE_BUTTON_TEXT){
                ItemButtonTextViewHolder item = new ItemButtonTextViewHolder(view);
                return item;
            }
            else if(viewType == KeyValuePair.EDIT_TYPE_BUTTON_TEXT_EDIT){
                ItemButtonEditViewHolder item = new ItemButtonEditViewHolder(view);
                return item;
            }
            else if(viewType == KeyValuePair.EDIT_TYPE_CHECKBOX){
                ItemCheckboxGroupViewHolder item = new ItemCheckboxGroupViewHolder(view);
                return item;
            }
            else if(viewType == KeyValuePair.EDIT_TYPE_CHECKBOX_SINGLE){
                ItemCheckboxViewHolder item = new ItemCheckboxViewHolder(view);
                return item;
            }
            else if(viewType == KeyValuePair.EDIT_TYPE_INPUT_GROUP){
                ItemEditGroupViewHolder item = new ItemEditGroupViewHolder(view);
                return item;
            }
            else if(viewType == KeyValuePair.EDIT_TYPE_INPUT_LOCKED){
                ItemEditLockedViewHolder item = new ItemEditLockedViewHolder(view);
                return item;
            }
            else if(viewType == KeyValuePair.EDIT_TYPE_LABEL){
                ItemLabelViewHolder item = new ItemLabelViewHolder(view);
                return item;
            }
            else{
                // TODO: 5/10/2017 add button input here
                ItemEditViewHolder item = new ItemEditViewHolder(view);
                return item;
            }
        }
        else {//if(VIEW_TYPE == TYPE_LIST){
            if(viewType == KeyValuePair.EDIT_TYPE_BUTTON){
                ItemButtonViewHolder item = new ItemButtonViewHolder(view);
                return item;
            }
            else{
                ItemListViewHolder item = new ItemListViewHolder(view);
                return item;
            }
        }
    }


    /**
     * Set custom values, settings, listeners, etc to the inflated xml layout.
     * This gets initialized everytime the UI containing this class is refreshed
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position){
        if(VIEW_TYPE == TYPE_LABEL){
            /*
            // Allow displaying of TextBox on EDIT_TYPE_INPUT_GROUP during ACTION_VIEW
            if(getItemViewType(position) == KeyValuePair.EDIT_TYPE_INPUT_GROUP){
                // display checkbox group data labels
                ((ItemEditGroupViewHolder) holder).label.setText(content.get(position).getLabel());
                ((ItemEditGroupViewHolder) holder).displayGroupData();
            }
            else {
                // display header title
                ((ItemLabelViewHolder) holder).label.setText(content.get(position).getLabel());
                // display data value, if any
                ((ItemLabelViewHolder) holder)._content.setText(content.get(position).getValue());
            }
            */

            //Log.d(TAG, "position: " + AppUtilities.INSTANCE.numberToString(position) + ", TYPE: " + AppUtilities.INSTANCE.numberToString(getItemViewType(position)) + ", label: " +
            //    content.get(position).getValue());
            if(getItemViewType(position) == KeyValuePair.EDIT_TYPE_HEADER){
                // display header title
               //Log.d(TAG, "value === " + content.get(position).getValue());
                ((ItemHeaderViewHolder) holder).label.setText(content.get(position).getValue());
            }
            else if(getItemViewType(position) == KeyValuePair.EDIT_TYPE_DIVIDER_HORIZONTAL){
                //Log.d(TAG, "--displaying DIVIDER");
            }
            else{
                //Log.d(TAG, "key-pair value = " + content.get(position).getValue());
                // display header title
                ((ItemLabelViewHolder) holder).label.setText(content.get(position).getLabel());
                // display data value, if any
                ((ItemLabelViewHolder) holder)._content.setText(content.get(position).getValue());
            }
        }
        else if(VIEW_TYPE == TYPE_EDIT){
            if(getItemViewType(position) == KeyValuePair.EDIT_TYPE_BUTTON){
                // display button name
                ((ItemButtonViewHolder) holder).button.setText(content.get(position).getLabel());
            }
            else if(getItemViewType(position) == KeyValuePair.EDIT_TYPE_BUTTON_TEXT){
                // display button name
                ((ItemButtonTextViewHolder) holder).button.setText(content.get(position).getLabel());
                ((ItemButtonTextViewHolder) holder).textContent.setText(content.get(position).getValue());
                if(!content.get(position).isFocusable()){
                    ((ItemButtonTextViewHolder) holder).button.setEnabled(false);
                }
            }
            else if(getItemViewType(position) == KeyValuePair.EDIT_TYPE_BUTTON_TEXT_EDIT){
                // display button name
                ((ItemButtonEditViewHolder) holder).button.setText(content.get(position).getLabel());
                ((ItemButtonEditViewHolder) holder).textContent.setText(content.get(position).getValue());
            }
            else if(getItemViewType(position) == KeyValuePair.EDIT_TYPE_CHECKBOX){
                // display checkbox group data labels
                ((ItemCheckboxGroupViewHolder) holder).label.setText(content.get(position).getLabel());
                ((ItemCheckboxGroupViewHolder) holder).displayGroupData((content.get(position).isFocusable()) ? true : false);
            }
            else if(getItemViewType(position) == KeyValuePair.EDIT_TYPE_INPUT_GROUP){
                // display checkbox group data labels
                ((ItemEditGroupViewHolder) holder).label.setText(content.get(position).getLabel());
                ((ItemEditGroupViewHolder) holder).displayGroupData((content.get(position).isFocusable()) ? true : false);
            }
            else if(getItemViewType(position) == KeyValuePair.EDIT_TYPE_CHECKBOX_SINGLE){
                // display checkbox group data labels
                ((ItemCheckboxViewHolder) holder).checkBox.setText(content.get(position).getLabel());
            }
            else if(getItemViewType(position) == KeyValuePair.EDIT_TYPE_INPUT_LOCKED){
                // display header title
                ((ItemEditLockedViewHolder) holder).label.setText(content.get(position).getLabel());
                // display data value, if any
                ((ItemEditLockedViewHolder) holder).textView.setText(content.get(position).getValue());

                // display custom value
                if(content.get(position).isValueShown()){
                    //Log.d("--show value", content.get(position).getValue() + "!");
                    ((ItemEditLockedViewHolder) holder).textView.setText(content.get(position).getValue());
                }
            }
            else if(getItemViewType(position) == KeyValuePair.EDIT_TYPE_LABEL){
                ((ItemLabelViewHolder) holder).label.setText(content.get(position).getLabel());
                ((ItemLabelViewHolder) holder)._content.setText(content.get(position).getValue());
            }
            else{
                // display header title
                ((ItemEditViewHolder) holder).label.setText(content.get(position).getLabel());
                // display data value, if any
                ((ItemEditViewHolder) holder).editText.setText(content.get(position).getValue());

                // disable text field
                if(!content.get(position).isFocusable()) {
                    ((ItemEditViewHolder) holder).editText.setFocusable(false);
                    ((ItemEditViewHolder) holder).editText.setTextColor(getTextColorDisabled());
                }

                if(content.get(position).isDisabled()) {
                    ((ItemEditViewHolder) holder).editText.setEnabled(false);
                    ((ItemEditViewHolder) holder).editText.setBackground(getTextBackgroundDisabled());
                }

                // display custom value
                if(content.get(position).isValueShown()){
                    //Log.d("--show value", content.get(position).getValue() + "!");
                    ((ItemEditViewHolder) holder).editText.setText(content.get(position).getValue());
                }
            }
        }
        else{
            // list
            ((ItemListViewHolder) holder).label.setText(content.get(position).getListHeader());
            ((ItemListViewHolder) holder)._content.setText(content.get(position).getListContent());
            ((ItemListViewHolder) holder).footer.setText(content.get(position).getListFooter());
        }
    }


    /**
     * Get a list item's content type, previously set inside the HashMap<Integer, KeyValuePair> content
     * @param position  index of selected item from the internal content HashMap
     * @return      selected item's type: (KeyValuePair) EDIT_TEXT_TYPE =
     *              EDIT_TYPE_INPUT, EDIT_TYPE_TEXTAREA, EDIT_TYPE_BUTTON
     */
    @Override
    public int getItemViewType(int position){
        return content.get(position).getEditTextType();
    }


    /**
     * Get the total item count (rows) from this adapter
     * @return
     */
    @Override
    public int getItemCount(){
        return content.size();
    }


    /**
     * Get an item's content value
     * @param position  index of selected item from the internal content HashMap
     * @return  String data value of selected item
     */
    public String getContent(int position){
        return content.get(position).getValue();
    }


    /**
     * Get the whole KeyValuePair containing object of a selected item
     * @param position  index of selected item from the internal content HashMap
     * @return  KeyValuePair parent of selected item
     */
    public KeyValuePair getObjectAt(int position){
        return content.get(position);
    }


    /**
     * Set this adapter's overall view type:
     * @param viewType  TYPE_LABEL, TYPE_EDIT, TYPE_OTHER, TYPE_LIST
     */
    public void setViewType(int viewType){
        VIEW_TYPE = viewType;
    }


    /**
     * Set the internal data value of an item using its index or position in the adapter
     * @param position  index of selected item from the internal content HashMap
     * @param value     Value to be set for the selected item
     */
    public void setData(int position, String value){
        //Log.d("--setting ADAPT", content.get(position).getValue() + ", to " + value);

        if(getItemViewType(position) == KeyValuePair.EDIT_TYPE_BUTTON_TEXT){
            //Log.d(TAG, "is button-text");
        }

        content.get(position).setValue(value);
        notifyDataSetChanged();
    }


    /**
     * Set the internal data value of an item in [content] using its String key map
     * @param key   firebase/sqlite table field name that maps to a value
     * @param value value to replace
     */
    public void setData(String key, String value){
        int index = -1;
        for(int i=0; i<content.size(); i++){
            if(content.get(i).getColumnField().equals(key)){
                index = i;
                break;
            }
        }

        if(index != -1){
            content.get(index).setValue(value);
            notifyDataSetChanged();
        }
    }


    /**
     * Insert a new item of KeyValuePair.EDIT_TEXT_TYPE
     * @param KEYVALUEPAIR_INPUT_TYPE   any type for KeyValuePair.EDIT_TEXT_TYPE
     * @param item      Pre-configured KeyValuePair item
     * @param columnFieldName   Common name identification for the newly-created item

    public void insertData(int KEYVALUEPAIR_INPUT_TYPE, KeyValuePair item, String columnFieldName){
        //Log.d(TAG, "item inserted");
        String ID = (columnFieldName != null) ? columnFieldName : "new_group";

        switch(KEYVALUEPAIR_INPUT_TYPE){
            case KeyValuePair.EDIT_TYPE_INPUT_GROUP: {
                countNewGrouped++;
                KeyValuePair groupedView = new KeyValuePair(
                        AppUtilities.INSTANCE.numberToString(countNewGrouped),   // label
                        ID,     // column field name
                        // data
                        ApplicationContextProvider.getContext().getResources()
                                .getStringArray(R.array.fb_meta_fertilizer_new));

                //Log.d(TAG, "added NEW FERTILIZER No. " + AppUtilities.INSTANCE.numberToString(countNewGrouped) +
                //        " at LIST POSITION " + AppUtilities.INSTANCE.numberToString(content.size()));
                content.put(content.size(), groupedView);

            } break;
            default: break;
        }

        // update existing items
        notifyDataSetChanged();
    }
     */
}



