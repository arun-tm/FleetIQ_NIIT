package com.teramatrix.demo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.telecom.Call;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

import com.teramatrix.demo.Utils.GeneralUtilities;
import com.teramatrix.demo.Utils.ImageUtils;
import com.teramatrix.demo.Utils.MyAnnotation;
import com.teramatrix.demo.Utils.SPUtils;
import com.teramatrix.demo.Utils.TextViewAnnotation;
import dmax.dialog.SpotsDialog;

public class Login extends AppCompatActivity {


    MyAnnotation myAnnotation = new MyAnnotation();


    private AppCompatEditText edPlace;
    private AppCompatEditText edPassword;

    @TextViewAnnotation(R.id.btnGo)
    public Button btnGo;

    private AlertDialog dialog;
    private String imei_number;
    private static final int REQUEST_IMEI_PERMISSION = 1111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            myAnnotation.bind(this);



            //Blur background Image
            ImageView imageViewBg = (ImageView) findViewById(R.id.img_bg);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.truck_bg2);
            bitmap = ImageUtils.getBlurredImage(bitmap, 0.2f, 10);
            Drawable d = new BitmapDrawable(getResources(), bitmap);
            imageViewBg.setBackground(d);

            initViews();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SPUtils spUtils = new SPUtils(Login.this);
                    String token = spUtils.getToken();

                    if (token != null && token.length() > 0) {
                        //redirect to Home Screen
                        startActivity(new Intent(Login.this, MainActivity.class));
                        finish();
                    } else {
                        //show login form

                        findViewById(R.id.img_fleet_iq_icon).animate().setDuration(500).translationY(-400);
                        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                        alphaAnimation.setDuration(1500);
                        alphaAnimation.setStartOffset(0);
                        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                findViewById(R.id.form_parent).setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                        findViewById(R.id.form_parent).startAnimation(alphaAnimation);
                    }

                }
            }, 2000);


            requestImei();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void requestImei() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_IMEI_PERMISSION);
            } else {
                //Updating IMEI Number in shared preferences
                imei_number = GeneralUtilities.getDeviceImei(Login.this);

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_IMEI_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //Updating IMEI Number in shared preferences
            imei_number = GeneralUtilities.getDeviceImei(Login.this);
        }
    }

    private void initViews() {
        edPlace = (AppCompatEditText) findViewById(R.id.edPlace);
        edPassword = (AppCompatEditText) findViewById(R.id.edPassword);

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });

        dialog = new SpotsDialog(this, R.style.Custom);
    }

    private boolean validateFormValues(String username, String password) {
        if (username.length() == 0) {
            edPlace.setError("Please enter username");
            return false;
        } else if (password.toString().length() == 0) {
            edPassword.setError("Please enter password");
            return false;
        }else if (!username.equalsIgnoreCase("obd@teramatrix.in") && !username.equalsIgnoreCase("niit@teramatrix.in"))
        {
            edPlace.setError("Please enter correct username");
            return false;
        }else if (!password.toString().equalsIgnoreCase("Admin@123"))
        {
            edPassword.setError("Please enter correct password");
            return false;
        }

        return true;
    }

    private void login() {



        final String username = edPlace.getText().toString();
        final String password = edPassword.getText().toString();

        //validate form values for incorract values
        if (!validateFormValues(username, password))
            return;


        SPUtils spUtils = new SPUtils(Login.this);
        spUtils.setValue(SPUtils.ACCESS_TOKEN, "123456789");
        spUtils.setValue(SPUtils.ACCESS_KEY, "123456789");
        spUtils.setValue(SPUtils.USER_KEY, "123456789");
        spUtils.setValue(SPUtils.USER_ID, "123456789");



        startActivity(new Intent(Login.this, MainActivity.class));
        finish();


    }
}
