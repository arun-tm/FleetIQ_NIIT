package com.teramatrix.demo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
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
public class FuelAnalyticFragment extends Fragment implements IRefreshFragment {


    private View convertView;
    private DonutProgress donutProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_fuel_analytic, null, false);
            initViews();
        }

        return convertView;
    }

    @Override
    public void onResume() {
        super.onResume();
        GeneralUtilities.setUpActionBar(getActivity(), this, "Fuel Performance", null);
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

            //Setup Fuel Progress indicator

            convertView.findViewById(R.id.fuel_progress_indicator).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CarScoreFragment carScoreFragment = new CarScoreFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("screen", "Fuel Score");
                    carScoreFragment.setArguments(bundle);
                    GeneralUtilities.loadFragment((AppCompatActivity) getActivity(), carScoreFragment);
                }
            });
            donutProgress = (DonutProgress) convertView.findViewById(R.id.fuel_progress_indicator);
            /*donutProgress.setMax(Integer.parseInt(fuel_max_value));*/
            donutProgress.setMax(10);
            donutProgress.setProgress(0);
            donutProgress.setTextSize(65);
            donutProgress.setSuffixText("");
            /*if(fuel_per>=20)
            {
                donutProgress.setFinishedStrokeColor(getActivity().getResources().getColor(R.color.rpm_meter_color_outside));
                donutProgress.setTextColor(getActivity().getResources().getColor(R.color.rpm_meter_color_outside));
                Animation anim = new AlphaAnimation(0.4f, 1.0f);
                anim.setDuration(350); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
//                donutProgress.startAnimation(anim);
            }else
            {
                donutProgress.setFinishedStrokeColor(getActivity().getResources().getColor(R.color.fuel_meter_color_outside));
                donutProgress.setTextColor(getActivity().getResources().getColor(R.color.fuel_meter_color_outside));
            }*/

            donutProgress.setFinishedStrokeColor(getActivity().getResources().getColor(R.color.fuel_meter_color_outside));
            donutProgress.setUnfinishedStrokeColor(getActivity().getResources().getColor(R.color.blueGray_dark));
            donutProgress.setTextColor(getActivity().getResources().getColor(R.color.fuel_meter_color_outside));

//            Util.animateDonut(getActivity(),donutProgress,fuel_per);


            //Animate Fuel Score
            try {
                String txt_score = "6";

                if (txt_score != null && txt_score.length() > 0) {
                    float score = Float.parseFloat(txt_score);
                    if (score == 0)
                        Util.animateDonut(getActivity(), donutProgress, 0);
                    else {
                        int fuel_score = GeneralUtilities.generateRandomNumber(6, 8);
                        Util.animateDonut(getActivity(), donutProgress, 6);
                    }
                } else {
                    Util.animateDonut(getActivity(), donutProgress, 0);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }


            //set fuel range

            ((TextView) convertView.findViewById(R.id.txt_can_go)).setText("30");

            //set fuel average
            try {

                ((TextView) convertView.findViewById(R.id.txt_mpg)).setText("12.33");
            } catch (Exception e) {
                e.printStackTrace();
            }


            //Load other attribute List
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
            hashMap.put(HomeFragment.PARAM_ALIAS,"Single Fuel Volume");
            hashMap.put(HomeFragment.PARAM_VALUE, "0.10");
            hashMap.put(HomeFragment.PARAM_UNIT, "L");
            hashMaps.add(hashMap);

            HashMap<String, String> hashMap1 = new HashMap<String, String>();
            hashMap1.put(HomeFragment.PARAM_ALIAS,"Total Fuel Consumption Volume");
            hashMap1.put(HomeFragment.PARAM_VALUE, "73.46");
            hashMap1.put(HomeFragment.PARAM_UNIT, "L");
            hashMaps.add(hashMap1);

            HashMap<String, String> hashMap2 = new HashMap<String, String>();
            hashMap2.put(HomeFragment.PARAM_ALIAS,"Average Fuel Consumption");
            hashMap2.put(HomeFragment.PARAM_VALUE, "6.98");
            hashMap2.put(HomeFragment.PARAM_UNIT, "L/100 Km");
            hashMaps.add(hashMap2);

            HashMap<String, String> hashMap3 = new HashMap<String, String>();
            hashMap3.put(HomeFragment.PARAM_ALIAS,"Instantaneous Fuel Consumption");
            hashMap3.put(HomeFragment.PARAM_VALUE, "0.63");
            hashMap3.put(HomeFragment.PARAM_UNIT, "L/Km");
            hashMaps.add(hashMap3);


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
