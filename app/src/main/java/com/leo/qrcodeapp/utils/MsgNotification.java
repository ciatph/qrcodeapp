package com.leo.qrcodeapp.utils;

import android.widget.Toast;

import com.leo.qrcodeapp.R;

/**
 * Created by mbarua on 10/11/2017.
 * A common class for displaying warning and error messages.
 * Contains global constants for custom error and message codes.
 */

public enum MsgNotification {
    INSTANCE;

    //
    public final int ERROR_NETWORK = 1;
    public final int ERROR_PASSWORD_MISMATCH = 2;
    public final int MSG_SIGNING_UP = 3;
    public final int MSG_REG_SUCCESS = 4;
    public final int MSG_REG_USEREXISTS = 5;
    public final int MSG_LOGIN_ERROR = 6;
    public final int MSG_LOGIN_SUCCESS = 7;
    public final int MSG_WAIT_LOADING = 8;
    public final int MSG_CHECK_INPUT = 9;
    public final int MSG_REG_PENDING = 10;
    public final int MSG_LOGIN_PASS = 11;
    public final int MSG_LOGIN_USER = 12;
    public final int MSG_SAVE_SUCCESS = 13;
    public final int MSG_SAVE_FAILED = 14;
    public final int MSG_SAVE_SUCCESS_LOCAL = 15;
    public final int MSG_SAVE_FAILED_LOCAL = 16;
    public final int MSG_NO_USER = 17;
    public final int MSG_LOGIN_SUCCESS_OFFLINE = 18;
    public final int MSG_SYNCING_WAIT = 19;
    public final int MSG_NO_USER_FIREBASE = 20;
    public final int MSG_ACCOUNT_ERROR = 21;
    public final int MSG_APP_LOCKED = 22;
    public final int ERROR_DELETE_DATA = 23;
    public final int MSG_DATA_DELETED = 24;
    public final int MSG_CONFIRM = 25;
    public final int MSG_MAX_DATA = 26;
    public final int MSG_ERROR_PLOT_COUNT = 27;
    public final int MSG_EMPTY_DATA_2014 = 28;
    public final int MSG_SAVING_DATA = 29;
    public final int MSG_UPLOAD_NONE = 30;
    public final int MSG_PAGE_LOADS = 31;


    /**
     * Display a Toast String message
     * @param message   string message to display in the Toast
     */
    public void displayMessage(String message){
        Toast.makeText(ApplicationContextProvider.getContext(),
                message, Toast.LENGTH_SHORT).show();
    }


    /**
     * Get the matching message to display for users, given a defined ERROR_CODE
     * @param MSG_CODE    Defined codes for specific errors
     * @return
     */
    public String getMessage(int MSG_CODE){
        String errorMessage = "Something went wrong.";

        switch(MSG_CODE){
            case ERROR_NETWORK:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.err_msg_network); break;
            case ERROR_PASSWORD_MISMATCH:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.err_msg_password_mismatch); break;
            case MSG_SIGNING_UP:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.msg_signing_up); break;
            case MSG_REG_SUCCESS:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.msg_registration_success); break;
            case MSG_REG_USEREXISTS:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.err_msg_fb_userexists); break;
            case MSG_LOGIN_ERROR:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.err_msg_fb_login); break;
            case MSG_LOGIN_SUCCESS:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.msg_login_success); break;
            case MSG_WAIT_LOADING:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.msg_please_wait); break;
            case MSG_CHECK_INPUT:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.err_msg_check_input); break;
            case MSG_REG_PENDING:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.err_msg_fb_user_validation); break;
            case MSG_LOGIN_PASS:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.err_msg_fb_login_pass); break;
            case MSG_LOGIN_USER:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.err_msg_fb_login_username); break;
            case MSG_SAVE_SUCCESS:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.msg_save_success); break;
            case MSG_SAVE_FAILED:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.msg_save_failed); break;
            case MSG_SAVE_SUCCESS_LOCAL:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.msg_save_success_local); break;
            case MSG_SAVE_FAILED_LOCAL:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.msg_save_failed_local); break;
            case MSG_NO_USER:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.msg_no_user_loggedin); break;
            case MSG_LOGIN_SUCCESS_OFFLINE:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.msg_login_success_offline); break;
            case MSG_SYNCING_WAIT:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.msg_syncing); break;
            case MSG_NO_USER_FIREBASE:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.msg_relogin_offline); break;
            case MSG_ACCOUNT_ERROR:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.msg_relogin_user_accnt); break;
            case MSG_APP_LOCKED:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.msg_app_locked); break;
            case ERROR_DELETE_DATA:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.err_msg_delete_data); break;
            case MSG_DATA_DELETED:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.msg_data_deleted); break;
            case MSG_CONFIRM:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.msg_confirm); break;
            case MSG_PAGE_LOADS:
                errorMessage = ApplicationContextProvider.getContext().
                        getResources().getString(R.string.msg_page_loads); break;
            default: break;
        }

        return errorMessage;
    }


    /**
     * Get the matching custom error message to display for users, given matching keywords
     * from a complete Java Exception error
     * @param error     Complete Java Exception string
     */
    public String getMessage(String error){
        String errorMessage = "Something went wrong.";

        if(error.contains("network error")){
            errorMessage = ApplicationContextProvider.getContext().getResources().getString(R.string.err_msg_network);
        }
        else if(error.contains("operation is not allowed")){
            errorMessage = ApplicationContextProvider.getContext().getResources().getString(R.string.err_msg_fb_console);
        }
        else if(error.contains("already in use")){
            errorMessage = ApplicationContextProvider.getContext().getResources().getString(R.string.err_msg_fb_userexists);
        }
        else if(error.contains("password is invalid")){
            errorMessage = ApplicationContextProvider.getContext().getResources().getString(R.string.err_msg_fb_login_pass);
        }
        else if(error.contains("no user record")){
            errorMessage = ApplicationContextProvider.getContext().getResources().getString(R.string.err_msg_fb_login_username);
        }

        return errorMessage;
    }

    /**
     * Get the description message part of a Java Exception error
     * from a complete Java Exception error after the ":" character.
     * @param error     Complete Java Exception string
     */
    public String getMessageDebug(String error){
        String errorMessage = "Something went wrong.";

        if(error.indexOf(":") >= 0){
            errorMessage = "Error: " + error.substring(error.indexOf(":") + 1, error.length());
        }

        return errorMessage;
    }
}
