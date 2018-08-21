package com.leo.qrcodeapp.models;

public class Account {
    // table name
    public static final String TABLE_NAME = "account";

    // encoder's name
    public static final String NAME = "name";

    // encoder's password
    public static final String PASS = "password";

    // account type:  1=user, 2=administrator, 3=super administrator
    public static final String COL_ACCT_TYPE = "acct_type";

    // date account was created
    public static final String DCREATE = "date_created";

    // date account details were modified
    public static final String DMODIFIED = "date_modified";
}
