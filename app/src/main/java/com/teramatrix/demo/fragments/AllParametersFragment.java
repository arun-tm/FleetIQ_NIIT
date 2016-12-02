package com.teramatrix.demo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.teramatrix.demo.Flint;
import com.teramatrix.demo.Interface.IRefreshFragment;
import com.teramatrix.demo.MainActivity;
import com.teramatrix.demo.R;
import com.teramatrix.demo.Utils.GeneralUtilities;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by arun.singh on 6/6/2016.
 */
public class AllParametersFragment extends Fragment implements IRefreshFragment {
    private View convertView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_all_parameters, null, false);
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
        }catch (Exception e)
        {
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
            GeneralUtilities.setUpActionBar(getActivity(), this, "All Parameters", null);


            final ArrayList<String> strings = new ArrayList<>();
            strings.add("Average Fuel Consumption,6.9,L/100 Km");
            strings.add("Average Hot Start Time,62,hours");
            strings.add("Average Speed,30,Km/h");
            strings.add("Battery Voltage,13.7,V");
            strings.add("Coolant Temperature,92,Deg. C");
            strings.add("Current Error Code Numbers,4,times");
            strings.add("Driving Range,0.91,Km");
            strings.add("Engine Load,26.67,%");
            strings.add("Engine Speed,750,RPM");
            strings.add("Engine Status,1,ON");
            strings.add("Harsh Acceleration No,3,times");
            strings.add("Harsh Brake No,7,times");
            strings.add("Highest History Rotation,5430,times");
            strings.add("History Highest Speed,135,Km/h");
            strings.add("Instantaneous Fuel Consumption,0.63,L/Km");
            strings.add("Running Speed,83,Km/h");
            strings.add("Single Fuel Volume,0.10,L");
            strings.add("Score,6,Points");
            strings.add("Throttle Opening Width,9.41,TPS%");
            strings.add("Total Driving TIme,31.09,hours");
            strings.add("Total Fuel Consumption Volume,73.46,L");
            strings.add("Total Harsh Acceleratino Number,819,times");
            strings.add("Total Harsh Break Number,2559,times");
            strings.add("Total Idealing Time,7.84,hours");
            strings.add("Total Ignition Number,177,times");
            strings.add("Total Mileage,91503,Km");

            //Sort Hash map on Keys value
            ListView listView =  (ListView)convertView.findViewById(R.id.listView);
            listView.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return strings.size();
                }

                @Override
                public Object getItem(int position) {
                    return strings.get(position);

                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    ViewHolder viewHolder = null;
                    if (convertView == null) {
                        viewHolder = new ViewHolder();
                        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.component_attribure_row_2, null);

                        viewHolder.paramName = ((TextView) convertView.findViewById(R.id.attribute_name));
                        viewHolder.paramValue = ((TextView) convertView.findViewById(R.id.txt_value));
                        viewHolder.paramUnit = ((TextView) convertView.findViewById(R.id.txt_unit));

                        convertView.setTag(viewHolder);
                    } else {
                        viewHolder = (ViewHolder) convertView.getTag();
                    }

                    viewHolder.paramName.setText(strings.get(position).split(",")[0]);
                    viewHolder.paramValue.setText(strings.get(position).split(",")[1]);
                    viewHolder.paramUnit.setText(strings.get(position).split(",")[2]);

                    return convertView;
                }

                class ViewHolder {
                    TextView paramName;
                    TextView paramValue;
                    TextView paramUnit;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refreshFragment() {
        initViews();
    }
}
