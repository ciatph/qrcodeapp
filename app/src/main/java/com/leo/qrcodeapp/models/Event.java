package com.leo.qrcodeapp.models;

import com.leo.qrcodeapp.R;
import com.leo.qrcodeapp.db.DatabaseHelper;
import com.leo.qrcodeapp.db.TableObjectsHelper;
import com.leo.qrcodeapp.utils.ApplicationContextProvider;

public class Event extends TableObjectsHelper{
    // table name
    public static final String tablename = "events";

    // account id of encoder; Account._id
    public static final String acct_id = "acct_id";

    // event name
    public static final String name = "name";

    // event venue
    public static final String venue = "venue";

    // event date
    public static final String date_event = "date_event";

    // date event was created
    public static final String date_created = "date_created";

    // date event details were modified
    public static final String date_modified = "date_modified";

    public Event(){
        initMapContents(
                DatabaseHelper.getClassFieldNames(Event.class)
        );
    }

    public Event(String[] labelsValues){
        initMapContents(
                DatabaseHelper.getClassFieldNames(Event.class)
        );

        initLabels(
                new String[]{ name, venue, date_event }, labelsValues
        );
    }
}
