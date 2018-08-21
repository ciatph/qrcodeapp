package com.leo.qrcodeapp.models;

public class Attendee {
    // table name
    public static final String TABLE_NAME = "attendees";

    // account id of encoder; Account._id
    public static final String ACCT_ID = "acct_id";

    // event id to which attendee was encoded; Events._id
    public static final String EVENT_ID = "event_id";

    // attend_id, guest id of attendee (QR CODE ID)
    public static final String ATT_ID = "att_id";

    // attend_id, guest id of attendee
    public static final String ATT_NAME = "att_name";

    // date attendee was encoded
    public static final String DCREATE = "date_created";

    // date attendee details were modified
    public static final String DMODIFIED = "date_modified";
}
