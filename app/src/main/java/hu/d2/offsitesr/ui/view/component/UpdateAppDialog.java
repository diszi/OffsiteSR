package hu.d2.offsitesr.ui.view.component;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import hu.d2.offsitesr.R;
import hu.d2.offsitesr.app.singleton.SettingsSingleton;
import hu.d2.offsitesr.ui.view.login.LoginActivity;
import hu.d2.offsitesr.ui.view.ticketlist.TicketListActivity;
import hu.d2.offsitesr.ui.view.verifications.UpdateActivity;
import hu.d2.offsitesr.util.EnvironmentTool;


/**
 * Created by szidonia.laszlo on 2017. 12. 08..
 */

public class UpdateAppDialog extends DialogFragment {


    private UpdateActivity updateActivity = new UpdateActivity();
    public Date datePressNoBtn;
    Intent intent;

    public void setDatePressNoBtn(Date date)
    {
        this.datePressNoBtn = date;
    }

    public Date getDatePressNoBtn()
    {
        return datePressNoBtn;
    }



    /**
     *
     *   - show question (New version is available! Would you like to update the app?) in dialog with 2 opt.: Yes / No
     *   - Yes: verify internet connection  -  update app
     *   - No: save actual date in property file - show ticket list
     *
     *
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity()).setMessage(R.string.dialogUpdateApp_message).setPositiveButton(R.string.buttonYES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final AlertDialog alertDialog = new AlertDialog.Builder((UpdateActivity)getActivity()).create();
                Activity activity = getActivity();
                if (isConnectedWifi()){
                    //ONLINE + WIFI
                    ((UpdateActivity)getActivity()).downloadNewApp();


                }else
                    if (isConnectedMobileNet()){
                        //ONLINE + MobileNET
                        alertDialog.setMessage(getString(R.string.dialogUpdateApp_mobilNetQuestion));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.buttonYES), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((UpdateActivity)getActivity()).downloadNewApp();

                            }
                        });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.buttonNO), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                intent = new Intent(activity,LoginActivity.class);
                                activity.startActivity(intent);

                            }
                        });
                        alertDialog.show();

                    }else
                        if (!isOnline()) {
                            //OFFLINE
                          alertDialog.setMessage(getString(R.string.dialogUpdateApp_offlineMode));
                          alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialogLicense_buttonOk), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    intent = new Intent(activity,LoginActivity.class);
                                    activity.startActivity(intent);

                                }
                            });
                          alertDialog.show();
                        }

                }
        }).setNegativeButton(R.string.buttonNO, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                intent = new Intent(getActivity(), TicketListActivity.class);
                startActivity(intent);

                datePressNoBtn = new Date();
                setDatePressNoBtn(datePressNoBtn);
                SettingsSingleton.getInstance().getSharedPreferences().edit().putString(getString(R.string.sharedPreference_getDate), EnvironmentTool.dateInString(datePressNoBtn)).commit();
                SettingsSingleton.getInstance().getDate();
            }
        }).create();

    }

    public boolean isConnectedWifi(){
        WifiManager wifiManager = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);
        boolean wifiEnabled = wifiManager.isWifiEnabled();
        return wifiEnabled;
    }

    public boolean isConnectedMobileNet(){
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
