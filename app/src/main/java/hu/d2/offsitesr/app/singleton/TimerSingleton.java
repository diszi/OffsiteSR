package hu.d2.offsitesr.app.singleton;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import hu.d2.offsitesr.ui.view.login.LoginActivity;

/**
 * Created by szidonia.laszlo on 2017. 11. 23..
 *
 * Methods in this class check user interaction in activity/screen using timer / counter
 */

public class TimerSingleton {

    private static final TimerSingleton ourInstance = new TimerSingleton();
    private Timer timer;
    private Boolean flag;

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


    /**
     *  Terminates the timer, discarding any currently scheduled tasks
     *  & remove all cancelled task from this timer's task queue
     */
    private void timerStop(){
        Log.d("------------------>","Timer STOP");
        timer.cancel();
        timer.purge();
    }

    /**
     *  The timer starts the counter.
     *  The user set the timeout value in the settings menu.
     *  TimerTask object created -> schedules the specified task
     *  for execution after the specified delay (delay = timeout value)
     */
    private void timerStart(){
        Log.d("------------------>","Timer START");
        String timeOut =  SettingsSingleton.getInstance().getTimeOutValue();
        Integer timeOutInt = Integer.parseInt(timeOut);
        Integer timeOutInMilisecond = timeOutInt*1000*60;
        timer=null;
        timer = new Timer();
        LogOutTimerTask logOutTimerTask = new LogOutTimerTask();
        timer.schedule(logOutTimerTask,timeOutInMilisecond);
    }


    /**
     *  Reset timer when there is a user interaction on screen while
     *  application is running  (if the flag is false).
     *
     *  Flag in this method is a Boolean object, the default value is false.
     *  The value changes when the TimerTask method is running (flag = true).
     *  In this case, the resetTimer method doesn't start a new Timer
     */
    public void resetTimer(){
        synchronized (flag){
            if (!flag){
                timerStop();
                timerStart();
            }
        }
    }

    /**
     *
     * @param activity - actual activity where the user is
     * Initialize flag with the default value (false) and start a timer.
     */
    public void initAndStartTimer(Activity activity){
        this.flag = false;
        setMyActivity(activity);
        timerStart();
    }

    /**
     *  TimerTask class - sets the flag value at true when the method is running at the first time
     *  If the flag is true, the resetTimer method doesn't start a new Timer + user is logged out
     *  */
    private class LogOutTimerTask extends TimerTask {

        @Override
        public void run() {
            synchronized (flag){
                flag = true;
            }
            Intent intent = new Intent(getMyActivity(),LoginActivity.class);
            getMyActivity().startActivity(intent);
            getMyActivity().finish();
        }

    }


}
