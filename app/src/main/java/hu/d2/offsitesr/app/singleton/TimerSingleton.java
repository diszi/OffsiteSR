package hu.d2.offsitesr.app.singleton;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import hu.d2.offsitesr.ui.view.login.LoginActivity;

/**
 * Created by szidonia.laszlo on 2017. 11. 23..
 */

public class TimerSingleton {

    private static final TimerSingleton ourInstance = new TimerSingleton();
    private Timer timer = new Timer();


    private Activity myActivity;

    public static TimerSingleton getInstance() {
        return ourInstance;
    }

    private TimerSingleton() {
    }


    public Activity getMyActivity() {
        return myActivity;
    }

    public void setMyActivity(Activity myActivity) {
        this.myActivity = myActivity;
    }


    public void timerStop(){
        Log.d("------------------>","Timer STOP");
        timer.cancel();
        timer.purge();

    }

    public void timerStart(){
        Log.d("------------------>","Timer START");
        String timeOut =  SettingsSingleton.getInstance().getTimeOutValue();
        Integer timeOutInt = Integer.parseInt(timeOut);
        Integer timeOutInMilisecond = timeOutInt*1000*60;
        timer = new Timer();
        LogOutTimerTask logOutTimerTask = new LogOutTimerTask();
        timer.schedule(logOutTimerTask,timeOutInMilisecond);
    }



    private class LogOutTimerTask extends TimerTask {

        @Override
        public void run() {
            Intent intent = new Intent(getMyActivity(),LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            getMyActivity().startActivity(intent);
            getMyActivity().finish();


        }
    }




}
