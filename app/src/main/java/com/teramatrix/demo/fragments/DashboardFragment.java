package com.teramatrix.demo.fragments;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cardiomood.android.controls.gauge.SpeedometerGauge;
import com.teramatrix.demo.Flint;
import com.teramatrix.demo.Interface.IRefreshFragment;
import com.teramatrix.demo.MainActivity;
import com.teramatrix.demo.R;
import com.teramatrix.demo.Utils.GeneralUtilities;
import com.teramatrix.demo.velocimeter.velocimeterlibrary.VelocimeterView;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by arun.singh on 6/6/2016.
 */
public class DashboardFragment extends Fragment implements IRefreshFragment {


    private View convertView;
    private SpeedometerGauge speedometer;
    private VelocimeterView velocimeter, velocimeter_fuel, velocimeter_rpm;
    Timer timer;
    Timer timer_fuel;
    private int fuel_remaining = 40;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.dashboard_fragment, null, false);
            initViews();
        }
        return convertView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setOpenedFragment(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        //Stop Speed  meter timer
        if (timer != null)
            timer.cancel();

        //Stop fuel meter timer
        if (timer_fuel != null)
            timer_fuel.cancel();
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //Hiding some option menus which are not required
        menu.findItem(R.id.action_graph).setVisible(false);
        super.onPrepareOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
            case R.id.action_refresh: {

            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void initViews() {

        try {

            GeneralUtilities.setUpActionBar(getActivity(), this, "My Vehicle", null);
            /*-------------------------------------------------------------------------------------------------*/
            //set Odometer Value
//            ((TextView) convertView.findViewById(R.id.tv_km_driven)).setText(HomeFragment.obdData.get("total_mileage").get(HomeFragment.PARAM_VALUE).toString());
            ((TextView) convertView.findViewById(R.id.tv_km_driven)).setText("9,875");
            /*-------------------------------------------------------------------------------------------------*/
            //Set average Speed of Vehicle
            ((TextView) convertView.findViewById(R.id.tv_car_speed_avg_value)).setText("83" + " kmph");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

            //calculate percent of speed from max value
            try {
                /*int running_speed_max = Integer.parseInt(HomeFragment.obdData.get("running_speed").get(HomeFragment.PARAM_MAX).toString());
                int running_speed = Integer.parseInt(HomeFragment.obdData.get("average_speed").get(HomeFragment.PARAM_VALUE).toString());
                float speed_percentage = (running_speed * 100) / running_speed_max;
                params.weight = speed_percentage;
                convertView.findViewById(R.id.rl_car_speed_avg_bar).setLayoutParams(params);*/

                int running_speed_max = 180;
                int running_speed = 76;
                float speed_percentage = (running_speed * 100) / running_speed_max;
                params.weight = speed_percentage;
                convertView.findViewById(R.id.rl_car_speed_avg_bar).setLayoutParams(params);
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            /*-------------------------------------------------------------------------------------------------*/
            //Set Fuel average of Vehicle
            try {
                ((TextView) convertView.findViewById(R.id.tv_car_fuel_average_value)).setText("12" + " kmpl");
                float current_fuel_avg = 12;

                if(current_fuel_avg>0) {
                    float v = 100/current_fuel_avg;
                    v = 12.33f;
                    String s = String.format("%.02f", v);
                    ((TextView) convertView.findViewById(R.id.tv_car_fuel_average_value)).setText(s + " kmpl");
                }

                LinearLayout.LayoutParams avg_fuelbar_params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                float avg_fuel_per = (current_fuel_avg * 100) / 20;
                avg_fuelbar_params.weight = avg_fuel_per;
                convertView.findViewById(R.id.rl_car_average_bar).setLayoutParams(avg_fuelbar_params);
            }catch (Exception e)
            {
                e.printStackTrace();
            }


            /*-------------------------------------------------------------------------------------------------*/

            //calculate remaining battery life
            try {
                String battery_min_value = "0";
                String battery_max_value = "100";
                String battery_current_value = "12.55";
                /*int upper_val = (int) (Float.parseFloat(battery_current_value) - Integer.parseInt(battery_min_value));
                int lower_val = Integer.parseInt(battery_max_value) - Integer.parseInt(battery_min_value);
                int per = ((upper_val * 100) / lower_val);*/

                float battery = Float.parseFloat(battery_current_value);
                int per =0;
                if(battery >= 12.60)
                {
                    per =  100;
                }else if(battery >= 12.40)
                {
                    per =  75;
                }else if(battery >= 12.20)
                {
                    per =  50;
                }else if(battery >= 12.00)
                {
                    per =  25;
                    blinkTextView();
                }else
                {
                    per =  0;
                }


                ((TextView) convertView.findViewById(R.id.tv_car_battery_value)).setText(per + " %");
                LinearLayout.LayoutParams battery_bar_params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                battery_bar_params.weight = per;
                convertView.findViewById(R.id.rl_car_battery_bar).setLayoutParams(battery_bar_params);
            }catch (Exception e)
            {
                e.printStackTrace();
            }



            /*-------------------------------------------------------------------------------------------------*/
            //set RPM Meter Value
            try {
                final String rpm_min_value = "1000";
               final String rpm_max_value = "12000";
                final int rpm_current_value = 6500;
                int rpm_perc = ((rpm_current_value - Integer.parseInt(rpm_min_value)) * 100) / (Integer.parseInt(rpm_max_value) - Integer.parseInt(rpm_min_value));

                velocimeter_rpm = (VelocimeterView) convertView.findViewById(R.id.velocimeter_rpm);
                velocimeter_rpm.setMax(100);
                velocimeter_rpm.setValue(rpm_perc, false, rpm_current_value - rpm_perc);


                //set timer for fuel indicator
                timer_fuel = new Timer();
                TimerTask timerTask_fuel = new TimerTask() {
                    @Override
                    public void run() {
                        Random random = new Random();
                        final int randomNum = random.nextInt((7000 - 6000) + 1) + 6000;
                        final int rpm_perc_local = ((randomNum - Integer.parseInt(rpm_min_value)) * 100) / (Integer.parseInt(rpm_max_value) - Integer.parseInt(rpm_min_value));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                velocimeter_rpm.setValue(rpm_perc_local, true, randomNum - rpm_perc_local);
                            }
                        });
                    }
                };
            timer_fuel.schedule(timerTask_fuel, 0, 3 * 1000);


            }catch (Exception e)
            {
                e.printStackTrace();
            }


            //Set Speedometer Value
            try {
                String speed_min_value = "0";
                String speed_max_value = "180";
                int speed_current_value = 85;
//            speed_current_value = 110;
//            int speed_perc = ((speed_current_value - Integer.parseInt(speed_min_value))*100)/(Integer.parseInt(speed_max_value) - Integer.parseInt(speed_min_value));

                velocimeter = (VelocimeterView) convertView.findViewById(R.id.velocimeter);
                velocimeter.setMax(220);
                velocimeter.setValue(speed_current_value, true, 0);
            }catch (Exception e)
            {
                e.printStackTrace();
            }



            //Set Fuel Meter Value
            try {
                String fuel_min_value = "0";
                String fuel_max_value = "35";
                float fuel_current_value = 27;
//            int fuel_perc = ((rpm_current_value - Integer.parseInt(rpm_min_value))*100)/(Integer.parseInt(rpm_max_value) - Integer.parseInt(rpm_min_value));
                velocimeter_fuel = (VelocimeterView) convertView.findViewById(R.id.velocimeter_fuel);
                velocimeter_fuel.setMax(100);
                velocimeter_fuel.setValue(fuel_current_value, true, 0);

                //Set Driving Range
                String driving_range_value = "30";
                ((TextView) convertView.findViewById(R.id.tv_trip_km_value)).setText(GeneralUtilities.getRoundOffValue(driving_range_value,2));

            }catch (Exception e)
            {
                e.printStackTrace();
            }


            //set timer for speed
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Random random = new Random();
                    final int randomNum = random.nextInt((90 - 40) + 1) + 40;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            velocimeter.setValue(randomNum, true,0);
//                            velocimeter_rpm.setValue(randomNum, true);
                        }
                    });
                }
            };
            timer.schedule(timerTask, 0, 3000);





            convertView.findViewById(R.id.tv_place).setAlpha(0.7f);
            convertView.findViewById(R.id.tv_place).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    MapFragment mapFragment = new MapFragment();
                    MapFragment mapFragment = new MapFragment();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    GeneralUtilities.loadFragment((AppCompatActivity) getActivity(), mapFragment);
                }
            });



            //Resolve Adrees Line from Latitude and Longitude
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String location_name =getCityNameFromLocation(Double.parseDouble(HomeFragment.obdData.get("latitude").get(HomeFragment.PARAM_VALUE)),Double.parseDouble(HomeFragment.obdData.get("longitude").get(HomeFragment.PARAM_VALUE)));
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    String foundLocation = "";
                                    if (location_name == null || location_name.length() == 0)
                                        foundLocation = "No location found";
                                    else
                                        foundLocation = location_name;

                                    ((TextView) convertView.findViewById(R.id.tv_place)).setText(foundLocation);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });



            convertView.findViewById(R.id.avg_speed_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*HistoryFragment historyFragment = new HistoryFragment();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    GeneralUtilities.loadFragment((AppCompatActivity) getActivity(), historyFragment);*/
                }
            });



        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void blinkTextView() {

        try {
            Animation anim = new AlphaAnimation(0.4f, 1.0f);
            anim.setDuration(350); //You can manage the blinking time with this parameter
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            convertView.findViewById(R.id.tv_car_battery_value).startAnimation(anim);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private String getCityNameFromLocation(double lat,double lng)
    {
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            String cityName = addresses.get(0).getAddressLine(0);
            String stateName = addresses.get(0).getAddressLine(1);
            String countryName = addresses.get(0).getAddressLine(2);
            return  cityName+","+stateName;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    public void refreshFragment() {
        initViews();
    }
}
