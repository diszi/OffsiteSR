package hu.d2.offsitesr.app;

import android.graphics.Color;
import android.view.View;

import butterknife.BindColor;
import hu.d2.offsitesr.R;

/**
 * Created by szidonia.laszlo on 2018. 02. 12..
 */

public class PropertySettings {

    public static Boolean DEV_MODE = true; // if in DEV mode || or false = if PROD mode
    public static int BACKGROUND_COLOR;


    public static void setMode(){
        if (DEV_MODE){
            CustomerProperties.SERVER_IP_ADDRESS = CustomerProperties.DEV_SERVER_IP_ADDRESS;
            //set color
            BACKGROUND_COLOR = Color.RED;

        }else{
            CustomerProperties.SERVER_IP_ADDRESS = CustomerProperties.PROD_SERVER_IP_ADDRESS;
            BACKGROUND_COLOR = -1;

        }
    }


}

