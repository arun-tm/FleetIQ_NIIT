package com.teramatrix.demo;

import android.app.Application;
import android.content.Intent;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;

/**
 * Created by arun.singh on 7/1/2016.
 */
public class Flint extends Application {


    public void onCreate() {
        super.onCreate();
        makeActionOverflowMenuShown();

        // Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                handleUncaughtException(thread, e);
            }
        });
    }

    public void handleUncaughtException (Thread thread, Throwable e)
    {
        e.printStackTrace(); // not all Android versions will print the stack trace automatically
        Intent intent = new Intent (this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
        startActivity(intent);
        System.exit(1); // kill off the crashed app
    }
    /**
     * Sometimes it happens that option menu will not visible on the action bar overflow.
     * Those menus can be achieved by clicking on hardware button for options.
     * But our system requirements are to show these option menus in action bar overflow(three dots)
     * This method will do this.
     */
    private void makeActionOverflowMenuShown() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
