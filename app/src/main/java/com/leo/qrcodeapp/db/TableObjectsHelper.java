package com.leo.qrcodeapp.db;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Angel on 2/19/2017.
 * an SQL query utility for getting fields and columns
 */

public class TableObjectsHelper {
    private String id;
    // table columns' content-value mapping
    // used by extending classes
    private HashMap<String, String> mappedContents = new HashMap<String, String>();

    // used to map field names with their names
    private HashMap<String, String> key = new HashMap<>();

    // actual values of foerign key maps
    private HashMap<String, String> mappedForeign = new HashMap<>();

    // foreign independent tables associated with entry
    // column name, value
    private HashMap<String, String> mappedTables = new HashMap<>();

    // backup for values to compare
    private HashMap<String, String> mappedBackup = new HashMap<>();

    // ordering of column fields, as they were initialized in initMapContents
    private HashMap<Integer, String> mappingOrder = new HashMap<>();

    // table name
    private String tableName = "";

    // fields request type
    private final int FIELD_CONTENTS = 0;   // all column fields, without the _ID
    private final int FIELD_FOREIGN = 1;    // all foreign key fields
    private final int FIELD_TABLES = 2;     // all "foreign" values [table_name]

    public TableObjectsHelper(){}

    public void initMapContents(String[] fields){
        // initialize HashMap contents with fields[], with ordering
        for(int i=0; i<fields.length; i++){
            mappedContents.put(fields[i], "");
            mappingOrder.put(i, fields[i]);
            key.put(fields[i], fields[i]);
        }
    }

    public void initMappedForeign(String[] fields){
        // initialize HashMap foreign key contents with fields[]
        for(int i=0; i<fields.length; i++){
            mappedForeign.put(fields[i], "");
        }
    }

    public void initMappedTables(String[] fields){
        for(int i=0; i<fields.length; i++){
            mappedTables.put(fields[i], "");
        }
    }

    private String integerToString(int num){
        StringBuilder sb = new StringBuilder();
        sb.append(num);
        return sb.toString();
    }

    public void set(String key, String value){
        // update an existing object in the HaspMap given a key
        mappedContents.put(key, value);
    }

    public void setForeign(String key, String value){
        mappedForeign.put(key, value);
    }

    public void setMappedTables(String key, String value){
        mappedTables.put(key, value);
    }

    public void setBackup(String column, String value){
        mappedBackup.put(column, value);
    }

    public void setTableName(String tableName){
        this.tableName = tableName;
    }

    public String getKey(String searchKey){
        return key.get(searchKey);
    }

    public String get(String key){
        // get he value of an object given a key
        return mappedContents.get(key);
    }

    public String getForeign(String key){
        // get he value of an object given a key
        return mappedForeign.get(key);
    }

    public String getTables(String key){
        // get he value of an object given a key
        return mappedTables.get(key);
    }

    public String getBackup(String column){
        return mappedBackup.get(column);
    }

    public String[] getColumnFields(){
        // get column fields minus the _ID
        ArrayList<String> fields = new ArrayList<>();
        for(int i=0; i<mappingOrder.size(); i++){
            fields.add(mappingOrder.get(i));
        }
        String[] array = fields.toArray(new String[0]);
        return array;
    }

    public String[] getColumnFieldsForLayout(String[] removeFields){
        // get all column fields for display in UI layout, minus removeFields and the _ID
        ArrayList<String> fields = new ArrayList<>();
        ArrayList<String> remove = new ArrayList<>();

        for(int i=0; i<removeFields.length; i++)
            remove.add(removeFields[i]);

        for(int i=0; i<mappingOrder.size(); i++){
            if(!remove.contains(mappingOrder.get(i))) {
                fields.add(mappingOrder.get(i));
            }
        }
        String[] array = fields.toArray(new String[0]);
        return array;
    }

    public String[] getValuesFields(){
        // get column field values minus the _ID
        ArrayList<String> fields = new ArrayList<>();
        for(int i=0; i<mappingOrder.size(); i++){
            fields.add(mappedContents.get(mappingOrder.get(i)));
        }
        String[] array = fields.toArray(new String[0]);
        return array;
    }

    public String[] getValuesFieldsForLayout(String[] removeFields){
        // get column field values for display in UI layout, minus minus removeFields and the _ID
        ArrayList<String> fields = new ArrayList<>();
        ArrayList<String> remove = new ArrayList<>();

        for(int i=0; i<removeFields.length; i++)
            remove.add(removeFields[i]);

        for(int i=0; i<mappingOrder.size(); i++){
            if(!remove.contains(mappingOrder.get(i))){
                fields.add(mappedContents.get(mappingOrder.get(i)));
            }
        }
        String[] array = fields.toArray(new String[0]);
        return array;
    }

    public String[] getColumnFieldsAll(){
        // get all column fields with the ID column included
        String[] columns = getColumnFields();

        ArrayList<String> combined = new ArrayList<>();
        combined.add("_did");

        for(int i=0; i<mappingOrder.size(); i++){
            combined.add(mappingOrder.get(i));
        }

        String[] newColumns = combined.toArray(new String[0]);
        return newColumns;
    }

    public ArrayList<String> getColumnFieldsList(int fieldType){
        // get an unordered arraylist of foreign field names, for "contains" matching
        ArrayList<String> foreign = new ArrayList<>();

        if(fieldType == FIELD_FOREIGN){
            for(String key : mappedForeign.keySet())
                foreign.add(key);
        }
        else if(fieldType == FIELD_CONTENTS){
            for(String key : mappedContents.keySet())
                foreign.add(key);
        }
        else if(fieldType == FIELD_TABLES){
            for(String key : mappedTables.keySet())
                foreign.add(key);
        }

        return foreign;
    }

    public String getTableName(){
        return tableName;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void removeColumn(String key){
        // remove a column from original column contents
        mappedContents.remove(key);
    }

    /**
     * Return a concatenated String following the <tableName>.<field> format
     * @param tableName Name of database table
     * @param fields    fields to be concatenated with the table name
     * @return
     */
    public String getTableColumnFormat(String tableName, String[] fields){
        // return a concatenated String of <tableName>.<field> format
        String format = "";
        for(int i=0; i<fields.length; i++)
            format += tableName + "." + fields[i] + ", ";

        format = format.substring(0, format.length()-2);
        return format;
    }

    public String getTableColumnFormat(String[] fields){
        // return a concatenated String of <tableName>.<field> format

        String[] colFields = (fields != null) ? fields : getColumnFieldsAll();
        String format = "";

        for(int i=0; i<colFields.length; i++)
            format += tableName + "." + colFields[i] + ", ";

        format = format.substring(0, format.length()-2);
        return format;
    }

    public boolean isForeign(String columnId){
        // return if a column is a foreign key (set in initMappedForeign[])
        return (mappedForeign.containsKey(columnId));

    }

    /**
     * Override the default ordering of column names, as they were first initialized.
     * getValuesFields() will be overriden with new mapping order
     * @param columns    new fields
     */
    public void setMappingOrder(String[] columns){
        mappingOrder.clear();

        for(int i=0; i<columns.length; i++)
            mappingOrder.put(i, columns[i]);
    }
}
