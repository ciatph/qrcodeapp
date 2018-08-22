package com.leo.qrcodeapp.utils;


import com.leo.qrcodeapp.db.DatabaseContract;
import com.leo.qrcodeapp.db.DatabaseHelper;
import com.leo.qrcodeapp.models.Account;

/**
 * Created by mbarua on 10/13/2017.
 * Initiates creation of hard-coded tables, values and scenarios for testing
 */

public class TestScript {
    public TestScript(){}

    /**
     * Creates a default offline sqlite user, not synced with firebase
     * Please remove this script during official release
     * @param dbConn
     */
    public void createUser(DatabaseHelper dbConn){
        Account account = new Account();
        account.set(Account.username, "guest");
        account.set(Account.email, "iwashere@yahoo.com");
        account.set(Account.fname, "google");
        account.set(Account.lname, "application");
        account.set(Account.password, "123");

        dbConn.insertDB(Account.tablename,
                account.getColumnFields(), account.getValuesFields());
    }
}
