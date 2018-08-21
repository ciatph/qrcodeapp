package com.leo.qrcodeapp.utils.listhandler;

import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * Created by mbarua on 11/2/2017.
 * A class for mapping viewable or editable resources from data to UI:
 * labels, data values and database column names
 * Works in sync with InputRecyclerAdapter
 */

public class KeyValuePair {
    /** Descriptive label, usually coming from strings resource, unless specified */
    private String label;
    /** Value of data to display in the TextView or EditBox UI */
    private String value;
    /** Column field name of data from the local sqlite table */
    private String columnField; // table column field name
    /** Miscellaneous data */
    private String data;
    /** A shorthand assignment of values to display for a list overview:
     *  [header, content, footer , id] */
    private String[] listOverview;  // [4] header, content, footer , id for list overview
    /** Sets if a data's UI is clickable of not */
    public boolean isClickable = false;
    /** Bitmap type */
    private Bitmap image;
    /** List of data to be displayed is a grouped checkbox/radio group */
    private String[] groupData;
    /** Values or status for each data in groupData */
    private String[] groupDataValue;
    /** Key-value pair of grouped data (used in ItemEditGroupViewHolder), grouped by common field name */
    private HashMap<String, Object> mapGroupData = new HashMap<>();
    private int paramType = -1;

    private int TYPE_LABEL = 0;
    private int TYPE_EDIT = 1;
    private int TYPE_LIST = 2;
    private int TYPE_EDIT_AREA = 3;
    private int VIEW_TYPE = TYPE_LABEL;
    /** Flag that the data input UI elementis a one-liner EditBox */
    public static final int EDIT_TYPE_INPUT = 4;
    /** Flag that the data input UI element is a multiple-line EditBox (TextArea) */
    public static final int EDIT_TYPE_TEXTAREA = 5;
    /** Flag that the data input UI element is Button */
    public static final int EDIT_TYPE_BUTTON = 6;
    /** Flag that the data input UI element is a Button with viewable TextView for output */
    public static final int EDIT_TYPE_BUTTON_TEXT = 7;
    /** The data input UI element is a group of 1 to N CheckBoxes */
    public static final int EDIT_TYPE_CHECKBOX = 8;
    /** The data input UI element is a single CheckBox */
    public static final int EDIT_TYPE_CHECKBOX_SINGLE = 9;
    /** The data input UI element is a group of 1 to N EditBoxes */
    public static final int EDIT_TYPE_INPUT_GROUP = 10;
    /** The EditBox input UI is a sub-item for another main field/item
     * Node name should follow the format: <main_field_name>_<subfield_name> */
    public static final int EDIT_TYPE_INPUT_SUBITEM = 11;
    /** The data is only for displaying Text, such as for titles and labels  */
    public static final int EDIT_TYPE_HEADER = 12;
    /** A horizontal line across the screen to divide UI segments */
    public static final int EDIT_TYPE_DIVIDER_HORIZONTAL = 13;
    /** Flag that the data input UI elementis a one-liner TextView, with EditBox background */
    public static final int EDIT_TYPE_INPUT_LOCKED = 14;
    /** The data input is a full-screen multiline text area */
    public static final int EDIT_TYPE_TEXTAREA_FULL = 15;
    /** Flag that the data input UI element is a Button with and editable EditText for output */
    public static final int EDIT_TYPE_BUTTON_TEXT_EDIT = 16;
    /** The data is a view-only TextView on ACTION_EDIT */
    public static final int EDIT_TYPE_LABEL = 17;
    /** Current UI for the data */
    private int EDIT_TEXT_TYPE = EDIT_TYPE_INPUT;

    public static final int CLICKABLE_TRUE = 112;
    public static final int CLICKABLE_FALSE = 113;
    public static int CLICKABLE = CLICKABLE_TRUE;

    // settings
    private boolean disabled = false;
    private boolean focusable = true;
    private boolean showValue = false;


    /**
     * Constructor used for displaying a single row content on a list overview;
     * [0]header, [1]content, [2]footer, [3]_id
     * @param overviewData
     */
    public KeyValuePair(String[] overviewData){
        listOverview = overviewData;
    }


    /**
     * Constructor that assigns a label and corresponding string value
     * @param label     Data label, displayed over the data's TextView or EditBox
     * @param value     Value of data to be displayed on UI
     */
    public KeyValuePair(String label, String value){
        this.label = label;
        this.value = value;
    }


    /**
     * Constructor that assigns a label, content and column name object with settings;
     * settings[0] = editText(EDIT_TYPE_INPUT) or textArea (EDIT_TYPE_TEXTAREA),
     * settings[1] = clickable (CLICKABLE_TRUE or CLICKABLE_FALSE)
     * @param label         Data label, displayed over the data's TextView or EditBox
     * @param columnField   Database name / identifier counterpart for that maps to label
     * @param value         Value of data to be displayed on UI
     * @param settings      Integer[] array specifying if value's UI should be:
     *                      EDIT_TYPE_INPUT, EDIT_TYPE_TEXTAREA or EDIT_TYPE_BUTTON
     */
    public KeyValuePair(String label, String columnField, String value, Integer[] settings){
        this.label = label;
        this.value = value;
        this.columnField = columnField;

        if(settings != null){
            EDIT_TEXT_TYPE = settings[0];
            isClickable = (settings[1] == CLICKABLE_TRUE) ? true : false;
        }
    }


    /**
     * Constructor that assigns a label, data value and database column name for this entry
     * @param label         Data label, displayed over the data's TextView or EditBox
     * @param columnField   Database name / identifier counterpart for that maps to label
     * @param value         Value of data to be displayed on UI
     */
    public KeyValuePair(String label, String columnField, String value){
        this.label = label;
        this.value = value;
        this.columnField = this.data = columnField;
    }


    /**
     * Constructor that assigns a label and data value. Usually used for Viewing only
     * @param label         Data label, displayed over the data's TextView or EditBox
     * @param value         Value of data to be displayed on UI
     * @param paramType     Specifies the type of UI for data
     */
    public KeyValuePair(String label, String value, int paramType){
        // uses an integer for the columnField instead of String
        this.label = label;
        this.value = value;
        this.paramType = paramType;

        //if(paramType == EDIT_TYPE_BUTTON || paramType == EDIT_TYPE_HEADER || paramType == EDIT_TYPE_DIVIDER_HORIZONTAL){
            EDIT_TEXT_TYPE = paramType;
        //}
    }


    /**
     * Constructor that accepts a bitmap for image
     * @param label A description label to display for the image.
     * @param value Keyword for this entry
     * @param img   BitMap for ImageView display
     */
    public KeyValuePair(String label, String value, Bitmap img){
        this.label = label;
        this.value = value;
        this.image = img;
    }


    /**
     * Constructor for creating data for an ItemEditInputGroup:
     * A group of N-EditTexts
     * @param label
     * @param columnField
     * @param groupData
     */
    public KeyValuePair(String label, String columnField, String[] groupData){
        this.label = label;
        this.columnField = columnField;
        this.groupData = groupData;
        this.EDIT_TEXT_TYPE = KeyValuePair.EDIT_TYPE_INPUT_GROUP;
    }

    public void setDisabled(boolean param){ disabled = param; }

    public void setFocusable(boolean param){ focusable = param; }

    public void setClickable(boolean clickable){
        this.isClickable = clickable;
    }

    public void setShowValue(boolean param) { showValue = param; }

    public void setLabel(String string) { label = string; }

    public void setValue(String string) { value = string; }

    public void setData(String string) { data = string; }

    public void setGroupData(String[] data){ groupData = data; }

    public void setGroupDataValue(String[] data){ groupDataValue = data; }

    public void setGroupDataMap(String key, String value) { mapGroupData.put(key, value); }

    public void setColumnField(String string){ this.columnField = string; }

    public void setImage(Bitmap img){ this.image = img; }

    public void setEditTextType(int itemType) { EDIT_TEXT_TYPE = itemType; }

    public String getLabel(){ return label; }

    public String getValue(){ return value; }

    public String getData(){ return data; }

    public String[] getGroupData(){ return groupData; }

    public String[] getGroupDataValue(){ return groupDataValue; }

    public HashMap<String, Object> getGroupDataMap() { return mapGroupData; }

    public String getGroupDataMap(String key) { return mapGroupData.get(key).toString(); }

    public Bitmap getImage(){ return image; }

    public int getEditTextType(){
        return EDIT_TEXT_TYPE;
    }

    public String getListHeader(){ return this.listOverview[0]; }

    public String getListContent(){ return this.listOverview[1]; }

    public String getListFooter(){ return this.listOverview[2]; }

    public String getListId(){ return this.listOverview[3]; }

    public String getColumnField(){ return this.columnField; }

    public int getParamType(){ return this.paramType; }

    public boolean isDisabled(){ return disabled; }

    public boolean isFocusable(){ return focusable; }

    public boolean isValueShown(){ return showValue; }

    public boolean itemIsClickable(){
        return isClickable;
    }
}

