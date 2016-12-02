package com.teramatrix.demo.fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.rey.material.widget.RadioButton;
import com.teramatrix.demo.Interface.IRefreshFragment;
import com.teramatrix.demo.MainActivity;
import com.teramatrix.demo.R;
import com.teramatrix.demo.Utils.GeneralUtilities;
import com.teramatrix.demo.Utils.SPUtils;
import com.teramatrix.demo.Utils.Util;
import com.teramatrix.demo.model.OBDVehicelInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import dmax.dialog.SpotsDialog;


/**
 * Created by arun.singh on 6/6/2016.
 */
public class HomeFragment extends Fragment implements  IRefreshFragment {

    private View convertView;
    private ImageView img_radial_menu;
    double latitude = 26.917;
    double longitude = 75.817;
    private AlertDialog dialog;


    public static String PARAM_VALUE = "param_value";
    public static String PARAM_UNIT = "param_unit";
    public static String PARAM_MAX = "param_max";
    public static String PARAM_MIN = "param_min";
    public static String PARAM_ALIAS = "param_alias";
    public static String PARAM_SERVICE_NAME = "service_name";
    public static String PARAM_SYSTEM_TIMESTAMP = "sys_timestamp";

    private String API_ID = "";
    private String API_GET_LOCATION_DATA = "location_data";
    private String API_GET_OBD_DATA = "obd_data";

    private OBDVehicelInfo obdVehicelInfo;

    public static HashMap<String, HashMap<String, String>> obdData = new HashMap<String, HashMap<String, String>>();
    public static ArrayList<OBDVehicelInfo> obdVehicelInfoArrayList = new ArrayList<OBDVehicelInfo>();

    private DonutProgress donutProgressengine_load_indicator;

    private boolean is_network_error_message_shown;

    BroadcastReceiver dataRefreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
//                callAPI(API_GET_OBD_DATA);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    BroadcastReceiver newObdDeviceSelection = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String selected_bd_device_id = intent.getStringExtra("selected_bd_device_id");
            configueAppFromSelectedODBDevice(selected_bd_device_id);
        }
    };

    BroadcastReceiver getAllConnectedObdDeviceInfo = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        obdData.clear();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.home_fragment, null, false);

                initViews();
                getActivity().registerReceiver(dataRefreshReceiver, new IntentFilter("ACTION_DASHBOARD_DATA_UPDATE"));
                getActivity().registerReceiver(newObdDeviceSelection, new IntentFilter("ACTION_OBD_SOURCE_CHANGED"));
                getActivity().registerReceiver(getAllConnectedObdDeviceInfo, new IntentFilter("ACTION_GET_ALL_OBD_SOURCE"));

                initViewsWithRealValues();

            }
            return convertView;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String methodName = "printMyName";
    private String valueObject = "Flint";

    @Override
    public void onResume() {

        try {
            super.onResume();
            GeneralUtilities.setUpActionBar(getActivity(), this, "FLINT", null);
            ((MainActivity) getActivity()).setOpenedFragment(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        try {


            super.onDestroy();
            getActivity().unregisterReceiver(dataRefreshReceiver);
            getActivity().unregisterReceiver(newObdDeviceSelection);
            getActivity().unregisterReceiver(getAllConnectedObdDeviceInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        try {
            //Disable map selection from bottom View
            convertView.findViewById(R.id.map_belo_parent).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            //Disable map selection from Score View
            convertView.findViewById(R.id.car_score_indicator).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        CarScoreFragment carScoreFragment = new CarScoreFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("screen", "Vehicle Score");
                        carScoreFragment.setArguments(bundle);

                        RadialViewFragment radialViewFragment = new RadialViewFragment();

                        GeneralUtilities.loadFragment((AppCompatActivity) getActivity(), carScoreFragment);
                    }
                    return true;
                }
            });

            img_radial_menu = (ImageView) convertView.findViewById(R.id.img_radial_menu);

            convertView.findViewById(R.id.txt_scan).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    img_radial_menu.setBackgroundResource(R.drawable.hover1);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
//                    img_radial_menu.setBackgroundResource(R.drawable.normal);

                        ScanFragment scanFragment = new ScanFragment();
                        GeneralUtilities.loadFragment((AppCompatActivity) getActivity(), scanFragment);
                    }

                    return true;
                }
            });
            convertView.findViewById(R.id.txt_track).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    img_radial_menu.setBackgroundResource(R.drawable.hover2);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
//                    img_radial_menu.setBackgroundResource(R.drawable.normal);

                        MapFragment mapFragment = new MapFragment();
                        GeneralUtilities.loadFragment((AppCompatActivity) getActivity(), mapFragment);
                    }

                    return true;
                }
            });
            convertView.findViewById(R.id.txt_dashboard).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    img_radial_menu.setBackgroundResource(R.drawable.hover3);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
//                    img_radial_menu.setBackgroundResource(R.drawable.normal);


                        DashboardFragment dashboardFragment = new DashboardFragment();
                        GeneralUtilities.loadFragment((AppCompatActivity) getActivity(), dashboardFragment);

                    }

                    return true;
                }
            });



            //Car Score
            donutProgressengine_load_indicator = (DonutProgress) convertView.findViewById(R.id.car_score_indicator);
            donutProgressengine_load_indicator.setMax(10);
            donutProgressengine_load_indicator.setProgress(0);
            donutProgressengine_load_indicator.setTextSize(60);
            donutProgressengine_load_indicator.setFinishedStrokeColor(getActivity().getResources().getColor(R.color.external_progress));
            donutProgressengine_load_indicator.setTextColor(Color.WHITE);
            donutProgressengine_load_indicator.setSuffixText("");
            donutProgressengine_load_indicator.setFinishedStrokeWidth(20);
            donutProgressengine_load_indicator.setUnfinishedStrokeWidth(20);
            Util.animateDonut(getActivity(), donutProgressengine_load_indicator, 0);


            dialog = new SpotsDialog(getActivity(), R.style.Custom);


            GeneralUtilities.getDatePeriodRange("week", null, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViewsWithRealValues() {

        try {

            //Set Car Score
            try {
                /*String txt_score = obdData.get("score").get(PARAM_VALUE);
                float score = Float.parseFloat(txt_score);*/
                donutProgressengine_load_indicator.setProgress(0);
                Util.animateDonut(getActivity(), donutProgressengine_load_indicator, Math.round(6));
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Set Location On Map
            try {
                /*String latitude = obdData.get("latitude").get(PARAM_VALUE);
                String longitude = obdData.get("longitude").get(PARAM_VALUE);
                this.latitude = Double.parseDouble(latitude);
                this.longitude = Double.parseDouble(longitude);*/
//                setUserPositionOnMap();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Set Values in Views
            try {
                ((TextView) convertView.findViewById(R.id.txt_model_name)).setText("Hyundai Creta");
                ((TextView) convertView.findViewById(R.id.txt_reg_number)).setText("RJ 14 CA 6481");
                ((TextView) convertView.findViewById(R.id.txt_trip_value)).setText("31.09 Hrs");
                ((TextView) convertView.findViewById(R.id.txt_drivername)).setText("Abhishek Jain");
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Set Account Title/SubTitle in Navigation Drawer Header
            ((MainActivity) getActivity()).setAccountInfo(" " + "Hyundai Creta", " " + "RJ 14 CA 6481" + "");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        try {
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

                try {
                /*Refresh Ticket Details. Call All Ticket APIs Again*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }




    private void initUserListDialog(final ArrayList<OBDVehicelInfo> vehicelInfoArrayList) {

        try {
            final android.support.v7.app.AlertDialog dialog;
            android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alertDialog.setTitle("Select Vehicle");
            dialog = alertDialog.create();
            dialog.getWindow().setDimAmount(0.7f);

            View hostVIew = LayoutInflater.from(getActivity()).inflate(R.layout.layout_tanker_listview, null);
            dialog.setView(hostVIew);
            final ListView listView = (ListView) hostVIew.findViewById(R.id.listview_tankers);

            listView.setAdapter(new BaseAdapter() {

                View selectedRowView;
                ViewHolder selectedViewHolder;

                @Override
                public int getCount() {
                    return vehicelInfoArrayList.size();
                }

                @Override
                public Object getItem(int position) {
                    return position;
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {

                    final ViewHolder viewHolder;
                    if (convertView == null) {
                        viewHolder = new ViewHolder();
                        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_tankers_list, null);
                        viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_tanker);
                        viewHolder.radioButton = (RadioButton) convertView.findViewById(R.id.radio_btn);
                        viewHolder.selectedRowView = convertView;
                        convertView.setTag(viewHolder);

                    } else {
                        viewHolder = (ViewHolder) convertView.getTag();
                    }


                    viewHolder.textView.setText(vehicelInfoArrayList.get(position).model_code);


                    if (obdVehicelInfo != null && obdVehicelInfo.registration_no.equalsIgnoreCase(vehicelInfoArrayList.get(position).registration_no))
                        viewHolder.radioButton.setChecked(true);
                    else
                        viewHolder.radioButton.setChecked(false);

                    viewHolder.selectedRowView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (selectedViewHolder != null) {
                                //uncheck previously selected radiobutton
                                selectedViewHolder.selectedRowView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                                selectedViewHolder.radioButton.setChecked(false);
                            }
                            //check currently selected radiobutton
                            selectedViewHolder = viewHolder;
                            selectedViewHolder.selectedRowView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                            viewHolder.radioButton.setChecked(true);

                            obdVehicelInfo = vehicelInfoArrayList.get(position);
                        }
                    });
                    viewHolder.radioButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (selectedViewHolder != null) {
                                //uncheck previously selected radiobutton
                                selectedViewHolder.selectedRowView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                                selectedViewHolder.radioButton.setChecked(false);
                            }
                            //check currently selected radiobutton
                            selectedViewHolder = viewHolder;
                            selectedViewHolder.selectedRowView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                            viewHolder.radioButton.setChecked(true);

                            obdVehicelInfo = vehicelInfoArrayList.get(position);
                        }
                    });

                    return convertView;
                }

                class ViewHolder {
                    View selectedRowView;
                    TextView textView;
                    RadioButton radioButton;
                }

            });
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (obdVehicelInfo == null) {
                        Toast.makeText(getActivity(), "Select a Vehicle", Toast.LENGTH_SHORT).show();
                    } else {
                        configueAppFromSelectedODBDevice(obdVehicelInfo.device_id);
                        dialog.dismiss();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void saveVehicleModelDetails(OBDVehicelInfo obdVehicelInfo) {
        SPUtils spUtils = new SPUtils(getActivity());
        spUtils.setValue(SPUtils.DEVICE_ID, obdVehicelInfo.device_id);
        spUtils.setValue(SPUtils.VEHICLE_MODEL, obdVehicelInfo.model_code);
        spUtils.setValue(SPUtils.VEHICLE_REGISTRATION_NUMBER, obdVehicelInfo.registration_no);
        spUtils.setValue(SPUtils.VEHICLE_ADDRESS, obdVehicelInfo.address);
        spUtils.setValue(SPUtils.VEHICLE_USERNAME, obdVehicelInfo.user_name);
        spUtils.setValue(SPUtils.VEHICLE_TYPE, obdVehicelInfo.vehicle_type);
        spUtils.setValue(SPUtils.VEHICLE_CONDITION, obdVehicelInfo.condition);
        spUtils.setValue(SPUtils.VEHICLE_ENGINE_NUMBER, obdVehicelInfo.engine_number);
        spUtils.setValue(SPUtils.VEHICLE_CHASIS_NUMBER, obdVehicelInfo.chasis_number);
        spUtils.setValue(SPUtils.VEHICLE_STATUS, obdVehicelInfo.vehicle_status);
        spUtils.setValue(SPUtils.VEHICLE_FUEL_TYPE, obdVehicelInfo.fuel_type);
        spUtils.setValue(SPUtils.VEHICLE_PURCHASE_DATE, obdVehicelInfo.purchase_date);
        spUtils.setValue(SPUtils.VEHICLE_COLOR_CODE, obdVehicelInfo.color_code);
        spUtils.setValue(SPUtils.VEHICLE_DRIVER_NAME, obdVehicelInfo.driver_name);
        spUtils.setValue(SPUtils.VEHICLE_OBD_DEVICE_NAME, obdVehicelInfo.device_name);
    }

    //Setup Account(ODB) list in Navigation Drawer.
    //Setup details in views(Complete App) for selected odb device
    private void configueAppFromSelectedODBDevice(String selected_odb_device_id) {
        try {
            OBDVehicelInfo selected_obdVehicelInfo = null;
            ArrayList<OBDVehicelInfo> obdVehicelInfos_nav_drawer_list = new ArrayList<OBDVehicelInfo>();
            for (OBDVehicelInfo obdVehicelInfo : obdVehicelInfoArrayList) {
                if (obdVehicelInfo.device_id.equalsIgnoreCase(selected_odb_device_id)) {
                    selected_obdVehicelInfo = obdVehicelInfo;
                } else {
                    obdVehicelInfos_nav_drawer_list.add(obdVehicelInfo);
                }
            }

            //Save Selected ODB Device Details in SP
            saveVehicleModelDetails(selected_obdVehicelInfo);
            //Initialize Selected Account(ODB Device) details in navigation Drawer
            ((MainActivity) getActivity()).setAccountInfo(selected_obdVehicelInfo.model_code, selected_obdVehicelInfo.registration_no);
            //Initialize Account(ODB Device) list in navigation Drawer
            ((MainActivity) getActivity()).setAccountsInNavigationDrawer(obdVehicelInfos_nav_drawer_list);

            new SPUtils(getActivity()).setValue(SPUtils.DEVICE_ID, selected_obdVehicelInfo.device_id);

            //call obd data api

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterOBDDataTagAndValues() {
        if (obdData.get("engine_status").get(PARAM_VALUE).toString().equalsIgnoreCase("0")) {
//            nullifyAllObdParamValues();
            //set value-tag to engine_status parameter
            obdData.get("engine_status").put(PARAM_VALUE, "OFF");
            obdData.get("engine_status").put(PARAM_UNIT, "");
        } else {
            //set value-tag to engine_status parameter
            obdData.get("engine_status").put(PARAM_VALUE, "ON");
            obdData.get("engine_status").put(PARAM_UNIT, "");
        }
    }

    private void nullifyAllObdParamValues() {
        Set<String> keys = obdData.keySet();
        for (String key : keys) {

            obdData.get(key).put(PARAM_MIN, "0");
            obdData.get(key).put(PARAM_MAX, "0");
            obdData.get(key).put(PARAM_VALUE, "0");
        }
    }

    @Override
    public void refreshFragment() {
//        callAPI(API_GET_OBD_DATA);
        initViewsWithRealValues();
    }


}
