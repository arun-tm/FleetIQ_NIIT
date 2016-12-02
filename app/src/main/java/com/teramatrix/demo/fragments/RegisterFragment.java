package com.teramatrix.demo.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.teramatrix.demo.R;
import com.teramatrix.demo.Utils.GeneralUtilities;
import com.teramatrix.demo.model.OBDVehicelInfo;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

/**
 * Created by arun.singh on 6/6/2016.
 */
public class RegisterFragment extends Fragment {


    private View convertView;
    private AlertDialog dialog;
    private OBDVehicelInfo selectedOBDDevice;
    final ArrayList<OBDVehicelInfo> obdVehicelInfos = new ArrayList<OBDVehicelInfo>();
    String selected_idn;
    Snackbar snackbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.register_vehicle_fragment, null, false);
            initViews();
        }
        return convertView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(snackbar!=null && snackbar.isShown())
            snackbar.dismiss();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        try {

            menu.findItem(R.id.action_refresh).setVisible(false);
            menu.findItem(R.id.action_graph).setVisible(false);
            super.onPrepareOptionsMenu(menu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initViews() {
        GeneralUtilities.setUpActionBar(getActivity(), this, "FLINT", null);
        dialog = new SpotsDialog(getActivity(), R.style.Custom);

        //Listener on IDN field
        convertView.findViewById(R.id.card_view_spinner_list).setTag("gone");
        convertView.findViewById(R.id.txt_idn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (obdVehicelInfos != null && obdVehicelInfos.size() > 0)
                    toggelIDNDropDown();
            }
        });

        convertView.findViewById(R.id.shadow_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggelIDNDropDown();
            }
        });


        //Done button
        convertView.findViewById(R.id.card_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                convertView.findViewById(R.id.txt_invalid_idn).setVisibility(View.GONE);
                convertView.findViewById(R.id.txt_invalid_drivar_name).setVisibility(View.GONE);
                convertView.findViewById(R.id.txt_invalid_registration_number).setVisibility(View.GONE);
                convertView.findViewById(R.id.txt_invalid_model_name).setVisibility(View.GONE);

                if (selected_idn == null) {
                    //IDN not selected
                    convertView.findViewById(R.id.txt_invalid_idn).setVisibility(View.VISIBLE);
                    return;
                } else if (((EditText) convertView.findViewById(R.id.txt_driver_name)).getText().toString().length() == 0) {
                    //Drivaer name not entered
                    convertView.findViewById(R.id.txt_invalid_drivar_name).setVisibility(View.VISIBLE);
                    return;
                } else if (((EditText) convertView.findViewById(R.id.txt_registration_nubmer)).getText().toString().length() == 0) {
                    //Registration number not entered
                    convertView.findViewById(R.id.txt_invalid_registration_number).setVisibility(View.VISIBLE);
                    return;
                } else if (((EditText) convertView.findViewById(R.id.txt_model_code)).getText().toString().length() == 0) {
                    //Vehicle Model not entered
                    convertView.findViewById(R.id.txt_invalid_model_name).setVisibility(View.VISIBLE);
                    return;
                }


                selectedOBDDevice.driver_name = ((EditText) convertView.findViewById(R.id.txt_driver_name)).getText().toString();
                selectedOBDDevice.registration_no = ((EditText) convertView.findViewById(R.id.txt_registration_nubmer)).getText().toString();
                selectedOBDDevice.model_code = ((EditText) convertView.findViewById(R.id.txt_model_code)).getText().toString();
                //Register Selected Device with Form Data

            }
        });

    }

    private void populateDeviceIDNList() {

        final ArrayList<OBDVehicelInfo> ToBeAdded_obdVehicelInfosList = new ArrayList<OBDVehicelInfo>();
        if(HomeFragment.obdVehicelInfoArrayList.size()>0 && obdVehicelInfos.size() > 0)
        {
            for(OBDVehicelInfo AlreadyAdded_obdVehicelInfo: HomeFragment.obdVehicelInfoArrayList)
            {
                for(int i =0;i<obdVehicelInfos.size();i++)
                {
                    OBDVehicelInfo New_obdVehicelInfo = obdVehicelInfos.get(i);
                    if(AlreadyAdded_obdVehicelInfo.device_id.equalsIgnoreCase(New_obdVehicelInfo.device_id))
                    {
                        obdVehicelInfos.remove(i);
                        break;
                    }
                }
            }
        }else
        {
            return;
        }

        ToBeAdded_obdVehicelInfosList.addAll(obdVehicelInfos);

        if (ToBeAdded_obdVehicelInfosList != null && ToBeAdded_obdVehicelInfosList.size() > 0) {
            convertView.findViewById(R.id.txt_no_device_found).setVisibility(View.GONE);
            convertView.findViewById(R.id.registration_form).setVisibility(View.VISIBLE);
        } else
            return;


        //Set Dropdown Height according to number of items
        View card_view_spinner_list = convertView.findViewById(R.id.card_view_spinner_list);
        ViewGroup.LayoutParams layoutParams = card_view_spinner_list.getLayoutParams();
        int h = ToBeAdded_obdVehicelInfosList.size() * 100;
        layoutParams.height = h;
        card_view_spinner_list.setLayoutParams(layoutParams);


        //init list
        ListView list_view_idns = (ListView) convertView.findViewById(R.id.list_view_idns);
        list_view_idns.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return obdVehicelInfos.size();
            }

            @Override
            public Object getItem(int position) {
                return obdVehicelInfos.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView_local, ViewGroup parent) {

                ViewHolder viewHolder = null;
                if (convertView_local == null) {
                    viewHolder = new ViewHolder();
                    convertView_local = LayoutInflater.from(getActivity()).inflate(R.layout.layout_idn_row, null);
                    viewHolder.textViewIdn = (TextView) convertView_local.findViewById(R.id.idn_name);
                    viewHolder.rowView = convertView_local;
                    convertView_local.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView_local.getTag();
                }
                viewHolder.textViewIdn.setText(ToBeAdded_obdVehicelInfosList.get(position).device_name_alias);

                viewHolder.rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        selected_idn = ToBeAdded_obdVehicelInfosList.get(position).device_name_alias;
                        selectedOBDDevice = ToBeAdded_obdVehicelInfosList.get(position);
                        ((EditText) convertView.findViewById(R.id.txt_idn1)).setText(selected_idn);
                        toggelIDNDropDown();
                    }
                });

                return convertView_local;
            }

            class ViewHolder {
                TextView textViewIdn;
                View rowView;
            }
        });
    }

    private void toggelIDNDropDown() {
        if (convertView.findViewById(R.id.card_view_spinner_list).getTag().toString().equalsIgnoreCase("gone")) {
            convertView.findViewById(R.id.card_view_spinner_list).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.card_view_spinner_list).setTag("visible");
            convertView.findViewById(R.id.shadow_layout).setVisibility(View.VISIBLE);

        } else if (convertView.findViewById(R.id.card_view_spinner_list).getTag().toString().equalsIgnoreCase("visible")) {
            convertView.findViewById(R.id.card_view_spinner_list).setVisibility(View.GONE);
            convertView.findViewById(R.id.card_view_spinner_list).setTag("gone");
            convertView.findViewById(R.id.shadow_layout).setVisibility(View.GONE);
        }
    }



    String response_messag ="";

}
