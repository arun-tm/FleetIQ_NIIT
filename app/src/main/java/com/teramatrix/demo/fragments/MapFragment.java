package com.teramatrix.demo.fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.teramatrix.demo.Flint;
import com.teramatrix.demo.Interface.IRefreshFragment;
import com.teramatrix.demo.R;
import com.teramatrix.demo.Utils.GeneralUtilities;
import com.teramatrix.demo.Utils.SPUtils;
import com.teramatrix.demo.model.LocationPoints;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import dmax.dialog.SpotsDialog;
import in.teramatrix.slidingpanel.SlidingUpPanelLayout;

/**
 * Created by arun.singh on 6/6/2016.
 */
public class MapFragment extends Fragment implements IRefreshFragment, DatePickerDialog.OnDateSetListener {


    double latitude = 26.917;
    double longitude = 75.817;
    Snackbar snackbar;
    private AlertDialog dialog;
    private TextView txtFromDate;
    private TextView txtToDate;
    private Date to = null;
    private Date from = null;
    SimpleDateFormat formatWithoutTime = new SimpleDateFormat("dd MMM yyyy");
    SimpleDateFormat formatWithTime = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
    private static final String prefixTo = "To_Date";
    private static final String prefixFrom = "From_Date";
    private SlidingUpPanelLayout panelLayout;
    private ImageView indicator;
    private ArrayList<ArrayList<LocationPoints>> routeList = new ArrayList<ArrayList<LocationPoints>>();
    private DonutProgress donutProgressengine_load_indicator;
    private View convertView;
    String eachTripDistance[];

    private TripAddapter tripAddapter;


    private String trip_demo_string ="{\"description\":\" Success\",\"object\":[{\"device_id\":\"379\",\"is_active\":\"1\",\"vehicle_type\":\"Four wheeler\",\"condition\":\"Brand new\",\"chasis_number\":\"18956445344\",\"vehicle_identity_no\":\"2fdd2235-39d2-11e6-8051-00ff9de4742d\",\"vehicle_id\":\"1\",\"fuel_type\":\"Hybrid\",\"registration_no\":\"RJ20CA2044\",\"device_name\":\"6610402169477\",\"engine_number\":\"TMD54654\",\"color_code\":\"#0000000\",\"model_code\":\"Honda City\",\"creation_time\":\"2016-06-24 11:56:45\",\"driver\":\"1\",\"purchase_date\":\"2016-06-24 00:12:01\",\"vehicle_statusGood\":\"Excellent\",\"device_key\":null,\"trips\":[[{\"address\":\"66/164 Heera Path, Mansarovar, Jaipur\",\"distance\":6.4,\"latitude\":\"26.883351666666666\",\"trip_no\":10,\"sys_timestamp\":\"2016-11-08 00:49:00\",\"longitude\":\"75.780155\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.883601666666667\",\"trip_no\":10,\"sys_timestamp\":\"2016-11-08 00:50:00\",\"longitude\":\"75.77547833333334\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.879986666666667\",\"trip_no\":10,\"sys_timestamp\":\"2016-11-08 00:51:00\",\"longitude\":\"75.77396166666666\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.880253333333332\",\"trip_no\":10,\"sys_timestamp\":\"2016-11-08 00:52:00\",\"longitude\":\"75.77186333333333\"},{\"address\":\"147,Heera Nagar ,DCM ,Ajmer Road \",\"distance\":6.4,\"latitude\":\"26.880561666666665\",\"trip_no\":10,\"sys_timestamp\":\"2016-11-08 00:53:00\",\"longitude\":\"75.77148333333334\"}],[{\"address\":\"45, Chand bihari, Jhotwara\",\"distance\":2.1,\"latitude\":\"26.897388333333332\",\"trip_no\":88,\"sys_timestamp\":\"2016-11-28 10:45:00\",\"longitude\":\"75.75368\"},{\"address\":null,\"distance\":2,\"latitude\":\"26.897388333333332\",\"trip_no\":88,\"sys_timestamp\":\"2016-11-28 10:46:00\",\"longitude\":\"75.75368\"},{\"address\":null,\"distance\":2,\"latitude\":\"26.897388333333332\",\"trip_no\":88,\"sys_timestamp\":\"2016-11-28 10:48:00\",\"longitude\":\"75.75368\"},{\"address\":null,\"distance\":2,\"latitude\":\"26.897388333333332\",\"trip_no\":88,\"sys_timestamp\":\"2016-11-28 10:49:00\",\"longitude\":\"75.75368\"},{\"address\":null,\"distance\":2,\"latitude\":\"26.897383333333334\",\"trip_no\":88,\"sys_timestamp\":\"2016-11-28 10:50:00\",\"longitude\":\"75.753685\"},{\"address\":null,\"distance\":2,\"latitude\":\"26.89586\",\"trip_no\":88,\"sys_timestamp\":\"2016-11-28 10:51:00\",\"longitude\":\"75.75131166666667\"},{\"address\":null,\"distance\":2,\"latitude\":\"26.894693333333333\",\"trip_no\":88,\"sys_timestamp\":\"2016-11-28 10:52:00\",\"longitude\":\"75.74893\"},{\"address\":null,\"distance\":2,\"latitude\":\"26.896141666666665\",\"trip_no\":88,\"sys_timestamp\":\"2016-11-28 10:53:00\",\"longitude\":\"75.74482666666667\"},{\"address\":\"4 ,Keshav Apartment, Doctors Colony,Chitrakoot\",\"distance\":2.1,\"latitude\":\"26.896141666666665\",\"trip_no\":88,\"sys_timestamp\":\"2016-11-28 10:54:00\",\"longitude\":\"75.74470833333334\"}],[{\"address\":\"Rajasthan University,JLN marg \",\"distance\":5.6,\"latitude\":\"26.896208333333334\",\"trip_no\":89,\"sys_timestamp\":\"2016-11-28 13:11:00\",\"longitude\":\"75.74502\"},{\"address\":null,\"distance\":5,\"latitude\":\"26.893651666666667\",\"trip_no\":89,\"sys_timestamp\":\"2016-11-28 13:12:00\",\"longitude\":\"75.74649833333334\"},{\"address\":null,\"distance\":5,\"latitude\":\"26.896706666666667\",\"trip_no\":89,\"sys_timestamp\":\"2016-11-28 13:13:00\",\"longitude\":\"75.752675\"},{\"address\":null,\"distance\":5,\"latitude\":\"26.898626666666665\",\"trip_no\":89,\"sys_timestamp\":\"2016-11-28 13:14:00\",\"longitude\":\"75.76289333333334\"},{\"address\":null,\"distance\":5,\"latitude\":\"26.901288333333333\",\"trip_no\":89,\"sys_timestamp\":\"2016-11-28 13:15:00\",\"longitude\":\"75.77122666666666\"},{\"address\":null,\"distance\":5,\"latitude\":\"26.909158333333334\",\"trip_no\":89,\"sys_timestamp\":\"2016-11-28 13:16:00\",\"longitude\":\"75.78047833333333\"},{\"address\":null,\"distance\":5,\"latitude\":\"26.913255\",\"trip_no\":89,\"sys_timestamp\":\"2016-11-28 13:17:00\",\"longitude\":\"75.78955500000001\"},{\"address\":null,\"distance\":5,\"latitude\":\"26.91461\",\"trip_no\":89,\"sys_timestamp\":\"2016-11-28 13:18:00\",\"longitude\":\"75.79431166666667\"},{\"address\":\"87/112 Vijay Path,Mansarovar\",\"distance\":5.6,\"latitude\":\"26.914593333333332\",\"trip_no\":89,\"sys_timestamp\":\"2016-11-28 13:19:00\",\"longitude\":\"75.79445333333334\"}],[{\"address\":\"B-4 Niit ,C-Scheme,Jaipur\",\"distance\":3.2,\"latitude\":\"26.91094\",\"trip_no\":90,\"sys_timestamp\":\"2016-11-28 14:55:00\",\"longitude\":\"75.79437166666666\"},{\"address\":null,\"distance\":3.2,\"latitude\":\"26.912185\",\"trip_no\":90,\"sys_timestamp\":\"2016-11-28 14:56:00\",\"longitude\":\"75.79490166666666\"},{\"address\":null,\"distance\":3.2,\"latitude\":\"26.914223333333332\",\"trip_no\":90,\"sys_timestamp\":\"2016-11-28 14:59:00\",\"longitude\":\"75.793445\"},{\"address\":null,\"distance\":3.2,\"latitude\":\"26.91398\",\"trip_no\":90,\"sys_timestamp\":\"2016-11-28 15:00:00\",\"longitude\":\"75.79261166666667\"},{\"address\":null,\"distance\":3.2,\"latitude\":\"26.913815\",\"trip_no\":90,\"sys_timestamp\":\"2016-11-28 15:01:00\",\"longitude\":\"75.79206833333333\"},{\"address\":null,\"distance\":3.2,\"latitude\":\"26.91367\",\"trip_no\":90,\"sys_timestamp\":\"2016-11-28 15:02:00\",\"longitude\":\"75.791705\"},{\"address\":null,\"distance\":3.2,\"latitude\":\"26.913425\",\"trip_no\":90,\"sys_timestamp\":\"2016-11-28 15:03:00\",\"longitude\":\"75.791005\"},{\"address\":null,\"distance\":3.2,\"latitude\":\"26.910806666666666\",\"trip_no\":90,\"sys_timestamp\":\"2016-11-28 15:04:00\",\"longitude\":\"75.78406\"},{\"address\":null,\"distance\":3.2,\"latitude\":\"26.909145\",\"trip_no\":90,\"sys_timestamp\":\"2016-11-28 15:05:00\",\"longitude\":\"75.78071166666666\"},{\"address\":null,\"distance\":3.2,\"latitude\":\"26.901521666666667\",\"trip_no\":90,\"sys_timestamp\":\"2016-11-28 15:06:00\",\"longitude\":\"75.77181333333333\"},{\"address\":null,\"distance\":3.2,\"latitude\":\"26.89921\",\"trip_no\":90,\"sys_timestamp\":\"2016-11-28 15:07:00\",\"longitude\":\"75.76748333333333\"},{\"address\":\"Vishal Megha mart,MI Road Jaipur\",\"distance\":3.2,\"latitude\":\"26.897821666666665\",\"trip_no\":90,\"sys_timestamp\":\"2016-11-28 15:08:00\",\"longitude\":\"75.76447833333333\"}],[{\"address\":\"Sanganer Petrol Pump,Sanganer\",\"distance\":2.8,\"latitude\":\"26.898088333333334\",\"trip_no\":91,\"sys_timestamp\":\"2016-11-28 15:37:00\",\"longitude\":\"75.76467833333334\"},{\"address\":null,\"distance\":2.8,\"latitude\":\"26.898568333333333\",\"trip_no\":91,\"sys_timestamp\":\"2016-11-28 15:38:00\",\"longitude\":\"75.764585\"},{\"address\":null,\"distance\":2.8,\"latitude\":\"26.898415\",\"trip_no\":91,\"sys_timestamp\":\"2016-11-28 15:39:00\",\"longitude\":\"75.75636333333334\"},{\"address\":null,\"distance\":2.8,\"latitude\":\"26.895658333333333\",\"trip_no\":91,\"sys_timestamp\":\"2016-11-28 15:40:00\",\"longitude\":\"75.75085166666666\"},{\"address\":null,\"distance\":2.8,\"latitude\":\"26.895785\",\"trip_no\":91,\"sys_timestamp\":\"2016-11-28 15:41:00\",\"longitude\":\"75.74636166666667\"},{\"address\":\"C-45 ICG College,SFS ,Mansarovar\",\"distance\":2.8,\"latitude\":\"26.896158333333332\",\"trip_no\":91,\"sys_timestamp\":\"2016-11-28 15:42:00\",\"longitude\":\"75.74471666666666\"}],[{\"address\":\"Subhash Marg, Ashok Nagar, Jaipur\",\"distance\":3.7,\"latitude\":\"26.896155\",\"trip_no\":92,\"sys_timestamp\":\"2016-11-28 23:18:00\",\"longitude\":\"75.74458666666666\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.895473333333335\",\"trip_no\":92,\"sys_timestamp\":\"2016-11-28 23:19:00\",\"longitude\":\"75.75013833333334\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.886381666666665\",\"trip_no\":92,\"sys_timestamp\":\"2016-11-28 23:21:00\",\"longitude\":\"75.754745\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.880645\",\"trip_no\":92,\"sys_timestamp\":\"2016-11-28 23:22:00\",\"longitude\":\"75.75644833333334\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.880568333333333\",\"trip_no\":92,\"sys_timestamp\":\"2016-11-28 23:23:00\",\"longitude\":\"75.76732833333334\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.878866666666667\",\"trip_no\":92,\"sys_timestamp\":\"2016-11-28 23:24:00\",\"longitude\":\"75.77285666666667\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.880261666666666\",\"trip_no\":92,\"sys_timestamp\":\"2016-11-28 23:25:00\",\"longitude\":\"75.773595\"},{\"address\":\"22 Godam Circle, Civil Lines, Jaipur\",\"distance\":3.7,\"latitude\":\"26.880636666666668\",\"trip_no\":92,\"sys_timestamp\":\"2016-11-28 23:26:00\",\"longitude\":\"75.77154833333333\"}],[{\"address\":\"D-Block, Malviya Nagar, Jaipur\",\"distance\":7.2,\"latitude\":\"26.877663333333334\",\"trip_no\":94,\"sys_timestamp\":\"2016-11-29 05:36:00\",\"longitude\":\"75.77460666666667\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.878896666666666\",\"trip_no\":94,\"sys_timestamp\":\"2016-11-29 05:37:00\",\"longitude\":\"75.76861166666667\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.880796666666665\",\"trip_no\":94,\"sys_timestamp\":\"2016-11-29 05:38:00\",\"longitude\":\"75.76007666666666\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.886325\",\"trip_no\":94,\"sys_timestamp\":\"2016-11-29 05:39:00\",\"longitude\":\"75.75429833333334\"},{\"address\":\"Mahatma Gandhi Nagar, Jaipur\",\"distance\":7.2,\"latitude\":\"26.887008333333334\",\"trip_no\":94,\"sys_timestamp\":\"2016-11-29 05:40:00\",\"longitude\":\"75.75282833333333\"}],[{\"address\":\"World Trade Park Pvt. Ltd., Gaurav tower, Gaurav Tower Marg, Siddharth Nagar, Sector 10, Malviya Nagar, Jaipur\",\"distance\":1.89,\"latitude\":\"26.895095\",\"trip_no\":95,\"sys_timestamp\":\"2016-11-29 05:43:00\",\"longitude\":\"75.75117\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.895983333333334\",\"trip_no\":95,\"sys_timestamp\":\"2016-11-29 05:44:00\",\"longitude\":\"75.75052\"},{\"address\":\"Near Raj Mandir Cinema Hall, Golcha Point, Panch Batti, Mirza Ismail Road, Jaipur\",\"distance\":1.89,\"latitude\":\"26.89621\",\"trip_no\":95,\"sys_timestamp\":\"2016-11-29 05:45:00\",\"longitude\":\"75.745765\"}],[{\"address\":\" Crystal Palm, 49, Patel Nagar, Bais Godam, Sardar Patel Road, Ashok Nagar, Jaipur\",\"distance\":4.4,\"latitude\":\"26.896408333333333\",\"trip_no\":96,\"sys_timestamp\":\"2016-11-29 12:30:00\",\"longitude\":\"75.7442\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.894288333333332\",\"trip_no\":96,\"sys_timestamp\":\"2016-11-29 12:31:00\",\"longitude\":\"75.747585\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.89851\",\"trip_no\":96,\"sys_timestamp\":\"2016-11-29 12:33:00\",\"longitude\":\"75.75642166666667\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.89869\",\"trip_no\":96,\"sys_timestamp\":\"2016-11-29 12:34:00\",\"longitude\":\"75.76057833333333\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.898936666666668\",\"trip_no\":96,\"sys_timestamp\":\"2016-11-29 12:35:00\",\"longitude\":\"75.76668833333333\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.899003333333333\",\"trip_no\":96,\"sys_timestamp\":\"2016-11-29 12:36:00\",\"longitude\":\"75.76757\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.896971666666666\",\"trip_no\":96,\"sys_timestamp\":\"2016-11-29 12:37:00\",\"longitude\":\"75.76779333333333\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.894628333333333\",\"trip_no\":96,\"sys_timestamp\":\"2016-11-29 12:38:00\",\"longitude\":\"75.76968166666667\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.894433333333332\",\"trip_no\":96,\"sys_timestamp\":\"2016-11-29 12:39:00\",\"longitude\":\"75.76968166666667\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.89429\",\"trip_no\":96,\"sys_timestamp\":\"2016-11-29 12:40:00\",\"longitude\":\"75.76993166666666\"},{\"address\":\"Site no .5,,Nagarbhavi Main road,,Opp. to Cordial High School, Magadi road Thimmenahalli village and layout, Kemapura Dakale (Agrahara), Prashanthnagar Vijayanagar\",\"distance\":4.4,\"latitude\":\"26.890246666666666\",\"trip_no\":96,\"sys_timestamp\":\"2016-11-29 12:42:00\",\"longitude\":\"75.77158666666666\"}],[{\"address\":\"Pink Square Mall, Shop No 5 & 6 Pink Square Mall, Raja Park, Jaipur, Rajasthan\",\"distance\":1.6,\"latitude\":\"26.890231666666665\",\"trip_no\":97,\"sys_timestamp\":\"2016-11-29 14:05:00\",\"longitude\":\"75.771645\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.890471666666667\",\"trip_no\":97,\"sys_timestamp\":\"2016-11-29 14:06:00\",\"longitude\":\"75.77147333333333\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.890171666666667\",\"trip_no\":97,\"sys_timestamp\":\"2016-11-29 14:07:00\",\"longitude\":\"75.76932\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.892063333333333\",\"trip_no\":97,\"sys_timestamp\":\"2016-11-29 14:08:00\",\"longitude\":\"75.76923166666667\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.89372\",\"trip_no\":97,\"sys_timestamp\":\"2016-11-29 14:09:00\",\"longitude\":\"75.76970333333334\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.894443333333335\",\"trip_no\":97,\"sys_timestamp\":\"2016-11-29 14:10:00\",\"longitude\":\"75.76981666666667\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.89525666666667\",\"trip_no\":97,\"sys_timestamp\":\"2016-11-29 14:11:00\",\"longitude\":\"75.76751666666667\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.898835\",\"trip_no\":97,\"sys_timestamp\":\"2016-11-29 14:12:00\",\"longitude\":\"75.76769666666667\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.89854\",\"trip_no\":97,\"sys_timestamp\":\"2016-11-29 14:13:00\",\"longitude\":\"75.76599333333333\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.898548333333334\",\"trip_no\":97,\"sys_timestamp\":\"2016-11-29 14:14:00\",\"longitude\":\"75.76145\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.898493333333334\",\"trip_no\":97,\"sys_timestamp\":\"2016-11-29 14:15:00\",\"longitude\":\"75.75662333333334\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.896031666666666\",\"trip_no\":97,\"sys_timestamp\":\"2016-11-29 14:16:00\",\"longitude\":\"75.75159833333333\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.895533333333333\",\"trip_no\":97,\"sys_timestamp\":\"2016-11-29 14:17:00\",\"longitude\":\"75.75050333333333\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.895268333333334\",\"trip_no\":97,\"sys_timestamp\":\"2016-11-29 14:18:00\",\"longitude\":\"75.74671166666667\"},{\"address\":\"Plot No. 25, Kalyan Colony, Malviya Nagar, Opposite Gaurav Tower, Jaipur\",\"distance\":1.6,\"latitude\":\"26.896221666666666\",\"trip_no\":97,\"sys_timestamp\":\"2016-11-29 14:19:00\",\"longitude\":\"75.74475666666666\"}],[{\"address\":\"Upper & Lower Ground Floor Shop No UG 2 & LG 2 DP Metro Plot no 5 & 6, Shankar Bhawan Sahakari Samity Ltd Chowkri Haveli Sahar Sodala\",\"distance\":1.8,\"latitude\":\"26.896211666666666\",\"trip_no\":98,\"sys_timestamp\":\"2016-11-29 17:50:00\",\"longitude\":\"75.74498\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.89558\",\"trip_no\":98,\"sys_timestamp\":\"2016-11-29 17:51:00\",\"longitude\":\"75.75026666666666\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.895791666666668\",\"trip_no\":98,\"sys_timestamp\":\"2016-11-29 17:54:00\",\"longitude\":\"75.75091833333333\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.894936666666666\",\"trip_no\":98,\"sys_timestamp\":\"2016-11-29 17:55:00\",\"longitude\":\"75.75133333333333\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.888523333333332\",\"trip_no\":98,\"sys_timestamp\":\"2016-11-29 17:56:00\",\"longitude\":\"75.752875\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.886505\",\"trip_no\":98,\"sys_timestamp\":\"2016-11-29 17:57:00\",\"longitude\":\"75.75515166666666\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.882688333333334\",\"trip_no\":98,\"sys_timestamp\":\"2016-11-29 17:58:00\",\"longitude\":\"75.75662833333334\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.880656666666667\",\"trip_no\":98,\"sys_timestamp\":\"2016-11-29 17:59:00\",\"longitude\":\"75.76614333333333\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.87942333333333\",\"trip_no\":98,\"sys_timestamp\":\"2016-11-29 18:00:00\",\"longitude\":\"75.772315\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.879705\",\"trip_no\":98,\"sys_timestamp\":\"2016-11-29 18:01:00\",\"longitude\":\"75.773465\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.880505\",\"trip_no\":98,\"sys_timestamp\":\"2016-11-29 18:02:00\",\"longitude\":\"75.77205\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.880505\",\"trip_no\":98,\"sys_timestamp\":\"2016-11-29 18:04:00\",\"longitude\":\"75.77136333333333\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.880505\",\"trip_no\":98,\"sys_timestamp\":\"2016-11-29 18:05:00\",\"longitude\":\"75.77136333333333\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.880505\",\"trip_no\":98,\"sys_timestamp\":\"2016-11-29 18:06:00\",\"longitude\":\"75.77136333333333\"},{\"address\":\"Shop No. G-1 & G-2, Kalwad Road, Jothwada, Jaipur, Rajasthan\",\"distance\":1.8,\"latitude\":\"26.880476666666667\",\"trip_no\":98,\"sys_timestamp\":\"2016-11-29 18:07:00\",\"longitude\":\"75.77142333333333\"}],[{\"address\":\"Plot No. C1, Block A, Ground Floor, Vaibhav Cine Multiplex, Near Amrapali Circle, Gautam Nagar, Vaishali Nagar,\",\"distance\":3.6,\"latitude\":\"26.880476666666667\",\"trip_no\":99,\"sys_timestamp\":\"2016-11-30 08:02:00\",\"longitude\":\"75.77142333333333\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.879103333333333\",\"trip_no\":99,\"sys_timestamp\":\"2016-11-30 08:03:00\",\"longitude\":\"75.76989333333333\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.878921666666667\",\"trip_no\":99,\"sys_timestamp\":\"2016-11-30 08:04:00\",\"longitude\":\"75.76609166666667\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.879015\",\"trip_no\":99,\"sys_timestamp\":\"2016-11-30 08:05:00\",\"longitude\":\"75.76021333333334\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.878758333333334\",\"trip_no\":99,\"sys_timestamp\":\"2016-11-30 08:06:00\",\"longitude\":\"75.75608833333334\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.88297\",\"trip_no\":99,\"sys_timestamp\":\"2016-11-30 08:07:00\",\"longitude\":\"75.75263333333334\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.887021666666666\",\"trip_no\":99,\"sys_timestamp\":\"2016-11-30 08:08:00\",\"longitude\":\"75.752095\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.890433333333334\",\"trip_no\":99,\"sys_timestamp\":\"2016-11-30 08:09:00\",\"longitude\":\"75.7513\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.894191666666668\",\"trip_no\":99,\"sys_timestamp\":\"2016-11-30 08:10:00\",\"longitude\":\"75.75124333333333\"},{\"address\":null,\"distance\":1.89,\"latitude\":\"26.895578333333333\",\"trip_no\":99,\"sys_timestamp\":\"2016-11-30 08:11:00\",\"longitude\":\"75.75074333333333\"},{\"address\":\" Plot No. B, Mansarovar Plaza, Madhyam Marg, Sector 7, Near KV-5, Mansarovar, Jaipur\",\"distance\":3.6,\"latitude\":\"26.895828333333334\",\"trip_no\":99,\"sys_timestamp\":\"2016-11-30 08:12:00\",\"longitude\":\"75.746245\"}]]}],\"list\":null,\"valid\":true}";


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_map2, null, false);

            View view = container.findViewById(R.id.linerlayout_daterange);

            initializePanel();

            dialog = new SpotsDialog(getActivity(), R.style.Custom);
            dialog.setCancelable(false);


            initViews();
        }

        return convertView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
        if (dialog != null)
            dialog.dismiss();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_graph:
                // app icon in action bar clicked; goto parent activity.
                CustomChartFragment customChartFragment = new CustomChartFragment();
                GeneralUtilities.loadFragment((AppCompatActivity) getActivity(), customChartFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initViews() {
        try {

            GeneralUtilities.setUpActionBar(getActivity(), this, new SPUtils(getActivity()).getVehicleModel() + " Trips", null);

            txtFromDate = (TextView) convertView.findViewById(R.id.txt_from_date);
            txtToDate = (TextView) convertView.findViewById(R.id.txt_to_date);

            txtFromDate.setText(GeneralUtilities.getDateTime("IST", "d MMM yyyy", -30));
            txtToDate.setText(GeneralUtilities.getDateTime("IST", "d MMM yyyy", 0));


            txtFromDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPicker(prefixFrom, txtFromDate.getText().toString());
                }
            });
            txtToDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPicker(prefixTo, txtToDate.getText().toString());
                }
            });


            loadHistoryTrackingData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initializePanel() {

        try {

            indicator = (ImageView) convertView.findViewById(R.id.indicator);

            donutProgressengine_load_indicator = (DonutProgress) convertView.findViewById(R.id.engine_load_indicator);
            donutProgressengine_load_indicator.setMax(100);
            donutProgressengine_load_indicator.setProgress(0);
            donutProgressengine_load_indicator.setTextSize(40);
            donutProgressengine_load_indicator.setTextColor(Color.WHITE);
            donutProgressengine_load_indicator.setSuffixText("");
            donutProgressengine_load_indicator.setFinishedStrokeColor(getResources().getColor(R.color.green));
            donutProgressengine_load_indicator.setFinishedStrokeWidth(10);
            donutProgressengine_load_indicator.setUnfinishedStrokeWidth(10);


            panelLayout = (SlidingUpPanelLayout) convertView.findViewById(R.id.sliding_layout);
            panelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
                @Override
                public void onPanelSlide(View panel, float slideOffset) {
                    //Log.e(TAG, "onPanelSlide, offset " + slideOffset);
                }

                @Override
                public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

                    try {
                        indicator.setImageResource(
                                (newState == SlidingUpPanelLayout.PanelState.COLLAPSED)
                                        ? R.mipmap.ic_arrow_drop_up_white_24dp
                                        : R.mipmap.ic_arrow_drop_down_white_24dp);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            panelLayout.setFadeOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void refreshFragment() {
    }


    private void loadHistoryTrackingData() {
        try {

            dialog.show();
            routeList.clear();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        String res = trip_demo_string;

                        System.out.println("LocationData:" + res);
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.has("valid")) {
                            String valid = jsonObject.getString("valid");
                            if (valid.equalsIgnoreCase("true")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("object");
                                if (jsonArray.length() > 0) {

                                    for (int y = 0; y < jsonArray.length(); y++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(y);
                                        JSONArray jsonArray_trip = jsonObject1.getJSONArray("trips");

                                        if (jsonArray_trip.length() > 0) {

                                            for (int j = 0; j < jsonArray_trip.length(); j++) {
                                                JSONArray jsonArray1 = jsonArray_trip.getJSONArray(j);
                                                if (jsonArray1 != null && jsonArray1.length() > 0) {

                                                    ArrayList<LocationPoints> route = new ArrayList<LocationPoints>();

                                                    double milage_in_route = Math.random() * (15.5 - 7.5) + 7.5;;

                                                    for (int i = 0; i < jsonArray1.length(); i++) {

                                                            JSONObject jsonObject2 = jsonArray1.getJSONObject(i);

                                                            double lat = 0;
                                                            double lng = 0;
                                                            if (jsonObject2.has("latitude")) {
                                                                lat = Double.parseDouble(jsonObject2.getString("latitude"));
                                                            }
                                                            if (jsonObject2.has("longitude")) {
                                                                lng = Double.parseDouble(jsonObject2.getString("longitude"));
                                                            }
                                                            LocationPoints locationPoints = new LocationPoints();

                                                            if (jsonObject2.has("address")) {
                                                                locationPoints.address = jsonObject2.getString("address");
                                                            }

                                                            if (jsonObject2.has("sys_timestamp")) {
                                                                locationPoints.logTime = jsonObject2.getString("sys_timestamp");
                                                            }
                                                            if (jsonObject2.has("distance")) {
                                                                locationPoints.distance = jsonObject2.getString("distance");
                                                            }

                                                            locationPoints.milage =milage_in_route+"";

                                                            route.add(locationPoints);
                                                    }

                                                    routeList.add(route);

                                                }
                                            }

                                        }
                                    }
                                }
                            } else {
                                showToastFromBgThread("Error in getting history location data,Please refresh.");
                            }
                        } else {
                            showToastFromBgThread("Error in getting history location data,Please refresh.");
                        }
                    } catch (Exception e) {
                        showToastFromBgThread("Error in data parsing.Please try again.");
                    } finally {
                        try {
                            dialog.dismiss();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    try {

                                        Collections.reverse(routeList);
                                            for (int i = 0; i < routeList.size(); i++) {
                                                ArrayList<LocationPoints> latLngs = routeList.get(i);
                                            }
                                            initTripList();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            showToastFromBgThread("Error in plotting history location data,Please refresh.");
                        }

                    }


                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
        }
    }

    private void showToastFromBgThread(final String message) {

        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openPicker(String tag, String current) {
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(formatWithoutTime.parse(current));
            DatePickerDialog.newInstance(this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
                    .show(getActivity().getFragmentManager(), tag);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String pattern = dayOfMonth + " " + GeneralUtilities.getMonth(monthOfYear) + " " + year;
        try {
            switch (view.getTag()) {
                case prefixFrom:
                    to = formatWithoutTime.parse(txtToDate.getText().toString());
                    from = formatWithoutTime.parse(pattern);
                    txtFromDate.setText(pattern);
                    break;
                case prefixTo:
                    to = formatWithoutTime.parse(pattern);
                    from = formatWithoutTime.parse(txtFromDate.getText().toString());
                    txtToDate.setText(pattern);
                    break;
            }
            to.setHours(23);
            to.setMinutes(59);
            to.setSeconds(59);
            if (from.before(to)) {

                long difference = to.getTime() - from.getTime();
                long diffHours = difference / (60 * 60 * 1000);
                /*if (diffHours > 24 * 2) {
                    Toast.makeText(getActivity(), "Invalid date range.Keep difference of one day only.", Toast.LENGTH_SHORT).show();
                } else {
                    loadHistoryTrackingData(formatWithTime.format(from), formatWithTime.format(to));
                }*/
                loadHistoryTrackingData();

            } else {
                Toast.makeText(getActivity(), "Invalid Dates! Please review dates again.", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    private void initTripList() {


        ListView listView = (ListView) convertView.findViewById(R.id.list);
        //Set Trip Related Values in Header
        if (routeList.size() > 0) {
            try {

                donutProgressengine_load_indicator.setProgress(GeneralUtilities.generateRandomNumber(65, 85));
                ((TextView) convertView.findViewById(R.id.txt_trips_count)).setText(routeList.size() + "");

                //Calculating total distance in all Trips between selected date range
                float total_distance = 0;
                float total_Avg_milage = 0;
                for (int i = 0; i < routeList.size(); i++) {
                    try {
                        total_distance = total_distance + Float.parseFloat(routeList.get(i).get(0).distance);
                        total_Avg_milage= total_Avg_milage +Float.parseFloat(routeList.get(i).get(0).milage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //Set All Trips Total Distance
                ((TextView) convertView.findViewById(R.id.txt_trips_total_km)).setText(String.format("%.2f", total_distance) + "");
                //Set All Trips Avg Milage
                ((TextView) convertView.findViewById(R.id.txt_trip_kmpl)).setText(String.format("%.2f", total_Avg_milage/routeList.size())+"");

                /*Calculate Total Duration of All Trips in between given Date range*/
                long total_trip_travel_time_in_millis = 0;
                for (int i = 0; i < routeList.size(); i++) {
                    ArrayList<LocationPoints> locationPointses = routeList.get(i);
                    String fromDate_String = locationPointses.get(0).logTime;
                    String toDate_String = locationPointses.get(locationPointses.size() - 1).logTime;


                    System.out.println("fromDate_String :"+fromDate_String+" toDate_String:"+toDate_String);

                    long fromDate_in_millis = GeneralUtilities.convertDateStringToMilliseconds(fromDate_String, "yyyy-MM-dd HH:mm:ss");
                    long toDate_in_millis = GeneralUtilities.convertDateStringToMilliseconds(toDate_String, "yyyy-MM-dd HH:mm:ss");

                    /*long fromDate_in_millis = Long.parseLong(fromDate_String);
                    long toDate_in_millis = Long.parseLong(toDate_String);*/

                    System.out.println("fromDate_String long :"+fromDate_in_millis+" toDate_String:"+toDate_in_millis);

                    if (toDate_in_millis >= fromDate_in_millis) {
                        total_trip_travel_time_in_millis = total_trip_travel_time_in_millis + (toDate_in_millis - fromDate_in_millis);
                    }
                }
                try {
                    int minutes = (int) ((total_trip_travel_time_in_millis / (1000 * 60)) % 60);
                    int hours = (int) ((total_trip_travel_time_in_millis / (1000 * 60 * 60)) % 24);


                    if (minutes < 10)
                        ((TextView) convertView.findViewById(R.id.txt_trip_hours)).setText(hours + ":0" + minutes);
                    else
                        ((TextView) convertView.findViewById(R.id.txt_trip_hours)).setText(hours + ":" + minutes);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }


                convertView.findViewById(R.id.view_empty_content).setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            donutProgressengine_load_indicator.setProgress(0);
            ((TextView) convertView.findViewById(R.id.txt_trips_count)).setText("0");
            ((TextView) convertView.findViewById(R.id.txt_trips_total_km)).setText("0");
            ((TextView) convertView.findViewById(R.id.txt_trip_kmpl)).setText("0");
            ((TextView) convertView.findViewById(R.id.txt_trip_hours)).setText("0");

            convertView.findViewById(R.id.view_empty_content).setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        tripAddapter = new TripAddapter();
        listView.setAdapter(tripAddapter);
    }



    int total_distance_covered = 0;








    class TripAddapter extends BaseAdapter {
        private int selectedPosition = -1;
        ViewHolder lastSelectedViewHolder;

        @Override
        public int getCount() {
            return routeList.size();
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.trip_summery_layout, null);
                viewHolder.txt_a = (TextView) convertView.findViewById(R.id.txt_a);
                viewHolder.txt_b = (TextView) convertView.findViewById(R.id.txt_b);
                viewHolder.txt_from_date = (TextView) convertView.findViewById(R.id.txt_from_date);
                viewHolder.txt_to_date = (TextView) convertView.findViewById(R.id.txt_to_date);
                viewHolder.txt_from_place = (TextView) convertView.findViewById(R.id.txt_from_place);
                viewHolder.txt_to_place = (TextView) convertView.findViewById(R.id.txt_to_place);
                viewHolder.txt_duration = (TextView) convertView.findViewById(R.id.txt_duration);
                viewHolder.graph_view = convertView.findViewById(R.id.graph_view);
                viewHolder.txt_trip_distance = (TextView) convertView.findViewById(R.id.txt_trip_distance);
                viewHolder.milage = (TextView) convertView.findViewById(R.id.txt_milage);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final ArrayList<LocationPoints> locationPointses = routeList.get(position);


            viewHolder.txt_from_place.setText(locationPointses.get(0).address);
            viewHolder.txt_to_place.setText(locationPointses.get(locationPointses.size() - 1).address);

            //set formated From Date
            String fromDate = locationPointses.get(0).logTime;
            String fromDate_milliseconds = GeneralUtilities.convertDateStringToMilliseconds(fromDate,"yyyy-MM-dd HH:mm:ss")+"";
            fromDate = GeneralUtilities.format_DateString_From_Millisecond_To_Another_Pattern(fromDate_milliseconds, "hh:mm aaa");
            viewHolder.txt_from_date.setText(fromDate);

            //set formated To Date
            String toDate = locationPointses.get(locationPointses.size() - 1).logTime;
            String toDate_milliseconds = GeneralUtilities.convertDateStringToMilliseconds(toDate,"yyyy-MM-dd HH:mm:ss")+"";
            toDate = GeneralUtilities.format_DateString_From_Millisecond_To_Another_Pattern(toDate_milliseconds, "hh:mm aaa") + "\n" + GeneralUtilities.format_DateString_From_Millisecond_To_Another_Pattern(toDate_milliseconds, "MMM dd");
            viewHolder.txt_to_date.setText(toDate);

            String duration = GeneralUtilities.getTimeOffset(fromDate_milliseconds,toDate_milliseconds);
            viewHolder.txt_duration.setText(duration);
            viewHolder.txt_trip_distance.setText(locationPointses.get(0).distance);
            viewHolder.milage.setText(String.format("%.1f", Float.parseFloat(locationPointses.get(0).milage)));
            
            if (selectedPosition == position) {
                viewHolder.txt_a.setBackgroundResource(R.drawable.round_corner_selected);
                viewHolder.txt_b.setBackgroundResource(R.drawable.round_corner_selected);
            } else {
                viewHolder.txt_a.setBackgroundResource(R.drawable.round_corner);
                viewHolder.txt_b.setBackgroundResource(R.drawable.round_corner);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lastSelectedViewHolder != null) {
                        lastSelectedViewHolder.txt_a.setBackgroundResource(R.drawable.round_corner);
                        lastSelectedViewHolder.txt_b.setBackgroundResource(R.drawable.round_corner);
                    }
                    viewHolder.txt_a.setBackgroundResource(R.drawable.round_corner_selected);
                    viewHolder.txt_b.setBackgroundResource(R.drawable.round_corner_selected);
                    lastSelectedViewHolder = viewHolder;
                    selectedPosition = position;
                    panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

                    //Highlight Selected trip path on map through different color
                    try {

                        //Set Default path color to previously selected path


                    } catch (Exception e) {
//                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });

            viewHolder.graph_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Start Analytic Screen
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView txt_a;
            TextView txt_b;
            TextView txt_to_date;
            TextView txt_from_date;
            TextView txt_to_place, txt_from_place;
            TextView txt_duration;
            TextView txt_trip_distance;
            TextView milage;

            View graph_view;

        }
    }
}
