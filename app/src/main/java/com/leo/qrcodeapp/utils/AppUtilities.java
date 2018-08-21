package com.leo.qrcodeapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static com.leo.qrcodeapp.utils.ApplicationContextProvider.getContext;

/**
 * Created by mbarua on 9/27/2017.
 * Various code helper utilities
 */

public enum AppUtilities {
    INSTANCE;
    private String TAG = "!APP-UTILS";

    // Data for multiple choice checkbox items
    public String selectedData = "";
    public String selectedOthers = "";


    /**
     * Reset selected multiple choice list and others items
     */
    public void resetSelection(){
        selectedData = "";
        selectedOthers = "";
    }


    /**
     * Set encoded others data from multiple choice list selection
     * @param data
     */
    public void setSelectedOthers(String data){
        selectedOthers = data;
    }


    /**
     * Set checked selected items from multiple choice list selection
     * @param data
     */
    public void setSelectedData(String data){
        if(selectedData != null) {
            if (selectedData.equals(""))
                selectedData += data;
            else
                selectedData += "," + data;
        }
        else selectedData = data;
    }


    /**
     * Remove un-checked item from multiple choice list selection
     * @param data
     */
    public void removeSelectedData(String data){
        selectedData = selectedData.replace(data, "");
        selectedData = selectedData.replace(",,", ",");
        selectedData = selectedData.replace(" ,", ",");

        if(selectedData.startsWith(","))
            selectedData = selectedData.substring(1, selectedData.length());

        if(selectedData.endsWith(","))
            selectedData = selectedData.substring(0, selectedData.length()-1);
    }


    /**
     * Hide the softkeyboard using the calling Activity's context
     * @param activity
     */
    public void hideSoftKeyboard(Activity activity) {
        if(activity == null)
            return;

        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);

        try{
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
        catch(Exception e){
            Log.d("--", "Window not visible");
        }
    }


    /**
     * A generic class for converting Number (integer, double, etc) to String
     * @param num   Number to convert to String representation
     * @param <E>   Generic type. Accepts any type of Number to convert to String representation
     */
    public <E> String numberToString(E num){
        StringBuilder sb = new StringBuilder();
        sb.append(num);
        return sb.toString();
    }


    /**
     * Checks if external storage is available. Read and write permissions must have been previously set in
     * the manifest file
     * */
    public boolean isExternalStorageWritable(Context context) {
        if(Build.VERSION.SDK_INT >= 19){
            File[] sdcards = context.getExternalFilesDirs(null);
            Log.d("--sdcards length", AppUtilities.INSTANCE.numberToString(sdcards.length));
            return (sdcards.length == 2 && sdcards[1] != null) ? true : false;
        }
        else{
            // Add storage location path detection support for Android 4.3 and lower (API 16 below)
            // Add support for Huawei API 16 (Android 4.3)
            File[] files = RemovableStorage.getPossibleRemovableSDCardDirectories(ApplicationContextProvider.getContext());
            return (files.length == 2 && files[1] != null) ? true : false;
        }
    }


    /**
     * Return a valid database installation location. Prefer external sdcard, and revert to
     * internal sdcard if there's no external sdcard present
     * */
    public String getDatabaseStorageLocation(Context context){
        // TODO: 3/28/2017 add checks for unmounted storage, write permssions, etc.
        File[] sdcard = new File[]{};
        String dbPath = null;

        if(Build.VERSION.SDK_INT >= 19){
            sdcard = context.getExternalFilesDirs(null);
            dbPath = sdcard[0].getAbsolutePath();
        }
        else{
            // Add storage location path detection support for Android 4.3 and lower (API 16 below)
            // Add support for Huawei API 16 (Android 4.3)
            File[] files = RemovableStorage.getPossibleRemovableSDCardDirectories(ApplicationContextProvider.getContext());
            if(files != null){
                for(int i=0; i<files.length; i++){
                    Log.d(TAG, "file # " + i + ": " + files[i].getAbsolutePath() + "\n" + files[i].getPath());
                }
                sdcard = files;
                dbPath = sdcard[0].getAbsolutePath();
            }
        }

        if(isExternalStorageWritable(context)){
            dbPath = sdcard[1].getAbsolutePath();
        }

        Log.d("--creating DB", "in EXTERNAL\n" + dbPath);
        return dbPath;
    }


    /**
     * Get the current date based on device time
     * @return      The current date, formatted as a human readable string
     */
    public String currentDate(boolean isSimple){
        return DateFormat.getDateInstance().format(new Date()).toString();
    }


    /**
     * Get the current date based on device time, formatted in YYYY/NN/DD_HH-mm-ss
     * @return      The current raw date following the programmed format
     */
    public String currentDate(){
        return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
    }


    /**
     * Detect if network connection is present
     * @return  boolean true|false flag if network connection is present
     */
    public boolean hasNetworkConnection(){
        ConnectivityManager cm = (ConnectivityManager) getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        //Log.d(TAG, activeNetwork.getTypeName());

        boolean hasConnection = ((activeNetwork != null) && (activeNetwork.isConnectedOrConnecting()));
        return hasConnection;
    }


    /**
     * Get the network type that is currently being used by the device
     * @return
     */
    public int getNetworkType(){
        int networkType = -1;

        ConnectivityManager cm = (ConnectivityManager) getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if((activeNetwork != null) && (activeNetwork.isConnectedOrConnecting())){
            networkType = activeNetwork.getType();
            Log.d(TAG, activeNetwork.getTypeName());
        }

        return networkType;
    }


    /**
     * Create a <String, String> HashMap given array values
     * @param key       Name of the key to map the value
     * @param value     Data value for the key
     * @return
     */
    public HashMap<String, String> createFertilizer(String[] key, String[] value){
        HashMap<String, String> map = new HashMap<>();

        for(int i=0; i<key.length; i++){
            map.put(key[i], value[i]);
        }

        return map;
    }


    /**
     * Add a prefix to each entry in the fields array
     * @param fields    String array
     * @param prefix    String to append as prefix to each element in fields
     * @return
     */
    public String[] stringArrayPrefix(String[] fields, String prefix){
        ArrayList<String> newArray = new ArrayList<>();

        for(int i=0; i<fields.length; i++){
            newArray.add(prefix + fields[i]);
        }

        return newArray.toArray(new String[0]);
    }


    /**
     * Add a suffix to each entry in the fields array
     * @param fields    String array
     * @param suffix    String to append as suffix to each element in fields
     * @return
     */
    public String[] stringArraySuffix(String[] fields, String suffix){
        ArrayList<String> newArray = new ArrayList<>();

        for(int i=0; i<fields.length; i++){
            newArray.add(fields[i] + suffix);
        }

        return newArray.toArray(new String[0]);
    }

    /**
     * Add an optional prefix and suffix to each entry in the fields array
     * @param fields    String array
     * @param suffix    String to append as suffix to each element in fields
     * @return
     */
    public String[] stringArrayPadding(String prefix, String[] fields, String suffix){
        ArrayList<String> newArray = new ArrayList<>();

        for(int i=0; i<fields.length; i++){
            newArray.add(prefix + fields[i] + suffix);
        }

        return newArray.toArray(new String[0]);
    }


    /**
     * Add an optional prefix and suffix to each entry in the fields array
     * @param fields    String array
     * @param suffix    String to append as suffix to each element in fields
     * @param trimAtEnd Trim (1) character at the end of the string
     * @return
     */
    public String toStringArrayPadding(String prefix, String[] fields, String suffix, boolean trimAtEnd){
        String string = "";

        for(int i=0; i<fields.length; i++){
            string += prefix + fields[i] + suffix;
        }

        if(trimAtEnd)
            string = string.substring(0, string.length()-1);
        return string;
    }


    /**
     * Create an array of blank "" String
     * @param num   number of blank elements in the array
     * @param character    character to substitue with blanks
     * @return
     */
    public String[] stringArrayBlanks(int num, String character){
        ArrayList<String> array = new ArrayList<>();
        for(int i=0; i<num; i++)
            array.add((character!=null) ? character : "");
        return array.toArray(new String[0]);
    }


    /**
     * Create an array of strings
     * @param string    String to populate the array
     * @param num
     * @return
     */
    public String[] stringArrayCreate(String string, int num){
        ArrayList<String> array = new ArrayList<>();
        for(int i=0; i<num; i++){
            array.add(string);
        }
        return array.toArray(new String[0]);
    }


    /**
     * Check if a String array contains a certain value
     * @param array String array to search value from
     * @param item Value to search in String array
     * @return  Boolean true|false if item exists in array
     */
    public boolean arrayHasValue(String[] array, String item){
        return Arrays.asList(array).contains(item);
    }


    /**
     * Return a String[] array of keys or key values in a HashMap<String, Object> container
     * @param data      A set of keys and values from a HashMap<String, String>
     * @param getKey    Flag to retrieve the keys. Null or false will get the key-data value
     * @return  String[] array of keys from a HashMap
     */
    public String[] getStringArrayKeys(HashMap<String, Object> data, boolean getKey){
        ArrayList<String> fields = new ArrayList<>();
        for(String key : data.keySet()){
            if(getKey)
                fields.add(key);
            else
                fields.add((data.get(key) != null) ? data.get(key).toString() : "");
        }
        return fields.toArray((new String[0]));
    }


    /**
     * Return an Object[] array of keys or key values in a HashMap<String, Object> container
     * @param data      A set of keys and values from a HashMap<String, String>
     * @param getKey    Flag to retrieve the keys. Null or false will get the key-data
     * @return  Object[] array of (simple String) keys from a HashMap
     */
    public Object[] getStringArrayKeysObj(HashMap<String, Object> data, boolean getKey){
        ArrayList<Object> fields = new ArrayList<>();
        for(String key : data.keySet()){
            if(getKey)
                fields.add(key);
            else
                fields.add(data.get(key).toString());
        }
        return fields.toArray((new Object[0]));
    }


    /**
     * Append Strings to an existing String[] array
     * @param original  String array's original contents
     * @param append    Array of an array Strings to be appended to original String array
     * @return
     */
    public String[] stringArrayAppend(String[] original, String[]... append){
        ArrayList<String> data = new ArrayList<>();

        // append original
        for(int i=0; i<original.length; i++)
            data.add(original[i]);

        // append new strings
        for(int i=0; i<append.length; i++){
            for(int j=0; j<append[i].length; j++){
                data.add(append[i][j]);
            }
        }

        return data.toArray(new String[0]);
    }


    /**
     * Subtract Strings from an existing String[] array
     * @param original  String array's original contents
     * @param subtract    Array of Strings to be subtracted from original String array
     * @return
     */
    public String[] stringArraySubtract(String[] original, String[] subtract){
        ArrayList<String> data = new ArrayList<>();

        // append original, ignoring subtract
        for(int i=0; i<original.length; i++){
            if(!Arrays.asList(subtract).contains(original[i])){
                data.add(original[i]);
            }
        }

        return data.toArray(new String[0]);
    }


    /**
     * Convert an ArrayList<String> to a String[] array
     * @param data  ArrayList<String> data input
     * @return  String[] array
     */
    public String[] arraylistToString(ArrayList<String> data){
        return data.toArray(new String[0]);
    }


    /**
     * Convert an array of strings into a comma-separated string
     * @param array     Array of Strings to convert
     * @return
     */
    public String arrayToString(String[] array){
        String string = "";
        for(int i=0; i<array.length; i++)
            string += array[i] + ", ";
        string = string.substring(0, string.length()-2);
        return string;
    }


    /**
     * Cast an int to Integer
     * @param number    int to convert to Integer
     * @return
     */
    public Integer getInteger(int number){
        return number;
    }


    /**
     * Get the values of a HashMap<String, String>
     * @param map   HashMap of Strings, indexed with Strings
     * @param fields    String[] array of fields to base ordering of values output
     * @return  String[] array of values from a HashMap
     */
    public String[] hashMapValuesToString(HashMap<String, String> map, String[] fields){
        ArrayList<String> values = new ArrayList<>();

        if(fields == null){
            for(String key : map.keySet()){
                values.add(map.get(key));
            }
        }
        else{
            for(int i=0; i<fields.length; i++){
                values.add(map.get(fields[i]));
                //Log.d(TAG, "toString, adding ... " + fields[i] + " => " + map.get(fields[i]));
            }

        }

        return arraylistToString(values);
    }


    /**
     * Get the keys of a String HashMap
     * @param map
     * @return
     */
    public String[] hashMapKeys(HashMap<String, String> map){
        ArrayList<String> values = new ArrayList<>();
        for(String key : map.keySet())
            values.add(key);
        return arraylistToString(values);
    }


    /**
     * Get the values of a HashMap<String, Object> map
     * @param map   HashMap of Objects (Strings), indexed with String keys
     * @param fields    String[] array of fields to base ordering of values output
     * @return  String[] array of values from a HashMap
     */
    public String[] hashMapObjValuesToString(HashMap<String, Object> map, String[] fields){
        ArrayList<String> values = new ArrayList<>();

        if(fields == null){
            for(String key : map.keySet()){
                values.add(map.get(key).toString());
            }
        }
        else{
            for(int i=0; i<fields.length; i++){
                if(map.get(fields[i]) != null) {
                    values.add(map.get(fields[i]).toString());
                    //Log.d(TAG, "toString, adding ... " + fields[i] + " => " + map.get(fields[i]));
                }
                else{
                    values.add("");
                }
            }
        }

        return arraylistToString(values);
    }


    public String[] hashMapValuesToStringObject(HashMap<String, Object> map, String[] fields){
        ArrayList<String> values = new ArrayList<>();

        if(fields == null){
            for(String key : map.keySet()){
                values.add(map.get(key).toString());
            }
        }
        else{
            for(int i=0; i<fields.length; i++){
                values.add(map.get(fields[i]).toString());
                //Log.d(TAG, "toString, adding ... " + fields[i] + " => " + map.get(fields[i]));
            }

        }

        return arraylistToString(values);
    }


    /**
     * Get the values of a HashMap<String, String>, given a list of fields[]
     * @param map   HashMap of Strings, indexed with Strings
     * @param fields  Array of HashMap keys to retrieve values from map
     * @return  HashMap<String, String> array of values from a HashMap
     */
    public HashMap<String, Object> hashMapValuesToMap(HashMap<String, Object> map, String[] fields){
        HashMap<String, Object> values = new HashMap<>();

        if(fields == null){
            for(String key : map.keySet()){
                if(Arrays.asList(fields).contains(key)){
                    values.put(key, map.get(key).toString());
                }

            }
        }
        else{
            for(int i=0; i<fields.length; i++){
                values.put(fields[i], map.get(fields[i]).toString());
            }

        }

        return values;
    }


    /**
     * Check if all keys in a HashMap<String, Object> has values.
     * One or more key(s) may or may not have a value
     * @param map   HashMap to check contents
     * @return
     */
    public boolean isValidMap(HashMap<String, Object> map){
        boolean valid = true;
        for(String key : map.keySet()){
            if(map.get(key) != null) {
                if (map.get(key).toString().trim().equals("") || map.get(key).equals(null)) {
                    Log.d(TAG, "--NOT VALID MAP ? " + key);
                    valid = false;
                    break;
                }
            }
            else{
                // null item
                valid = false;
                break;
            }
        }
        return valid;
    }


    /**
     * Check if all keys in a HashMap<String, Object> has values
     * @param map   HashMap to check contents
     * @param keysExclude list of keys to ignore in checking
     * @return
     */
    public boolean isValidMap(HashMap<String, Object> map, String[] keysExclude){
        boolean valid = true;
        for(String key : map.keySet()) {
            if (map.get(key) != null) {
                if (Arrays.asList(keysExclude).contains(key)) {
                    if (map.get(key).equals("") || map.get(key).equals(null)) {
                        Log.d(TAG, "--EXCLUDED VALID MAP ! " + key);
                    }
                } else {
                    if (map.get(key).toString().trim().equals("") || map.get(key).equals(null)) {
                        Log.d(TAG, "--NOT VALID MAP ? " + key);
                        valid = false;
                        break;
                    }
                }
            }
            else{
                // null item
                valid = false;
                break;
            }
        }
        return valid;
    }


    /**
     * Return the ActionBar size
     * @return
     */
    public int getActionBarSize(){
        final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        Log.d(TAG, "actionBar SIZE " + numberToString(mActionBarSize));
        styledAttributes.recycle();
        return mActionBarSize;
    }


    /**
     * Convert the Unix-timestamp firebase server time to human-readable format
     * @param serverTime
     */
    public String convertServerTime(String serverTime){
        Log.d(TAG, "server-time: " + serverTime);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return sdf.format(new Date(Long.parseLong(serverTime)));
    }


    /**
     * Convert a human-readable format date string to its  Unix-timestamp firebase server time equivalent
     * @param normalTime
     */
    public String convertToServerTime(String normalTime){
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date dateNow = null;
        long timeStamp = 0;

        try{
            dateNow = sdf.parse(normalTime);
            timeStamp = dateNow.getTime();
        }
        catch(Exception e){
            Log.d(TAG, "something went wrong :/\n" + e.toString());
        }

        return AppUtilities.INSTANCE.numberToString(timeStamp);
    }


    /**
     * Get the current local (pc/mobile) date formatted to a specified conversion
     * @param conversion    format of current date DATE_SIMPLE, DATE_READABLE, DATE_UNIX
     * @return      formatted current local (pc/mobile) date
     */
    public String getCurrentDate(int conversion){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS", Locale.US);
        String date = sdf.format(new Date());
        Date dateNow = null;
        long timeStamp = 0;
        String returnDate = date;

        try{
            dateNow = sdf.parse(date);
            timeStamp = dateNow.getTime();

        }
        catch(Exception e){
            Log.d(TAG, "something went wrong :/\n" + e.toString());
        }

        Log.d(TAG, "raw-date: " + date + "\nformatted-date: " + dateNow.toString() + "\ntimeStamp: " + AppUtilities.INSTANCE.numberToString(timeStamp) + "\nre-convert: " +
            AppUtilities.INSTANCE.convertServerTime(AppUtilities.INSTANCE.numberToString(timeStamp)));

        if(conversion == CommonFlags.INSTANCE.DATE_SIMPLE)
            returnDate = date;
        else if(conversion == CommonFlags.INSTANCE.DATE_READABLE)
            returnDate = dateNow.toString();
        else if(conversion == CommonFlags.INSTANCE.DATE_UNIX)
            returnDate = AppUtilities.INSTANCE.convertServerTime(AppUtilities.INSTANCE.numberToString(timeStamp));

        return returnDate;
    }



}
