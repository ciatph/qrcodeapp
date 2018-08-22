package com.leo.qrcodeapp.ui;


import android.app.ProgressDialog;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import ciat.ph.appdatacollect.db.DatabaseFirebaseHelper;
import ciat.ph.appdatacollect.utils.Account;
import ciat.ph.appdatacollect.R;
import ciat.ph.appdatacollect.db.DatabaseContract;
import ciat.ph.appdatacollect.db.DatabaseHelper;
import ciat.ph.appdatacollect.utils.AppUtilities;
import ciat.ph.appdatacollect.utils.MsgNotification;


/**
 * A simple {@link Fragment} subclass.
 * Handles user registration
 * Created by mbarua on 9/27/2017.
 */
public class RegistrationFragment extends Fragment {
    private EditText txtEmail;
    private EditText txtPass;
    private EditText txtPassConfirm;
    private EditText txtFirstname;
    private EditText txtLastname;

    private ProgressDialog mProgress;

    // firebase sign-up variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mReference;
    DatabaseHelper dbConnector;

    public RegistrationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        initFirebase();
        intializeElements(view);
        return view;
    }


    @Override
    public void onStart(){
        super.onStart();
        dbConnector = DatabaseHelper.getsInstance(getActivity());
    }


    /**
     * Initialize firebase database and authentication
     */
    private void initFirebase(){
        database  = FirebaseDatabase.getInstance();
        mReference = database.getReference(DatabaseFirebaseHelper.INSTANCE.FB_GUEST_ACCOUNT_TEMP);
        mAuth = FirebaseAuth.getInstance();
        //mAuth.signOut();

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mAuth.getCurrentUser() != null) {
                    Log.d("Registration::", "user " + mAuth.getCurrentUser().getUid() + " login stat has changed ");
                }
            }
        });
    }


    /**
     * Initializes click handlers and listeners for this layout
     * @param view fragment xml view from inflater
     */
    private void intializeElements(View view){
        // input fields
        txtEmail = view.findViewById(R.id.txt_email);
        txtPass = view.findViewById(R.id.txt_password);
        txtPassConfirm = view.findViewById(R.id.txt_confirmpass);
        txtFirstname = view.findViewById(R.id.txt_fname);
        txtLastname = view.findViewById(R.id.txt_lastname);

        // add clear text field on click listeners
        txtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtEmail.setText("");
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
        txtPassConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPassConfirm.setText("");
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
                mAuth.signOut();
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

        // TODO: Add online firebase super-admin pending accounts registration validation web interface
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        final String email = txtEmail.getText().toString().trim();
        final String pass = txtPass.getText().toString().trim();
        final String cpass = txtPassConfirm.getText().toString().trim();
        final String fname = txtFirstname.getText().toString().trim();
        final String lname = txtLastname.getText().toString().trim();

        // detect network connection
        if(!AppUtilities.INSTANCE.hasNetworkConnection()){
            MsgNotification.INSTANCE.displayMessage(
                    MsgNotification.INSTANCE.getMessage(MsgNotification.INSTANCE.ERROR_NETWORK));
        }
        else {
            // check if all input fields are not empty
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(cpass) &&
                    !TextUtils.isEmpty(fname) && !TextUtils.isEmpty(lname)) {

                // check for matching passwords
                if (cpass.equals(pass) && email.matches(emailPattern)) {
                    mProgress.setMessage(MsgNotification.INSTANCE.getMessage(MsgNotification.INSTANCE.MSG_SIGNING_UP));
                    mProgress.show();
                    //mAuth.signOut();


                    // // TODO: 10/12/2017 check inter-app security for google-services.json
                    // online firebase registration
                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // sign-in the newly-created user again to avoid firebase denied writes
                                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            // // TODO: 10/19/2017 Use a more automated common class approach to user account
                                            // register to online firebase
                                            final String user_id = mAuth.getCurrentUser().getUid();
                                            DatabaseReference mCurrentUser = mReference.child(user_id);
                                            mReference.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    Log.d("--registration", "NEW USER created! " + user_id);
                                                    //mAuth.signOut();
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    Log.d("--registration", "USER cancelled. " + user_id);
                                                    //mAuth.signOut();
                                                }
                                            });
                                            Account account = new Account();

                                            // firebase account status
                                            HashMap<String, String> map = new HashMap<>();
                                            map.put(DatabaseContract.AccountTable.COL_FNAME, fname);
                                            map.put(DatabaseContract.AccountTable.COL_LASTNAME, lname);
                                            map.put(DatabaseContract.AccountTable.COL_EMAIL, email);
                                            map.put(DatabaseContract.AccountTable.COL_DATE_CREATE, account.get(DatabaseContract.AccountTable.COL_DATE_CREATE));
                                            //map.put(DatabaseContract.AccountTable.COL_ACCT_TYPE, account.mappedContents.get(DatabaseContract.AccountTable.COL_ACCT_TYPE));
                                            //map.put(DatabaseContract.AccountTable.COL_ACCT_STATUS, account.mappedContents.get(DatabaseContract.AccountTable.COL_ACCT_STATUS));
                                            //map.put(DatabaseContract.AccountTable.COL_LOGIN_STATUS, account.get(DatabaseContract.AccountTable.COL_LOGIN_STATUS));
                                            //map.put(DatabaseContract.AccountTable.COL_DATE_APPR, "---");
                                            mCurrentUser.setValue(map);

                                            // local sqlite3 regisration
                                            account.mappedContents.put(DatabaseContract.AccountTable.COL_FB_ID, user_id);
                                            account.mappedContents.put(DatabaseContract.AccountTable.COL_FNAME, fname);
                                            account.mappedContents.put(DatabaseContract.AccountTable.COL_LASTNAME, lname);
                                            account.mappedContents.put(DatabaseContract.AccountTable.COL_EMAIL, email);
                                            account.mappedContents.put(DatabaseContract.AccountTable.COL_PASSWORD, pass);

                                            dbConnector.insertDB(DatabaseFirebaseHelper.INSTANCE.FB_USER_ACCOUNT,
                                                    account.getColumnFields(), account.getValuesFields());

                                            MsgNotification.INSTANCE.displayMessage(
                                                    MsgNotification.INSTANCE.getMessage(MsgNotification.INSTANCE.MSG_REG_SUCCESS) + " -- new user: " + mAuth.getCurrentUser().getUid() + "!!!");
                                        }
                                        else{
                                            // 2nd log-in is unsuccessful
                                            // Original error message from the Java Exception class
                                            Log.d("---", "Error exception = " + task.getException());

                                            MsgNotification.INSTANCE.displayMessage(
                                                    MsgNotification.INSTANCE.getMessage(task.getException().toString()));
                                        }

                                        mProgress.dismiss();
                                    }
                                });


                                // // TODO: 10/12/2017 Add admin/super-admin new accounts validation before proceeding to login
                                mProgress.dismiss();
                            } else {
                                // Original error message from the Java Exception class
                                Log.d("---", "Error exception = " + task.getException());
                                mProgress.dismiss();
                                MsgNotification.INSTANCE.displayMessage(
                                        MsgNotification.INSTANCE.getMessage(task.getException().toString()));
                            }

                            // remove temporary firebase user regisration cache
                            //mAuth.signOut();
                        }
                    });

                    //mAuth.signOut();
                } else {
                    //mAuth.signOut();

                    Toast.makeText(getActivity(),
                            MsgNotification.INSTANCE.getMessage(MsgNotification.INSTANCE.ERROR_PASSWORD_MISMATCH),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
