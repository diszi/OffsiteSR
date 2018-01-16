package hu.d2.offsitesr.ui.view.verifications;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

import java.util.List;

import hu.d2.offsitesr.R;
import hu.d2.offsitesr.ui.model.License;
import hu.d2.offsitesr.ui.view.component.LicenseDialog;
import hu.d2.offsitesr.ui.view.login.LoginActivity;

/**
 * Created by szidonia.laszlo on 2017. 12. 11..
 */

public class LicenseActivity extends AppCompatActivity {

    private VerificationPresenter presenter;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        presenter = new VerificationPresenterImpl();
        presenter.setLicenseView(this);

        presenter.validateLicense(getIMEI());


    }

    @SuppressLint("MissingPermission")
    public String getIMEI() {
        String IMEInumber;
        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= 26) {

            IMEInumber = telManager.getImei();
        }
        else
        {
            IMEInumber=telManager.getDeviceId();
        }
        System.out.println("IMEI = "+IMEInumber);
        return IMEInumber;
    }

    public void startProcess(List<License> licenseList){

        if (licenseList.size()== 0){
            //System.out.println("ERROR");
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(getApplicationContext().getString(R.string.dialogLicenseError_message));
            alert.setNeutralButton(getApplicationContext().getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

            alert.create();
            alert.show();
        }
        else{
            Intent i = new Intent(this,UpdateActivity.class);
            startActivity(i);
        }

    }
}