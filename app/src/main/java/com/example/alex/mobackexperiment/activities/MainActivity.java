package com.example.alex.mobackexperiment.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.mobackexperiment.R;
import com.example.alex.mobackexperiment.TicTacToeApp;
import com.moback.android.MoBack;
import com.moback.android.SSOManager;

import java.util.Random;


public class MainActivity extends Activity implements View.OnClickListener {

    static int screenSizeX = 0;
    static int screenSizeY = 0;
    static int[][] ticArray;
    static int ticNumber = 0, tacNumber = 0, wins = 0, losses = 0;
    static String won = "0";
    static String lost = "0";
    boolean gameover = false, checker = false;

    Handler compTurnHandler = new Handler();
    private boolean mIsYourTurn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MoBack.setApplicationKeys(this, TicTacToeApp.APP_KEY, TicTacToeApp.DEV_KEY);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics dm1 = getResources().getDisplayMetrics();
        screenSizeX = dm1.widthPixels / 3;
        screenSizeY = dm1.heightPixels / 3;
        ticArray = new int[3][3];
        adjustScreen();
        initializeTicArray();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        ((TextView)findViewById(R.id.tic1)).setOnClickListener(this);
        ((TextView)findViewById(R.id.tic2)).setOnClickListener(this);
        ((TextView)findViewById(R.id.tic3)).setOnClickListener(this);
        ((TextView)findViewById(R.id.tic4)).setOnClickListener(this);
        ((TextView)findViewById(R.id.tic5)).setOnClickListener(this);
        ((TextView)findViewById(R.id.tic6)).setOnClickListener(this);
        ((TextView)findViewById(R.id.tic7)).setOnClickListener(this);
        ((TextView)findViewById(R.id.tic8)).setOnClickListener(this);
        ((TextView)findViewById(R.id.tic9)).setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id){
            case R.id.action_settings:
                resetTic();
                break;
            case R.id.action_logout:
                if(SSOManager.isUserLoggedIn()) {
                    SSOManager.logoutUser();
                }
                Intent mIntent = new Intent(this, LoginActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mIntent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!mIsYourTurn) {
            return false;
        }
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(!gameover) {
                    clicked(x);
                }
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
        }
        return false;
    }*/

    public void clicked(int x) {
        if(mIsYourTurn) {
            if (x == 1) {
                if (!gameover) {
                    ticNumber = 0;
                    tacNumber = 0;
                    ticLogic();
                }
            }
            if (x == 2) {
                if (!gameover) {
                    ticNumber = 1;
                    tacNumber = 0;
                    ticLogic();
                }
            }
            if (x == 3) {
                if (!gameover) {
                    ticNumber = 2;
                    tacNumber = 0;
                    ticLogic();
                }
            }
            if (x == 4) {
                if (!gameover) {
                    ticNumber = 0;
                    tacNumber = 1;
                    ticLogic();
                }
            }
            if (x == 5) {
                if (!gameover) {
                    ticNumber = 1;
                    tacNumber = 1;
                    ticLogic();
                }
            }
            if (x == 6) {
                if (!gameover) {
                    ticNumber = 2;
                    tacNumber = 1;
                    ticLogic();
                }
            }
            if (x == 7) {
                if (!gameover) {
                    ticNumber = 0;
                    tacNumber = 2;
                    ticLogic();
                }
            }
            if (x == 8) {
                if (!gameover) {
                    ticNumber = 1;
                    tacNumber = 2;
                    ticLogic();
                }
            }
            if (x == 9) {
                if (!gameover) {
                    ticNumber = 2;
                    tacNumber = 2;
                    ticLogic();
                }
            }
        }
    }

    public void initializeTicArray() {
        ticArray[0][0] = 0;
        ticArray[0][1] = 0;
        ticArray[0][2] = 0;
        ticArray[1][0] = 0;
        ticArray[1][1] = 0;
        ticArray[1][2] = 0;
        ticArray[2][0] = 0;
        ticArray[2][1] = 0;
        ticArray[2][2] = 0;
    }

    public void ticLogic() {
        if(ticArray[ticNumber][tacNumber] == 0) {
            ticArray[ticNumber][tacNumber] = 1;
            mIsYourTurn = false;
            checkYourWin();
            updateScreen();
            //Delay comp turn by 500 ms
            compTurnHandler.postDelayed(new Runnable(){
                @Override
                public void run(){
                    compTurn();
                    checkCompWin();
                    updateScreen();
                    checkTie();
                    mIsYourTurn = true;
                }
            }, 500);
        }
        else {
            Toast.makeText(this, "Invalid Choice", Toast.LENGTH_SHORT).show();
        }
    }

    public void compTurn() {
        if(!mIsYourTurn) {
            if (!gameover) {
                checker = false;
                int x = 0;
                int y = 0;

                while (x != 3) {
                    while (y != 3) {
                        if (ticArray[x][y] != 1 && ticArray[x][y] != -1) {
                            checker = true;
                        }
                        y = y + 1;
                    }
                    x = x + 1;
                    y = 0;
                }

                doNotOpen();

                while (checker) {
                    Random rand = new Random();
                    x = rand.nextInt(3);
                    y = rand.nextInt(3);
                    if (ticArray[x][y] != 1 && ticArray[x][y] != -1) {
                        ticArray[x][y] = -1;
                        checker = false;
                    }

                }
            }
        }
    }

    public void adjustScreen() {
        int textSizeAdjust = screenSizeX * 3;
        textSizeAdjust = textSizeAdjust / 4;
        int z = 1;

        while(z != 10) {
            int id = getResources().getIdentifier("tic" + Integer.toString(z), "id", MainActivity.this.getPackageName());
            TextView tv = (TextView) findViewById(id);
            tv.setTextSize(textSizeAdjust);
            z = z + 1;
        }

    }

    public void updateScreen () {
        int x = 0;
        int y = 0;
        int z = 1;

        while(y != 3) {
            while(x != 3) {
                if (ticArray[x][y] == 0) {
                    int id = getResources().getIdentifier("tic" + Integer.toString(z), "id", MainActivity.this.getPackageName());
                    TextView tv = (TextView) findViewById(id);
                    tv.setText("-");
                }
                if (ticArray[x][y] == 1) {
                    int id = getResources().getIdentifier("tic" + Integer.toString(z), "id", MainActivity.this.getPackageName());
                    TextView tv = (TextView) findViewById(id);
                    tv.setText("X");
                }
                if (ticArray[x][y] == -1) {
                    int id = getResources().getIdentifier("tic" + Integer.toString(z), "id", MainActivity.this.getPackageName());
                    TextView tv = (TextView) findViewById(id);
                    tv.setText("O");
                }
                z = z + 1;
                x = x + 1;
            }
            y = y + 1;
            x = 0;
        }

    }

    public void resetTic() {
        int x = 0;
        int y = 0;
        mIsYourTurn = true;

        while(x != 3) {
            while(y != 3) {
                ticArray[x][y] = 0;
                y = y + 1;
            }
            x = x + 1;
            y = 0;
        }
        updateScreen();
        gameover = false;
        Toast.makeText(this, "Game Resest", Toast.LENGTH_SHORT).show();
    }

    public void checkYourWin() {
         if (ticArray[0][0] == 1 && ticArray[1][0] == 1 && ticArray[2][0] == 1) win();
         else if (ticArray[0][1] == 1 && ticArray[1][1] == 1 && ticArray[2][1] == 1) win();
         else if (ticArray[0][2] == 1 && ticArray[1][2] == 1 && ticArray[2][2] == 1) win();
         else if (ticArray[0][0] == 1 && ticArray[0][1] == 1 && ticArray[0][2] == 1) win();
         else if (ticArray[1][0] == 1 && ticArray[1][1] == 1 && ticArray[1][2] == 1) win();
         else if (ticArray[2][0] == 1 && ticArray[2][1] == 1 && ticArray[2][2] == 1) win();
         else if (ticArray[0][0] == 1 && ticArray[1][1] == 1 && ticArray[2][2] == 1) win();
         else if (ticArray[2][0] == 1 && ticArray[1][1] == 1 && ticArray[0][2] == 1) win();

    }

    public void checkCompWin() {
        if (ticArray[0][0] == -1 && ticArray[1][0] == -1 && ticArray[2][0] == -1) loss();
        else if (ticArray[0][1] == -1 && ticArray[1][1] == -1 && ticArray[2][1] == -1) loss();
        else if (ticArray[0][2] == -1 && ticArray[1][2] == -1 && ticArray[2][2] == -1) loss();
        else if (ticArray[0][0] == -1 && ticArray[0][1] == -1 && ticArray[0][2] == -1) loss();
        else if (ticArray[1][0] == -1 && ticArray[1][1] == -1 && ticArray[1][2] == -1) loss();
        else if (ticArray[2][0] == -1 && ticArray[2][1] == -1 && ticArray[2][2] == -1) loss();
        else if (ticArray[0][0] == -1 && ticArray[1][1] == -1 && ticArray[2][2] == -1) loss();
        else if (ticArray[2][0] == -1 && ticArray[1][1] == -1 && ticArray[0][2] == -1) loss();

    }

    public void win() {
        wins = wins + 1;
        gameover = true;
        won = String.valueOf(wins);
        Toast.makeText(this, "You Win", Toast.LENGTH_SHORT).show();
    }

    public void loss() {
        if(!gameover) {
            losses = losses + 1;
            gameover = true;
            lost = String.valueOf(losses);
            Toast.makeText(this, "You Lose", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkTie() {
        if(!gameover) {
            int x = 0;
            int y = 0;
            int z = 0;

            while (x != 3) {
                while (y != 3) {
                    if (ticArray[x][y] != 0) z = z + 1;
                    y = y + 1;
                }
                x = x + 1;
                y = 0;
            }

            if (z == 9) {
                gameover = false;
                Toast.makeText(this, "You've Tied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void doNotOpen() {
        if (ticArray[0][0] == 0 && ticArray[1][0] == -1 && ticArray[2][0] == -1) {
            ticArray[0][0] = -1;
            checker = false;
        }
        else if (ticArray[0][1] == 0 && ticArray[1][1] == -1 && ticArray[2][1] == -1) {
            ticArray[0][1] = -1;
            checker = false;
        }
        else if (ticArray[0][2] == 0 && ticArray[1][2] == -1 && ticArray[2][2] == -1) {
            ticArray[0][2] = -1;
            checker = false;
        }
        else if (ticArray[0][0] == 0 && ticArray[0][1] == -1 && ticArray[0][2] == -1) {
            ticArray[0][0] = -1;
            checker = false;
        }
        else if (ticArray[1][0] == 0 && ticArray[1][1] == -1 && ticArray[1][2] == -1) {
            ticArray[1][0] = -1;
            checker = false;
        }
        else if (ticArray[2][0] == 0 && ticArray[2][1] == -1 && ticArray[2][2] == -1) {
            ticArray[2][0] = -1;
            checker = false;
        }
        else if (ticArray[0][0] == 0 && ticArray[1][1] == -1 && ticArray[2][2] == -1) {
            ticArray[0][0] = -1;
            checker = false;
        }
        else if (ticArray[2][0] == 0 && ticArray[1][1] == -1 && ticArray[0][2] == -1) {
            ticArray[2][0] = -1;
            checker = false;
        }
        else if (ticArray[0][0] == -1 && ticArray[1][0] == 0 && ticArray[2][0] == -1) {
            ticArray[1][0] = -1;
            checker = false;
        }
        else if (ticArray[0][1] == -1 && ticArray[1][1] == 0 && ticArray[2][1] == -1) {
            ticArray[1][1] = -1;
            checker = false;
        }
        else if (ticArray[0][2] == -1 && ticArray[1][2] == 0 && ticArray[2][2] == -1) {
            ticArray[1][2] = -1;
            checker = false;
        }
        else if (ticArray[0][0] == -1 && ticArray[0][1] == 0 && ticArray[0][2] == -1) {
            ticArray[0][1] = -1;
            checker = false;
        }
        else if (ticArray[1][0] == -1 && ticArray[1][1] == 0 && ticArray[1][2] == -1) {
            ticArray[1][1] = -1;
            checker = false;
        }
        else if (ticArray[2][0] == -1 && ticArray[2][1] == 0 && ticArray[2][2] == -1) {
            ticArray[2][1] = -1;
            checker = false;
        }
        else if (ticArray[0][0] == -1 && ticArray[1][1] == 0 && ticArray[2][2] == -1) {
            ticArray[1][1] = -1;
            checker = false;
        }
        else if (ticArray[2][0] == -1 && ticArray[1][1] == 0 && ticArray[0][2] == -1) {
            ticArray[1][1] = -1;
            checker = false;
        }
        else if (ticArray[0][0] == -1 && ticArray[1][0] == -1 && ticArray[2][0] == 0) {
            ticArray[2][0] = -1;
            checker = false;
        }
        else if (ticArray[0][1] == -1 && ticArray[1][1] == -1 && ticArray[2][1] == 0) {
            ticArray[2][1] = -1;
            checker = false;
        }
        else if (ticArray[0][2] == -1 && ticArray[1][2] == -1 && ticArray[2][2] == 0) {
            ticArray[2][2] = -1;
            checker = false;
        }
        else if (ticArray[0][0] == -1 && ticArray[0][1] == -1 && ticArray[0][2] == 0) {
            ticArray[0][2] = -1;
            checker = false;
        }
        else if (ticArray[1][0] == -1 && ticArray[1][1] == -1 && ticArray[1][2] == 0) {
            ticArray[1][2] = -1;
            checker = false;
        }
        else if (ticArray[2][0] == -1 && ticArray[2][1] == -1 && ticArray[2][2] == 0) {
            ticArray[2][2] = -1;
            checker = false;
        }
        else if (ticArray[0][0] == -1 && ticArray[1][1] == -1 && ticArray[2][2] == 0) {
            ticArray[2][2] = -1;
            checker = false;
        }
        else if (ticArray[2][0] == -1 && ticArray[1][1] == -1 && ticArray[0][2] == 0) {
            ticArray[0][2] = -1;
            checker = false;
        }
        else if (ticArray[0][0] == 0 && ticArray[1][0] == 1 && ticArray[2][0] == 1) {
            ticArray[0][0] = -1;
            checker = false;
        }
        else if (ticArray[0][1] == 0 && ticArray[1][1] == 1 && ticArray[2][1] == 1) {
            ticArray[0][1] = -1;
            checker = false;
        }
        else if (ticArray[0][2] == 0 && ticArray[1][2] == 1 && ticArray[2][2] == 1) {
            ticArray[0][2] = -1;
            checker = false;
        }
        else if (ticArray[0][0] == 0 && ticArray[0][1] == 1 && ticArray[0][2] == 1) {
            ticArray[0][0] = -1;
            checker = false;
        }
        else if (ticArray[1][0] == 0 && ticArray[1][1] == 1 && ticArray[1][2] == 1) {
            ticArray[1][0] = -1;
            checker = false;
        }
        else if (ticArray[2][0] == 0 && ticArray[2][1] == 1 && ticArray[2][2] == 1) {
            ticArray[2][0] = -1;
            checker = false;
        }
        else if (ticArray[0][0] == 0 && ticArray[1][1] == 1 && ticArray[2][2] == 1) {
            ticArray[0][0] = -1;
            checker = false;
        }
        else if (ticArray[2][0] == 0 && ticArray[1][1] == 1 && ticArray[0][2] == 1) {
            ticArray[2][0] = -1;
            checker = false;
        }
        else if (ticArray[0][0] == 1 && ticArray[1][0] == 0 && ticArray[2][0] == 1) {
            ticArray[1][0] = -1;
            checker = false;
        }
        else if (ticArray[0][1] == 1 && ticArray[1][1] == 0 && ticArray[2][1] == 1) {
            ticArray[1][1] = -1;
            checker = false;
        }
        else if (ticArray[0][2] == 1 && ticArray[1][2] == 0 && ticArray[2][2] == 1) {
            ticArray[1][2] = -1;
            checker = false;
        }
        else if (ticArray[0][0] == 1 && ticArray[0][1] == 0 && ticArray[0][2] == 1) {
            ticArray[0][1] = -1;
            checker = false;
        }
        else if (ticArray[1][0] == 1 && ticArray[1][1] == 0 && ticArray[1][2] == 1) {
            ticArray[1][1] = -1;
            checker = false;
        }
        else if (ticArray[2][0] == 1 && ticArray[2][1] == 0 && ticArray[2][2] == 1) {
            ticArray[2][1] = -1;
            checker = false;
        }
        else if (ticArray[0][0] == 1 && ticArray[1][1] == 0 && ticArray[2][2] == 1) {
            ticArray[1][1] = -1;
            checker = false;
        }
        else if (ticArray[2][0] == 1 && ticArray[1][1] == 0 && ticArray[0][2] == 1) {
            ticArray[1][1] = -1;
            checker = false;
        }
        else if (ticArray[0][0] == 1 && ticArray[1][0] == 1 && ticArray[2][0] == 0) {
            ticArray[2][0] = -1;
            checker = false;
        }
        else if (ticArray[0][1] == 1 && ticArray[1][1] == 1 && ticArray[2][1] == 0) {
            ticArray[2][1] = -1;
            checker = false;
        }
        else if (ticArray[0][2] == 1 && ticArray[1][2] == 1 && ticArray[2][2] == 0) {
            ticArray[2][2] = -1;
            checker = false;
        }
        else if (ticArray[0][0] == 1 && ticArray[0][1] == 1 && ticArray[0][2] == 0) {
            ticArray[0][2] = -1;
            checker = false;
        }
        else if (ticArray[1][0] == 1 && ticArray[1][1] == 1 && ticArray[1][2] == 0) {
            ticArray[1][2] = -1;
            checker = false;
        }
        else if (ticArray[2][0] == 1 && ticArray[2][1] == 1 && ticArray[2][2] == 0) {
            ticArray[2][2] = -1;
            checker = false;
        }
        else if (ticArray[0][0] == 1 && ticArray[1][1] == 1 && ticArray[2][2] == 0) {
            ticArray[2][2] = -1;
            checker = false;
        }
        else if (ticArray[2][0] == 1 && ticArray[1][1] == 1 && ticArray[0][2] == 0) {
            ticArray[0][2] = -1;
            checker = false;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tic1:
                clicked(1);
                break;
            case R.id.tic2:
                clicked(2);
                break;
            case R.id.tic3:
                clicked(3);
                break;
            case R.id.tic4:
                clicked(4);
                break;
            case R.id.tic5:
                clicked(5);
                break;
            case R.id.tic6:
                clicked(6);
                break;
            case R.id.tic7:
                clicked(7);
                break;
            case R.id.tic8:
                clicked(8);
                break;
            case R.id.tic9:
                clicked(9);
                break;
        }
    }



    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.8f + delta; // perform low-cut filter
            if(mAccel > 10){
                resetTic();
            }
        }
        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {

        }
    };

}
