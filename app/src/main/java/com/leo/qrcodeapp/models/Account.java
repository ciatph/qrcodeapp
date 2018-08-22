package com.leo.qrcodeapp.models;

import com.leo.qrcodeapp.db.DatabaseHelper;
import com.leo.qrcodeapp.db.TableObjectsHelper;

public class Account extends TableObjectsHelper{
    // table name
    public static final String tablename = "account";

    // encoder's first name
    public static final String fname = "fname";

    // encoder's middle name
    public static final String mname = "mname";

    // encoder's last name
    public static final String lname = "lname";

    // encoder's user name
    public static final String username = "username";

    // encoder's email address
    public static final String email = "email";

    // encoder's password
    public static final String password = "password";

    // account type:  1=user, 2=administrator, 3=super administrator
    public static final String acct_type = "acct_type";

    // date account was created
    public static final String date_created = "date_created";

    // date account details were modified
    public static final String date_modified = "date_modified";

    public Account(){
        initMapContents(
                DatabaseHelper.getClassFieldNames(Account.class)
        );
    }
}


