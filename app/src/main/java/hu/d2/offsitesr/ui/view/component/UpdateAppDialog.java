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
import java.util.Date;

import hu.d2.offsitesr.R;
import hu.d2.offsitesr.app.singleton.SettingsSingleton;
import hu.d2.offsitesr.ui.view.login.LoginActivity;
import hu.d2.offsitesr.ui.view.ticketlist.TicketListActivity;
import hu.d2.offsitesr.ui.view.verifications.UpdateApp;
import hu.d2.offsitesr.util.EnvironmentTool;
import hu.d2.offsitesr.util.UIConstans;


/**
 * Created by szidonia.laszlo on 2017. 12. 08..
 */

public  class UpdateAppDialog extends DialogFragment {


    public Date datePressNoBtn;
    public String temp, newVersionNr;
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
     * @param temp - activity identificator (UpdateActivity / TicketListActivity)
     * @param versionNr - available new version number
     * @return an instance of UpdateAppDialog class
     */
    public static UpdateAppDialog newInstance(String temp,String versionNr){
        UpdateAppDialog updateAppDialog = new UpdateAppDialog();
        Bundle args = new Bundle();
        args.putString("temp",temp);
        args.putString("versionNr",versionNr);
        updateAppDialog.setArguments(args);
        return updateAppDialog;
    }

    /**
     *   - display question (New version is available! Would you like to update the app?) in dialog with 2 opt.: Yes / No
     *   - Yes: verify internet connection  ->  update app
     *   - No: save actual date in property file -> display ticket list
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        UpdateApp updateApp = (UpdateApp)getActivity();
        Activity activity = getActivity();
        temp = getArguments().getString("temp");
        newVersionNr = getArguments().getString("versionNr");

            return new AlertDialog.Builder(getActivity()).setMessage(getString(R.string.dialogUpdateApp_message)+"\n \n"+getString(R.string.dialogUpdateApp_versionNr)+newVersionNr).setPositiveButton(R.string.buttonYES, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    final AlertDialog alertDialog  = new AlertDialog.Builder(activity).create();

                    //ONLINE + WIFI
                    if (isConnectedWifi()){
                        updateApp.downloadApp();
                    }
                        //ONLINE + MobileNET
                    if (isConnectedMobileNet()){
                        alertDialog.setMessage(getString(R.string.dialogUpdateApp_mobilNetQuestion));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.buttonYES), new DialogInterface.OnClickListener() {

                            //update app using mobile net
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updateApp.downloadApp();
                            }
                        });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.buttonNO), new DialogInterface.OnClickListener() {
                            //don't update app
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                intent = new Intent(activity,LoginActivity.class);
                                activity.startActivity(intent);
                            }
                        });
                        alertDialog.show();

                    }
                        //OFFLINE
                    if (!isOnline()) {
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

                // the user doesn't want to update the app -> loading ticket list + save actual date in property file
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    intent = new Intent(getActivity(), TicketListActivity.class);
                    startActivity(intent);

                    datePressNoBtn = new Date();
                    setDatePressNoBtn(datePressNoBtn);

                    SettingsSingleton.getInstance().getSharedPreferences().edit().putString(getString(R.string.sharedPreference_getDate), EnvironmentTool.convertDate(datePressNoBtn, UIConstans.DATE_PATTERN_HU)).commit();
                    SettingsSingleton.getInstance().getDate();
                }
            }).create();
    }

    /**
     *
     * @return true, if there is a WIFI connection
     */
    public boolean isConnectedWifi(){
        WifiManager wifiManager = (WifiManager)getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        boolean wifiEnabled = wifiManager.isWifiEnabled();
        return wifiEnabled;
    }

    /**
     *
     * @return true, if there is mobile network connection
     */
    public boolean isConnectedMobileNet(){
        ConnectivityManager cm = (ConnectivityManager)getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /**
     *
     * @return true, if there is internet connection (Wifi, mobile net, et.)
     */
    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager)getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
