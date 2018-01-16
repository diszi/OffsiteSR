package hu.d2.offsitesr.ui.view.login;

import android.app.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.SparseIntArray;
import android.view.View;

import hu.d2.offsitesr.R;


/**
 * Created by szidonia.laszlo on 2017. 12. 05..
 *
 *  - abstract class - Permissions verification after install app, before login activity
 *
 */

public abstract class AbsRuntimePermission extends Activity {


    private SparseIntArray mErrorString;

    boolean showRequestPermissions;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mErrorString = new SparseIntArray();

    }

    public abstract void onPermissionsGranted(int requestCode);


    public void requestAppPermissions(final String[]requestedPermissions, final int stringId, final int requestCode){

        mErrorString.put(requestCode,stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        showRequestPermissions = false;
        for (String permission : requestedPermissions){
            permissionCheck = permissionCheck+ ContextCompat.checkSelfPermission(this,permission);
            showRequestPermissions = showRequestPermissions || ActivityCompat.shouldShowRequestPermissionRationale(this,permission);

        }


        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (showRequestPermissions) {

                Snackbar.make(findViewById(android.R.id.content), stringId, Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.app_permissionEnable), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(AbsRuntimePermission.this, requestedPermissions, requestCode);

                    }
                }).show();

            } else {

                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        } else {

            onPermissionsGranted(requestCode);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;

        for (int permission : grantResults){
            permissionCheck = permissionCheck + permission;
        }

        if ( (grantResults.length > 0) && PackageManager.PERMISSION_GRANTED == permissionCheck){
            onPermissionsGranted(requestCode);
        }else{
            // display msg when contain some  permission not accept
                Snackbar.make(findViewById(android.R.id.content),mErrorString.get(requestCode),Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.app_permissionEnable), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(AbsRuntimePermission.this, permissions, requestCode);
                    }
            }).show();

        }
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
        super.onBackPressed();
    }
}
