package com.leo.qrcodeapp;

import android.Manifest;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;

import com.leo.qrcodeapp.db.DatabaseHelper;
import com.leo.qrcodeapp.models.Account;
import com.leo.qrcodeapp.ui.LoginFragment;
import com.leo.qrcodeapp.ui.RegistrationFragment;
import com.leo.qrcodeapp.utils.CommonFlags;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

public class MainActivity extends AppCompatActivity {
    private String LOG = "---UI-MAIN";
    private DatabaseHelper dbConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // default: login fragment
        switchFragments(0);
    }


    @Override
    public void onStart(){
        super.onStart();
        dbConnector = DatabaseHelper.getsInstance(this);
    }

    /**
     * Toggles the login and registration fragments
     * @param fragType
     */
    public void switchFragments(int fragType){
        if(fragType == 1){  // registration
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.activity_main, (new RegistrationFragment())).commit();
        }
        else if(fragType == 0){ // login
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.activity_main, (new LoginFragment())).commit();
        }
    }

    public boolean userExists(String user, String pass, boolean log){
        String query = String.format("SELECT * FROM %s WHERE %s = '%s'",
                Account.tablename, Account.username, user);
        Log.d("query", query);

        Cursor c = dbConnector.customQuery(query);
        if(c.moveToFirst()){
            if(log){
                CommonFlags.INSTANCE.CURRENT_USER = new Account();
                CommonFlags.INSTANCE.CURRENT_USER.set(Account.username, c.getString(c.getColumnIndex(Account.username)));
                CommonFlags.INSTANCE.CURRENT_USER.set(Account.password, c.getString(c.getColumnIndex(Account.password)));
                CommonFlags.INSTANCE.CURRENT_USER.setId(c.getString(c.getColumnIndex("_id")));
            }
            //dbPass = c.getString(c.getColumnIndex(Account.COL_PASS));
            //Log.d("PASS", dbPass);
            c.close();
            return true;
        }
        else{
            return false;
        }
    }
}
