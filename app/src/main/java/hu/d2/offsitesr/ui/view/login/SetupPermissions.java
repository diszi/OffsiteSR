package hu.d2.offsitesr.ui.view.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import hu.d2.offsitesr.R;

/**
 * Created by szidonia.laszlo on 2017. 12. 07..
 */

public class SetupPermissions extends AbsRuntimePermission {

    public static Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mContext=getApplicationContext();

        requestAppPermissions(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE}, R.string.app_permissionMsg,10);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);

    }
}
