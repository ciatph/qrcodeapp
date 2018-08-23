package com.leo.qrcodeapp.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leo.qrcodeapp.R;
import com.leo.qrcodeapp.events.EventStatus;
import com.leo.qrcodeapp.models.Event;
import com.leo.qrcodeapp.utils.listhandler.InputRecyclerAdapter;
import com.leo.qrcodeapp.utils.listhandler.KeyValuePair;
import com.leo.qrcodeapp.utils.listhandler.RecyclerListManager;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEventFragment extends Fragment {

    public RecyclerListManager mData;
    public String TAG = "--add-event";

    public AddEventFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);
        mData = new RecyclerListManager(view, getActivity());

        return view;
    }


    @Override
    public void onStart(){
        super.onStart();

        if(mData.mAdapter == null){
            int adapterViewType = (EventStatus.INSTANCE.isAction(EventStatus.INSTANCE.ACTION_EDIT)) ?
                    InputRecyclerAdapter.TYPE_EDIT : InputRecyclerAdapter.TYPE_EDIT;

            mData.setAdapter(getViewData(EventStatus.INSTANCE.getAction()), adapterViewType);
        }
    }


    @Override
    public void onResume(){
        super.onResume();

        if(EventStatus.INSTANCE.isAction(EventStatus.INSTANCE.ACTION_ADD)){
            ((MainQRActivity) getActivity()).setActionBarTitleFragment(R.string.top_title_add);
        }
    }


    public HashMap<Integer, KeyValuePair> getViewData(int processMode){
        HashMap<Integer, KeyValuePair> map = new HashMap<>();

        if(EventStatus.INSTANCE.isAction(EventStatus.INSTANCE.ACTION_ADD)){
            Event event = new Event(getResources().getStringArray(R.array.model_event));
            String[] labels = event.getLabelContent(true);
            for(int i=0; i<labels.length; i++){
                Log.d(TAG, "--" + labels[i]);
            }
            map = mData.uiDataBuilder(event.getLabelContent(true),
                    event.getLabelContent(false), new String[event.getLabelList().length], null);
        }

        return map;
    }
}
