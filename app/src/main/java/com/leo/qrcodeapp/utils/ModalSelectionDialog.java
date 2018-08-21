package com.leo.qrcodeapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.leo.qrcodeapp.R;


/**
 * Display a modal selection of selectable item lists.
 * Returns the selected item using an interface
 * Created by mbarua on 2/21/2018.
 */

public class ModalSelectionDialog {
    private static ModalSelectionDialog sInstance;
    ModalSelectionListener mCallback;
    Activity activity;
    String TAG = "--Modal";

    /** Index of selected item from list */
    private int selectdListId = 0;

    /** Unique integer to refer callback action for onModalSelectionItemClicked */
    private int processId = -1;

    /** Indicated rowID of calling RecyclerList item, if any. Defaults to -1 */
    private int recyclerRowId = -1;


    /**
     * Interface for retrieveing selected item from this modal selection's list
     */
    public interface ModalSelectionListener{
        public void onModalSelectionItemClicked(int itemId, int processId, int recyclerRowId);
    }


    public static synchronized ModalSelectionDialog getsInstance(Activity activity){
        if(sInstance == null){
            sInstance = new ModalSelectionDialog(activity);
        }
        return sInstance;
    }

    public ModalSelectionDialog(Activity activity){
        this.activity = activity;
    }


    public void ShowModalSelectionDialog(/*Activity activity, */String title, String[] list, int processCode, ModalSelectionListener callback){
        mCallback = callback;
        processId = processCode;

        // always reset index to 0
        selectdListId = 0;

        // Check if Activity is valid and not yet destroyed
        if(!((activity).isFinishing())) {
            LayoutInflater inflater = activity.getLayoutInflater();
            View titleView = inflater.inflate(R.layout.alert_title, null);
            ((TextView) titleView.findViewById(R.id.label_title)).setText(title);

            final AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                    .setCustomTitle(titleView)
                    .setSingleChoiceItems(list, selectdListId, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "--selected " + which + "");
                            selectFromList(which);
                        }
                    })
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "--selectedLisId: " + selectdListId + "");
                            mCallback.onModalSelectionItemClicked(selectdListId, processId, -1);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.create().show();
        }
        else{
            Log.d(TAG, "Error in activity, cannot show modal dialog.");
        }
    }


    /**
     * Encode the selected multiple choice index from an AlertDialog
     * @param id
     */
    public void selectFromList(int id){
        selectdListId = id;
    }
}
