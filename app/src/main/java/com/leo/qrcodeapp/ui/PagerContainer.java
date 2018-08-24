package com.leo.qrcodeapp.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leo.qrcodeapp.R;
import com.leo.qrcodeapp.db.DatabaseHelper;
import com.leo.qrcodeapp.events.EventStatus;
import com.leo.qrcodeapp.ui.adapters.DataPagerAdapter;
import com.leo.qrcodeapp.utils.AppUtilities;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class PagerContainer extends Fragment {

    TabLayout tabLayout = null; // final
    ViewPager viewPager;    // final
    DataPagerAdapter pageAdapter;
    ViewPager.OnPageChangeListener pageChangeListener;
    TabLayout.OnTabSelectedListener tabLayoutOnPageChangeListener;
    DatabaseHelper dbConnector;

    public PagerContainer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pager_container, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();

        // update tabs
        tabLayout = getActivity().findViewById(R.id.tab_layout);

        if(tabLayout.getTabCount() == 0) {
            String[] tabArray = getResources().getStringArray(R.array.tab_pages);
            for (int i = 0; i < tabArray.length; i++) {
                tabLayout.addTab(tabLayout.newTab().setText(tabArray[i]));
            }

            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayoutOnPageChangeListener = new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            };
            tabLayout.addOnTabSelectedListener(tabLayoutOnPageChangeListener);
        }

        // update viewpager
        // initialize the view pager, subpages for the tabs
        viewPager = getActivity().findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(2);
        pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                AppUtilities.INSTANCE.hideSoftKeyboard(getActivity());
            }

            @Override
            public void onPageSelected(int position) {
                //TO-DO: check position coincides with SCR
                Log.d(TAG, "position: " + position + "");
                EventStatus.INSTANCE.setScreen(position);
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        // set the pager adapter
        pageAdapter = new DataPagerAdapter(getChildFragmentManager(),
            tabLayout.getTabCount(), EventStatus.INSTANCE.getAction());
        viewPager.setAdapter(pageAdapter);

        // add listeners
        viewPager.addOnPageChangeListener(pageChangeListener);
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();

        if(viewPager != null)
            viewPager.removeOnPageChangeListener(pageChangeListener);

        if(tabLayout != null)
            tabLayout.removeOnTabSelectedListener(tabLayoutOnPageChangeListener);

        pageChangeListener = null;
        tabLayoutOnPageChangeListener = null;
    }
}
