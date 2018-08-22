package com.leo.qrcodeapp.ui;


import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.leo.qrcodeapp.MainActivity;
import com.leo.qrcodeapp.R;
import com.leo.qrcodeapp.db.DatabaseContract;
import com.leo.qrcodeapp.db.DatabaseHelper;
import com.leo.qrcodeapp.models.Account;
import com.leo.qrcodeapp.utils.AppUtilities;
import com.leo.qrcodeapp.utils.CommonFlags;
import com.leo.qrcodeapp.utils.MsgNotification;


import java.util.HashMap;




/**
 * A simple {@link Fragment} subclass.
 * Handles user registration
 * Created by mbarua on 9/27/2017.
 */
public class RegistrationFragment extends Fragment {
    private EditText txtUsername;
    private EditText txtPass;
    private EditText txtPassConfirm;
    private EditText txtFirstname;
    private EditText txtLastname;

    private ProgressDialog mProgress;

    // firebase sign-up variables
    DatabaseHelper dbConnector;

    public RegistrationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        intializeElements(view);
        return view;
    }


    @Override
    public void onStart(){
        super.onStart();
        dbConnector = DatabaseHelper.getsInstance(getActivity());
    }

    /**
     * Initializes click handlers and listeners for this layout
     * @param view fragment xml view from inflater
     */
    private void intializeElements(View view){
        // input fields
        txtUsername = view.findViewById(R.id.txt_email);
        txtPass = view.findViewById(R.id.txt_password);
        txtFirstname = view.findViewById(R.id.txt_fname);
        txtLastname = view.findViewById(R.id.txt_lastname);

        // add clear text field on click listeners
        txtUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtUsername.setText("");
            }
        });

        // add clear text field on click listeners
        txtPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPass.setText("");
            }
        });

        // add clear text field on click listeners
        txtFirstname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtFirstname.setText("");
            }
        });

        // add clear text field on click listeners
        txtLastname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtLastname.setText("");
            }
        });

        // spinner dialog
        mProgress = new ProgressDialog(getActivity());

        // register button event
        view.findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // // TODO: 9/27/2017 add firebase and local sqlite user registration
                registerAccount();
                AppUtilities.INSTANCE.hideSoftKeyboard(getActivity());
            }
        });

        // login link
        view.findViewById(R.id.label_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).switchFragments(0);
            }
        });
    }


    /**
     * Register account online in firebase database.
     * password, passConfirmation, email, firstname, lastname
     */
    public void registerAccount(){
        Log.d("--GMS", GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE);

        final String username = txtUsername.getText().toString().trim();
        final String pass = txtPass.getText().toString().trim();
        final String fname = txtFirstname.getText().toString().trim();
        final String lname = txtLastname.getText().toString().trim();

        // check if all input fields are not empty
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pass) &&
                !TextUtils.isEmpty(fname) && !TextUtils.isEmpty(lname)) {

            if(!((MainActivity) getContext()).userExists(username, pass, false)){
                showSpinner(true, MsgNotification.INSTANCE.getMessage(MsgNotification.INSTANCE.MSG_SIGNING_UP));
                Account account = new Account();

                // local sqlite3 regisration
                account.set(Account.fname, fname);
                account.set(Account.lname, lname);
                account.set(Account.username, username);
                account.set(Account.password, pass);

                dbConnector.insertDB(Account.tablename,
                        account.getColumnFields(), account.getValuesFields());

                MsgNotification.INSTANCE.displayMessage(
                        MsgNotification.INSTANCE.getMessage(MsgNotification.INSTANCE.MSG_REG_SUCCESS));

                showSpinner(false, "");
                AppUtilities.INSTANCE.hideSoftKeyboard(((MainActivity) getContext()));
            }
            else{
                MsgNotification.INSTANCE.displayMessage(
                        MsgNotification.INSTANCE.getMessage(MsgNotification.INSTANCE.MSG_REG_USEREXISTS));
            }
        }
    }


    /**
     * Display or dismiss the loading message
     * @param show
     * @param msg
     */
    public void showSpinner(boolean show, String msg){
        if(mProgress == null)
            return;

        if(show){
            mProgress.setMessage(MsgNotification.INSTANCE.getMessage(msg));
            mProgress.show();
        }
        else{
            mProgress.dismiss();
        }
    }
}
