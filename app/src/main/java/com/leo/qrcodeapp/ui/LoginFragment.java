package com.leo.qrcodeapp.ui;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.leo.qrcodeapp.db.DatabaseContract;
import com.leo.qrcodeapp.db.DatabaseHelper;
import com.leo.qrcodeapp.utils.MsgNotification;

/**
 * A simple {@link Fragment} subclass.
 * Handles user Login
 * Created by mbarua on 9/27/2017.
 */
public class LoginFragment extends Fragment {

    EditText txtEmail;
    EditText txtPass;
    Button btnLogin;

    private ProgressDialog mProgress;
    private DatabaseHelper dbConnector;
    private String TAG = "--ACCT";

    private SharedPreferences sharedPref;


    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initFirebase();
        intializeElements(view);

        // check if import data from sharedPreferences exists
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        return view;
    }


    /**
     * Initialize context and references
     */
    public void onStart(){
        super.onStart();
        dbConnector = DatabaseHelper.getsInstance(getActivity());
    }


    /**
     * Disable all user input and display the loading progress spinner
     */
    public void autoLoginInit(){
        txtEmail.setEnabled(false);
        txtPass.setEnabled(false);
        btnLogin.setEnabled(false);
        mProgress.setMessage(MsgNotification.INSTANCE.getMessage(MsgNotification.INSTANCE.MSG_WAIT_LOADING));
        mProgress.show();
    }


    /**
     * Initialize constant reference tables with default values.
     * These tables must be in sync with firebase online
     */
    public void initConstants(){
        // default test account
        if(!dbConnector.recordExists(DatabaseContract.AccountTable.TABLE_NAME,
                DatabaseContract.AccountTable.COL_FB_ID, "qwertyuiop"))
            (new TestScript()).createUser(dbConnector);

        // Add test user to device sharedPrefs. This user cannot save online data
        logUserDevice("ciat@gmail.com", "ciat", "ciatph");
    }

    /**
     * Initialize firebase database and authentication
     */
    private void initFirebase(){
        Log.d(TAG, "--initialized firebase!");
        // TODO: 10/13/2017 get specific user records to avoid massive data downloads
        database = FirebaseDatabase.getInstance();
        mReference = database.getReference(DatabaseFirebaseHelper.INSTANCE.FB_USER_ACCOUNT);
        mAuth = FirebaseAuth.getInstance();
    }


    /**
     * Initializes click handlers and listeners for this layout
     * @param view fragment xml view from inflater
     */
    private void intializeElements(View view){
        // input fields
        txtEmail = view.findViewById(R.id.txt_email);
        txtPass = view.findViewById(R.id.txt_password);
        btnLogin = view.findViewById(R.id.btn_login);
        mProgress = new ProgressDialog(getActivity());
        mProgress.setCancelable(false);

        // button
        view.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
                AppUtilities.INSTANCE.hideSoftKeyboard(getActivity());
            }
        });

        // registration
        view.findViewById(R.id.label_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).switchFragments(1);
            }
        });
    }



    /**
     * Login-authentication using firebase email-password authentication (if online)
     * Insert account details to local sqlite if online account does not yet exist
     */
    private void login() {
        final String email = txtEmail.getText().toString().trim();
        final String password = txtPass.getText().toString().trim();
        int msgCode = MsgNotification.INSTANCE.MSG_CHECK_INPUT;

        // check if a user is logged in from firebase even if offline: proceed to login
        // no need to verify email-password auth online
        // user data is automatically assumed to be:
        //      - admin-approved (acct_status=1)
        //      - and record exists in local sqlite3 database
        if(mAuth.getCurrentUser() != null){
        //if(CommonFlags.INSTANCE.USER_SIGNED_IN){
            Log.d(TAG, "-------A user is TAGged-in! Proceed to main page");

            final String user_id = mAuth.getCurrentUser().getUid();
            isValidUser(mAuth.getCurrentUser().getEmail());
            Log.d(TAG, "-------user is " + user_id);

            autoLoginInit();
            proceedMainPage(1000);
        }
        else{
            // Detect network connection. Proceed to login if user is valid (has firebase auth & admin-validated)
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                if(!AppUtilities.INSTANCE.hasNetworkConnection()){
                    if(isValidUser(email, password)){
                        MsgNotification.INSTANCE.displayMessage(
                                MsgNotification.INSTANCE.getMessage(MsgNotification.INSTANCE.MSG_LOGIN_SUCCESS_OFFLINE));

                        // proceed to login
                        Log.d(TAG, "User exists in sharedPrefs");
                        autoLoginInit();
                        proceedMainPage(1000);
                    }
                    else{
                        Log.d(TAG, "User do not exist in sharedPrefs");
                        MsgNotification.INSTANCE.displayMessage(
                                MsgNotification.INSTANCE.getMessage(MsgNotification.INSTANCE.MSG_CHECK_INPUT));
                    }
                }
                else{
                    // initialize online login, add user to device sharedPrefs for first time
                    mProgress.setMessage(MsgNotification.INSTANCE.getMessage(MsgNotification.INSTANCE.MSG_WAIT_LOADING));
                    mProgress.show();

                    // initiate sign-in using firebase Email-Password authentication
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final String user_id = mAuth.getCurrentUser().getUid();

                                //// TODO: 10/18/2017 Add Intent background listeners here to minimize codes
                                mReference.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (DatabaseFirebaseHelper.INSTANCE.getValue(DatabaseContract.AccountTable.COL_ACCT_STATUS, dataSnapshot).equals("1")) {

                                            // check if logged-in user exists in local database. insert if not
                                            if (!dbConnector.recordExists(DatabaseContract.AccountTable.TABLE_NAME,
                                                    DatabaseContract.AccountTable.COL_FB_ID, user_id)) {

                                                Log.d(TAG, "INSERT Account to local sqlite!");
                                                Account account = new Account();
                                                account.mappedContents.put(DatabaseContract.AccountTable.COL_FB_ID, user_id);
                                                account.mappedContents.put(DatabaseContract.AccountTable.COL_FNAME, mAuth.getCurrentUser().getDisplayName());
                                                account.mappedContents.put(DatabaseContract.AccountTable.COL_EMAIL, mAuth.getCurrentUser().getEmail());

                                                dbConnector.insertDB(DatabaseContract.AccountTable.TABLE_NAME,
                                                        account.getColumnFields(), account.getValuesFields());

                                            } else {
                                                Log.d(TAG, "User EXISTS in local sqlite! " + email);
                                            }

                                            // add user to device  sharedPrefs
                                            logUserDevice(email, password, user_id);

                                            mProgress.dismiss();
                                            MsgNotification.INSTANCE.displayMessage(
                                                    MsgNotification.INSTANCE.getMessage(MsgNotification.INSTANCE.MSG_LOGIN_SUCCESS));


                                            // revert to main page after successful login
                                            txtPass.setEnabled(false);
                                            txtEmail.setEnabled(false);
                                            btnLogin.setEnabled(false);
                                            proceedMainPage(0);
                                        }
                                        else {
                                            // log-out default signed-in user if account is not yet admin-authenticated
                                            // // TODO: 10/13/2017 research on using Custom Authentication for firebase
                                            mAuth.signOut();
                                            mProgress.dismiss();

                                            Log.d(TAG, "User automatically signed out!");
                                            MsgNotification.INSTANCE.displayMessage(
                                                    MsgNotification.INSTANCE.getMessage(MsgNotification.INSTANCE.MSG_REG_PENDING));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                            else {
                                // display login notification message
                                Log.d("---", "error: " + task.getException().toString());
                                mProgress.dismiss();
                                MsgNotification.INSTANCE.displayMessage(
                                        MsgNotification.INSTANCE.getMessage(task.getException().toString()));
                            }
                        }
                    });
                }
            }
            else{
                MsgNotification.INSTANCE.displayMessage(
                        MsgNotification.INSTANCE.getMessage(msgCode));
            }
        }
    }


    /**
     * Switch to main Farmers List Activity
     */
    public void proceedMainPage(int timeoutMilli){
        Log.d(TAG, "--proceeding login TIMEOUT: " + timeoutMilli + "");

        if(timeoutMilli > 0) {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            try {
                                mProgress.dismiss();
                            } catch (Exception e) { }
                            startActivity(new Intent(getActivity(), AppDataCollectMain.class));
                        }
                    }, timeoutMilli);
        }
        else{
            startActivity(new Intent(getActivity(), AppDataCollectMain.class));
        }
    }


    /**
     * Permanently set a user's firebase email (usrname), uid and password into the device
     * Used for offline login credentials checking
     * @param email
     */
    public void logUserDevice(String email, String password, String userId){
        if(!sharedPref.contains(email)){
            Log.d(TAG, "Logging user to device, " + email);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(email, password);
            editor.putString("_userid_" + email, userId);
            editor.commit();
        }
        else{
            Log.d(TAG, "user already in device: " + email + ", " + password + ", " + userId);
        }
        CommonFlags.INSTANCE.USERID = userId;
    }


    /**
     * Update global USERID with a valid firebase uid
     * @param email     User's email address
     * @param userId    User's firebase uid
     */
    public void updateUserDevice(String email, String userId){
        if(sharedPref.contains("_userid_" + userId)){
            Log.d(TAG, "Updating user to device, " + email + ", uid: " + userId);
            CommonFlags.INSTANCE.USERID = sharedPref.getString("_userid_" + email, null);
        }
        else{
            Log.d(TAG, "Can't update device, user " + email + " does not exist");
        }
    }


    /**
     * Check if login is a valid user, if user credentials have previously been stored in sharedPreferences.
     * User has logged in at least once in the app while online (thus verified with firebase & auth)
     * Used for offline login credentials checking
     * @param username
     * @return
     */
    public boolean isValidUser(String username, String password){
        // // TODO: 1/14/2018 Security!!!
        // Retrieve the user's password from device sharedPrefs
        String sharedPass = sharedPref.getString(username, null);

        // Retrieve the user's firebase UID from device sharedPrefs
        String sharedUserID = sharedPref.getString("_userid_" + username, null);
        Log.d(TAG, "device pass: " + sharedPass + " =? " + password + " :: userid = " + sharedUserID + ", email: " + username);

        // set the global userid read from sharedPrefs
        CommonFlags.INSTANCE.USERID = sharedUserID;
        return (sharedPref.contains(username) && password.equals(sharedPass)) ? true : false;
    }

    /**
     * Check if login is a valid user, using only email if user credentials have previously been stored in sharedPreferences.
     * User has logged in at least once in the app while online (thus verified with firebase & auth)
     * Used when user has not logged out of firebase auth
     * Used for offline login credentials checking
     * @param username
     * @return
     */
    public boolean isValidUser(String username){
        // // TODO: 1/14/2018 Security!!!
        // Retrieve the user's firebase UID from device sharedPrefs
        String sharedUserID = sharedPref.getString("_userid_" + username, null);

        // set the global userid read from sharedPrefs
        CommonFlags.INSTANCE.USERID = sharedUserID;
        Log.d(TAG, "success checking email " + username + ", userid = " + sharedUserID);
        return (sharedPref.contains(username)) ? true : false;
    }
}
