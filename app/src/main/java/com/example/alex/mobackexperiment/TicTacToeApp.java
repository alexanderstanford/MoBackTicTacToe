package com.example.alex.mobackexperiment;

import android.app.Application;

import com.moback.android.MoBack;

/**
 * Created by marks on 8/7/14.
 */
public class TicTacToeApp extends Application {

    public static final String APP_KEY      = "Y2IyMGQ2NmYtZmY4ZS00NGM5LTkzNWYtODVjZDc4OTM0NWE4";
    public static final String DEV_KEY      = "MDM0ZGE1MDItNGY2Mi00Y2U5LTg2OWMtNDJmNjI2YjdjNDhi";
    public static final String PROD_KEY     = "YjBhNmI4OWUtMWIyNy00Yjc4LWJhNWItZTMyNTQ3ZDgxZjRl";


    /**
     * Called when the application is starting, before any other application
     * objects have been created.  Implementations should be as quick as
     * possible (for example using lazy initialization of state) since the time
     * spent in this function directly impacts the performance of starting the
     * first activity, service, or receiver in a process.
     * If you override this method, be sure to call super.onCreate().
     */
    @Override
    public void onCreate() {
        super.onCreate();

    }

    /**
     * This method is for use in emulated process environments.  It will
     * never be called on a production Android device, where processes are
     * removed by simply killing them; no user code (including this callback)
     * is executed when doing so.
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
