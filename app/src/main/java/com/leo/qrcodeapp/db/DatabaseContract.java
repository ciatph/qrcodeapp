package com.leo.qrcodeapp.db;

import com.leo.qrcodeapp.models.Account;


/**
 * Created by mbarua on 10/11/2017.
 * Contains offline sqlite3 table definition, fields and contracts
 * Provides a common naming convention for sqlite and firebase database input fields and tables
 */

public class DatabaseContract {
    private DatabaseContract(){}


    // ---------------- MAIN APPLICATION TABLES ----------------
    // Account: modes/Account



    // ---------------- SQL CREATE TABLE query constants for FARMER METADATA TABLES ----------------
    public static final String SQL_CREATE_ACCOUNT = DatabaseHelper.initLocalSchemaIndexed(Account.TABLE_NAME, Account.class);

}
