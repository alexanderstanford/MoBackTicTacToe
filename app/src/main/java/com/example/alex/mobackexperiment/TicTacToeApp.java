package com.example.alex.mobackexperiment;

import android.app.Application;

import com.moback.android.MoBack;

/**
 * Created by marks on 8/7/14.
 */
public class TicTacToeApp extends Application {

    public static final String APP_KEY      = "MDQwNGNkMmYtZjI2Yi00ODExLTgyY2EtNjM2NDc2NzY5OWVm";
    public static final String DEV_KEY      = "MTRjZTc1MjYtOGE3NS00NjViLThhZWMtOGNlMmIxNDk1YTdi";
    public static final String PROD_KEY     = "OTY2NTE5OGItNTlhYS00ZmFhLTgwMDYtOTA2MmEyMDEwNWZj";


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
