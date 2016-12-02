package com.teramatrix.demo.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ListView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.teramatrix.demo.Flint;
import com.teramatrix.demo.Interface.IRefreshFragment;
import com.teramatrix.demo.MainActivity;
import com.teramatrix.demo.R;
import com.teramatrix.demo.Utils.GeneralUtilities;
import com.teramatrix.demo.Utils.Util;
import com.teramatrix.demo.adapter.ComponentAttributeListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by arun.singh on 6/6/2016.
 */
public class EnginePerformanceFragment extends Fragment implements IRefreshFragment {


    private View convertView;
    private DonutProgress donutProgressengine_load_indicator;
    private DonutProgress donutProgressengine_rpm_indicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_engine_performance, null, false);

        }

        return convertView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initViews();
        ((MainActivity) getActivity()).setOpenedFragment(this);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        try {

            menu.findItem(R.id.action_refresh).setVisible(true);
            menu.findItem(R.id.action_graph).setVisible(false);
            super.onPrepareOptionsMenu(menu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh: {

            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {

        try {

            GeneralUtilities.setUpActionBar(getActivity(), this, "Engine Performance", null);

            //Setup Engine Load Progress indicator

            float engine_load_current_value = 27;

            donutProgressengine_load_indicator = (DonutProgress) convertView.findViewById(R.id.engine_load_indicator);
            donutProgressengine_load_indicator.setMax(100);
            donutProgressengine_load_indicator.setProgress(0);
            donutProgressengine_load_indicator.setTextSize(65);
            donutProgressengine_load_indicator.setTextColor(Color.WHITE);
            donutProgressengine_load_indicator.setUnfinishedStrokeColor(getActivity().getResources().getColor(R.color.blueGray_dark));

            Util.animateDonut(getActivity(), donutProgressengine_load_indicator, (int) Math.ceil(engine_load_current_value));


            /*if ((int) Math.ceil(engine_load_current_value) == 0) {
                //default state
                donutProgressengine_load_indicator.setTextColor(Color.WHITE);

            } else if ((int) Math.ceil(engine_load_current_value) < 75) {
                //Its Ok
                donutProgressengine_load_indicator.setFinishedStrokeColor(getActivity().getResources().getColor(R.color.donut_default_color));
                donutProgressengine_load_indicator.setTextColor(getActivity().getResources().getColor(R.color.donut_default_color));
            } else {
                //alert situation
                donutProgressengine_load_indicator.setFinishedStrokeColor(getActivity().getResources().getColor(R.color.rpm_meter_color_outside));
                donutProgressengine_load_indicator.setTextColor(getActivity().getResources().getColor(R.color.rpm_meter_color_outside));
                Animation anim = new AlphaAnimation(0.4f, 1.0f);
                anim.setDuration(350); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
//                donutProgressengine_load_indicator.startAnimation(anim);
            }*/


            //Setup Engine Rpm Progress indicator

            String rpm_max_value = "12000";
            int rpm_current_value = 6500;

            donutProgressengine_rpm_indicator = (DonutProgress) convertView.findViewById(R.id.egine_rpm);
            donutProgressengine_rpm_indicator.setMax(Integer.parseInt(rpm_max_value));
            donutProgressengine_rpm_indicator.setProgress(0);
            donutProgressengine_rpm_indicator.setTextSize(65);
            donutProgressengine_rpm_indicator.setTextColor(Color.WHITE);
            donutProgressengine_rpm_indicator.setSuffixText("");
            donutProgressengine_rpm_indicator.setUnfinishedStrokeColor(getActivity().getResources().getColor(R.color.blueGray_dark));

            Util.animateDonut(getActivity(), donutProgressengine_rpm_indicator, rpm_current_value);


            initAttributeList();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initAttributeList() {
        try {
            ListView listView = (ListView) convertView.findViewById(R.id.listView);

            ArrayList<HashMap<String, String>> hashMaps = new ArrayList<HashMap<String, String>>();

            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put(HomeFragment.PARAM_ALIAS, "Harsh Brake No");
            hashMap.put(HomeFragment.PARAM_VALUE, "7");
            hashMap.put(HomeFragment.PARAM_UNIT, "times");
            hashMaps.add(hashMap);

            HashMap<String, String> hashMap1 = new HashMap<String, String>();
            hashMap1.put(HomeFragment.PARAM_ALIAS, "Harsh Acceleration No");
            hashMap1.put(HomeFragment.PARAM_VALUE, "3");
            hashMap1.put(HomeFragment.PARAM_UNIT, "times");
            hashMaps.add(hashMap1);

            HashMap<String, String> hashMap2 = new HashMap<String, String>();
            hashMap2.put(HomeFragment.PARAM_ALIAS, "Throttle Opening Width");
            hashMap2.put(HomeFragment.PARAM_VALUE, "9.41");
            hashMap2.put(HomeFragment.PARAM_UNIT, "TPS%");
            hashMaps.add(hashMap2);

            HashMap<String, String> hashMap3 = new HashMap<String, String>();
            hashMap3.put(HomeFragment.PARAM_ALIAS, "Current Error Code Numbers");
            hashMap3.put(HomeFragment.PARAM_VALUE, "4");
            hashMap3.put(HomeFragment.PARAM_UNIT, "times");
            hashMaps.add(hashMap3);

            HashMap<String, String> hashMap4 = new HashMap<String, String>();
            hashMap4.put(HomeFragment.PARAM_ALIAS, "Coolant Temperature");
            hashMap4.put(HomeFragment.PARAM_VALUE, "92");
            hashMap4.put(HomeFragment.PARAM_UNIT, "Deg. C");
            hashMaps.add(hashMap4);


            listView.setAdapter(new ComponentAttributeListAdapter(getActivity(), hashMaps));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refreshFragment() {
        initViews();
    }
}
