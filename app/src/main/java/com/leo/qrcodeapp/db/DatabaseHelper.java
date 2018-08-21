package com.leo.qrcodeapp.db;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.leo.qrcodeapp.utils.AppUtilities;

/**
 * Created by Angel on 2/19/2017.
 * A query class helper for the local sqlite3 database
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper sInstance;
    public SQLiteDatabase db;
    private String LOG = "!!! DB-SQ3";

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "QrcodeApp.db";

    public static synchronized DatabaseHelper getsInstance(Context context){
        if(sInstance == null){
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public DatabaseHelper(final Context context){
        // super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // make database viewable on external sdcard

        super(context, AppUtilities.INSTANCE.getDatabaseStorageLocation(context) + "/" + DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("---", "Storage location: " + AppUtilities.INSTANCE.getDatabaseStorageLocation(context));
    }

    /**
     * Create local sqlite tables. Insert constant values for selection
     * @param db    sqlite3 database
     */
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG, DatabaseContract.SQL_CREATE_ACCOUNT);
        db.execSQL(DatabaseContract.SQL_CREATE_ACCOUNT);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        /*
        String[] tables = {TreeContract.TreeTable.TABLE_NAME, TreeContract.AccountTable.TABLE_NAME,
                TreeContract.RfidTable.TABLE_NAME, TreeContract.HistoryTable.TABLE_NAME,
                TreeContract.TreeLocalName.TABLE_NAME, TreeContract.TreeDistTable.TABLE_NAME };

        for(int i=0; i<tables.length; i++){
            db.execSQL(tables[i]);
        } */
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long insertDB(String tableName, String[] fields, String[] values){
        // performs update on row, or insert if it does not yet exist
        db = getWritableDatabase();
        long newRowId = -1;

        ContentValues content = new ContentValues();

        for(int i=0; i<fields.length; i++){
            content.put(fields[i], values[i]);
            if(values[i] != null)
                Log.d("db>> " + fields[i], values[i]);
        }

        try {
            newRowId = db.insert(tableName, null, content);
        }
        catch (Exception e){
            Log.d("Error inserting", e.toString());
        }

        db.close();
        return newRowId;
    }

    public String insertDBTransaction(String tableName, String[] fields, String[] values){
        if(fields.length != values.length){
            Log.d("db>>", "Cannot insert data fields does not match values length");
        }

        String query = "INSERT INTO " + tableName + " (";

        for(int i=0; i<fields.length; i++)
            query += fields[i] + ", ";

        query = query.substring(0, query.length()-2) + ")";

        query += " VALUES (";

        for(int i=0; i<values.length; i++)
            query += "'" + values[i] + "', ";

        query = query.substring(0, query.length()-2) + ")";
        Log.d("query", query);
        return query;
    }

    public Cursor searchDbColumns(String tableName, String[] fields, String[] selection, String[] args){
        db = getReadableDatabase();

        String columns = "";
        for(int i=0; i<fields.length; i++){
            columns += fields[i];
            if(i < fields.length-1)
                columns +=  ", ";
        }

        String query = "SELECT " + columns + " FROM " + tableName;

        if(selection.length > 0){
            query += " WHERE ";

            for(int i=0; i<selection.length; i++){
                query += selection[i] + " = " + args[i] + " AND ";
            }
        }

        query = query.substring(0, query.length()-5);
        query += " ORDER BY _id ASC";
        Log.d("query:", query);
        Cursor c = db.rawQuery(query, null);

        return c;
    }


    /**
     * Fetch records from a table that matches a list of identifying fields, set to a limit
     * @param tableName Name of table to fetch records from
     * @param fields    Fields search from the table
     * @param selection List of field names from the table to match the search
     * @param args      List of values that correspond to selection[]
     * @param limit     Limit the number of returned records
     * @return
     */
    public Cursor searchDbColumns(String tableName, String[] fields, String[] selection, String[] args, String limit){
        db = getReadableDatabase();

        String columns = "";
        for(int i=0; i<fields.length; i++){
            columns += fields[i];
            if(i < fields.length-1)
                columns +=  ", ";
        }

        String query = "SELECT " + columns + " FROM " + tableName;

        if(selection.length > 0){
            query += " WHERE ";

            for(int i=0; i<selection.length; i++){
                query += selection[i] + " = " + args[i] + " AND ";
            }
        }

        query = query.substring(0, query.length()-5);
        query += (limit != null) ? " ORDER BY _id ASC LIMIT " + limit : "";
        Log.d("query:", query);
        Cursor c = db.rawQuery(query, null);

        return c;
    }

    /**
     * Retrieve table field values given the following parameters
     * @param tableName     name of table to search
     * @param projection    Array[] string order of column fields to retrieve
     * @param selection     single table field to check against
     * @param selectionArgs single value to check against existing table field
     * @param sortOder      ASC or DESC
     * @param sortArg       table field on which to base sorting. Default is null
     * @return
     */
    public Cursor searchDB(String tableName, String[] projection, String selection,
                           String selectionArgs, String sortOder, String sortArg){
        db = getReadableDatabase();

        if(selection != null)
            selection = selection + " = ? ";
        String[] selectionArguments = { selectionArgs };

        String sorting = "_id " + sortOder;
        if(sortArg != null){
            sorting = sortArg + " " + sortOder;
        }

        Cursor cursor = db.query(
                tableName,      // table name
                projection,      // table columns to return from query
                selection,      // table column to check
                selectionArguments,  // values to check against selection column
                null,
                null,
                sorting
        );

        return cursor;
        // db.close();
    }


    /**
     * Retrieve all columns of a record from tableName
     * @param tableName name of table on which to retrieve all columns
     * @return
     */
    public Cursor searchDB(String tableName){
        db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + tableName, null);
        return c;
    }


    /**
     * Select fields from two or more tables using LEFT JOIN
     * @param tableNames    String[] array of table names to search from
     * @param joinedFields  String[] array of unordered fields to search from requested tables, following the format <tableName>.<fieldName>
     * @param onArgs        String[] array of ordered arguments for the ON connector, following the order of tables in tableNames[].
     *                      Does not contain entry for tableNames[0], where this is the main table
     * @param whereArgs     String[] array of ordered arguments for the WHERE selection, following the ordering from onArgs[], if custWhere is null (default)
     *                      Does not contain entry for tableNames[0], where this is the main table, if custWhere is null (default)
     *                      String[] array of unordered arguments for the WHERE selection, following the format <tableName>.<fieldName> "=" <tableName>.<fieldName>
     *                      if custWhere is true
     * @param selection     String[] array of identifying columns (foreign key) selection for all tables
     * @param selectionArgs String[] array of values to check for selection[]. Should be ordered as selection[]'s ordering
     * @param countSelection    Count(*) selection query, following the format: COUNT(*) FROM <tableName> where <tableName> is the first entry in the tableNames[] parameter
     * @param groupBy       Group by selection string
     * @param customWhere   flag to use user-defined complex (unordered) WHERE clause, following the format <tableName>.<fieldName> "=" <tableName>.<fieldName>
     * @param distinct  DISTINCT rows selection flag
     * @param custSelection     user-defined complete SELECTION arguments
     * @return  Cursor of searched rows
     */
    public Cursor searchDBTables(String[] tableNames, String[] joinedFields, String[] onArgs, String[] whereArgs, String[] selection, String[] selectionArgs,
                                 String countSelection, String groupBy, Boolean customWhere, Boolean distinct, String custSelection){
        db = getReadableDatabase();
        String getFields = "";

        boolean isDistinct = (distinct != null) ? distinct : false;
        boolean userWhere = (customWhere != null) ? customWhere : false;

        // format joined table fields for selection
        for(int i=0; i<joinedFields.length; i++)
            getFields += joinedFields[i] + ", ";
        getFields = getFields.substring(0, getFields.length()-2);

        // format DISTINCT selection
        String distinctSelection = (isDistinct) ? "DISTINCT" : "";

        // format COUNT selection
        String query = "SELECT " + distinctSelection + " " + getFields;
        query += (countSelection != null) ? ", " + countSelection  : " FROM " + tableNames[0];

        // format LEFT JOIN query string
        // always starts at 2nd tableNames[] entry (index 1), because 1st entry is already in the SELECT field
        String body = "";
        for(int i=1; i<tableNames.length; i++){
            // Added an equality checker for String arguments
            String str = " LEFT JOIN " + tableNames[i] + " ON " + tableNames[i] + "." + onArgs[i-1] + "=" + tableNames[0] + "." + onArgs[i-1];
            Log.d("dbh: ", str);
            body += str;
        }

        // format WHERE query string
        String where = " WHERE ";
        for(int i=0; i<whereArgs.length; i++)
            where += (userWhere) ? whereArgs[i] + " AND " :
                tableNames[i+1] + "." + onArgs[i] + "=" + tableNames[0] + "." + whereArgs[i] + " AND ";
        where = where.substring(0, where.length() - (" AND ").length());

        query += body;
        query += where;

        if(selection != null && selectionArgs != null){
            for(int i=0; i<selection.length; i++){
                if(selectionArgs[i].contains(" IN "))
                    query += " AND " + selection[i] + " IN " + selectionArgs[i].replace("IN", "");
                else
                    query += " AND " + selection[i] + "=" + selectionArgs[i];
            }
        }

        // custom selection
        if(custSelection != null)
            query += " AND " + custSelection;

        // group by clause
        query += (groupBy != null) ? " GROUP BY " + groupBy : "";
        Log.d(">> QUERY", query);

        Cursor c = db.rawQuery(query, null);
        return c;

    }

    public Cursor selectAll(String tableName){
        db = getReadableDatabase();
        String sql = "SELECT * FROM " + tableName;
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor customQuery(String query){
        db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        return c;
    }

    public int update(String tableName, String[] fields, String[] values,
                       String selection, String selectionArgs){
        db = getWritableDatabase();
        ContentValues content = new ContentValues();

        for(int i=0; i<fields.length; i++){
            content.put(fields[i], values[i]);
        }

        String selectionCheck = selection + " LIKE ?";
        String[] selectionArguments = { selectionArgs };

        int count = db.update(
                tableName,
                content,
                selectionCheck,
                selectionArguments
        );

        return count;
    }

    public void update(String tableName, String[] fields, String[] values,
                       String[] selection, String[] selectionArgs){
        db = getWritableDatabase();

        String query = "UPDATE " + tableName + " SET ";

        for(int i=0; i<fields.length; i++)
            query += fields[i] + "='" + values[i] + "', ";

        query = query.substring(0, query.length()-2);

        query += " WHERE ";
        for(int i=0; i<selection.length; i++)
            query += selection[i] + "=" + "'" + selectionArgs[i] + "' AND ";

        query = query.substring(0, query.length()-5);
        Log.d("query-insert", query);

        db.execSQL(query);
        db.close();
    }

    public void deleteRow(String tableName, String selection, String[] selectionArgs){
        db = getWritableDatabase();

        String selectionCheck = selection + " = ?";
        db.delete(tableName, selectionCheck, selectionArgs);
    }

    /**
     * Check if a table contains rows of data
     * @param tableName
     * @return
     */
    public boolean tableHasRows(String tableName){
        boolean hasRows = false;
        String query = "SELECT COUNT(*) AS cnt FROM " + tableName;
        Cursor c = customQuery(query);

        if(c.moveToFirst()){
            int count = c.getInt(c.getColumnIndex("cnt"));
            hasRows = (count > 0) ? true : false;
            Log.d("--table " + tableName, " data rows = " + AppUtilities.INSTANCE.numberToString(count));
        }
        c.close();
        return hasRows;
    }

    /**
     * Delete all entries from a table and reset its numbering index to 1
     * @param restart   flag to restart the table's autoincrement index back to 1
     * @param tableName
     */
    public void emptyTable(String tableName, Boolean restart){
        db = getWritableDatabase();
        boolean restartCount = (restart != null) ? restart : false;

        // check if table has rows
        if(tableHasRows(tableName)){
            String query = "DELETE FROM " + tableName + ";";

            if(restartCount)
                query += " DELETE FROM sqlite_sequence WHERE name = '" + tableName + "'; SELECT * FROM sqlite_sequence;";

            Log.d("-->> deleting from", tableName + "\n" + query);
            db.execSQL(query);
        }
    }


    /**
     * Check if a record exists in a table from the database
     * @param tableName     name of table to search
     * @param projection    Array[] string order of column fields to retrieve
     * @param selection     single table field to check against
     * @param selectionArgs single value to check against existing table field
     * @param sortOder      ASC or DESC
     * @param sortArg       table field on which to base sorting. Default is null
     * @return              boolean flag if a record exists in the database's table
     */
    public boolean recordExists(String tableName, String[] projection, String selection,
                                String selectionArgs, String sortOder, String sortArg){
        boolean exists = false;

        Cursor c = searchDB(tableName, projection, selection, selectionArgs, sortOder, sortArg);
        if (c.moveToFirst())
            exists = true;

        c.close();

        return exists;
    }

    /**
     * Check if a record exists in a table from the database, using only a single search parameter
     * @param tableName     Name of table to search from
     * @param selection     Table field name to check
     * @param selectionArg  Value of selection field
     * @return
     */
    public boolean recordExists(String tableName, String selection, String selectionArg){
        boolean exists = false;

        Cursor c = searchDB(tableName,
                new String[] { selection },
                selection, selectionArg, "ASC", null);

        if (c.moveToFirst())
            exists = true;

        c.close();

        return exists;
    }


    /**
     * Get the count/number of records in a table
     * @param tableName
     * @return
     */
    public String getCount(String tableName){
        db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT COUNT(*) AS COUNT FROM " +tableName, null);
        if(c.moveToFirst()){
            return c.getString(c.getColumnIndex("COUNT"));
        }
        else{
            return "0";
        }
    }


    /**
     * Get the public UI (ALL) field names of a standard Firebase custom class in a String[] array
     * Includes the default node settings field names
     * @param modelClass     name of custom firebase class with member variables
     * @return
     */
    public static String[] getClassFieldNames(Class modelClass){
        Field[] fields = modelClass.getFields();
        ArrayList<String> temp = new ArrayList<>();

        // ommit last (4): $change and serialVersionUID; plus default model
        ArrayList<String> excluded = new ArrayList<>();
        excluded.add("serialVersionUID");
        excluded.add("$change");

        for(int i=0; i<fields.length; i++){
            if(!excluded.contains(fields[i].getName())){
                temp.add(fields[i].getName());
                // Log.d(LOG, "--field name adding " + fields[i].getName());
            }

        }

        String[] array = temp.toArray(new String[0]);
        return array;
    }



    /**
     * Creates a query for creating a local sqlite table for the base Firebase node
     * @param META  Firebase node ID of table to be created
     * @param firebaseClass     Any class with simple member variables
     * @return      Sqlite query for creating a local database with firebaseClass's
     *              member variables as fields
     */
    public static String initLocalSchema(String META, Class firebaseClass){
        String query = "CREATE TABLE ";
        query += META + " (";

        String[] fields = getClassFieldNames(firebaseClass);
        for(int i=0; i<fields.length; i++){
            query += fields[i] + " TEXT, ";
        }

        query = query.substring(0, query.length()-2);
        query += ")";
        Log.d("--QUERY", query);
        return query;
    }


    /**
     * Creates a query for creating a local sqlite table for the base Firebase node
     * Uses a unique, local autoincrement index
     * @param META  Firebase node ID of table to be created
     * @param firebaseClass     Any class with simple member variables
     * @return      Sqlite query for creating a local database with firebaseClass's
     *              member variables as fields
     */
    public static String initLocalSchemaIndexed(String META, Class firebaseClass){
        String query = "CREATE TABLE ";
        query += META + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, ";

        String[] fields = getClassFieldNames(firebaseClass);
        for(int i=0; i<fields.length; i++){
            query += fields[i] + " TEXT, ";
        }

        query = query.substring(0, query.length()-2);
        query += ")";
        Log.d("--QUERY", query);
        return query;
    }
}
