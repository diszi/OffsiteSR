package hu.d2.offsitesr.app;


import android.util.Log;

import java.util.Set;

import hu.d2.offsitesr.ui.view.login.SetupPermissions;
import hu.d2.offsitesr.util.EnvironmentTool;

/**

 * Created by szidonia.laszlo on 2018. 02. 09..
 */

public class CustomerProperties {

    public static Boolean AUTHENTICATION_NATIVE = true; //D2
    public static Boolean DATABASE_DB2 = true; //DB2

    /**
     *  - PROD_SERVER_IP_ADDRESS = invitech server with Oracle DB and basic authentication
     *  - DEV_SERVER_IP_ADDRESS = initech test server with Oracle DB and basic authentication
     *
     *  //MOST -> a DEV_SERVER_IP_ADDRESS = D2 server
     */
 //   public static String PROD_SERVER_IP_ADDRESS = "mobil-icd.invitech.hu"; //INVITECH = oracle + basic authenticator
    public static String PROD_SERVER_IP_ADDRESS ="91.82.81.106";
    public static String DEV_SERVER_IP_ADDRESS = "91.82.81.106" ; //D2
    public static String D2_SERVER_IP_ADDRESS = "91.82.81.106"; //for update
    public static String SERVER_IP_ADDRESS ;

    static {
        PropertySettings.setMode();
    }


    /**
     *  Authentication in case of D2 server
     */
    public static String CER_USER = "d2";
    public static String CER_PW = "12345678";


    public static String LOGIN_URL ="https://"+SERVER_IP_ADDRESS+"/maxrest/rest/login";
    public static String SOAP_SR_URL_GET ="https://"+SERVER_IP_ADDRESS+"/meaweb/services/D2OFFSITE_MOB_SR1";
    public static String SOAP_SR_URL_UPDATE ="https://"+SERVER_IP_ADDRESS+"/meaweb/services/D2OFFSITE_MOB_SR2";
    public static String SOAP_OWNER_URL ="https://"+SERVER_IP_ADDRESS+"/meaweb/services/D2OFFSITE_MOB_OWNER";
    public static String SOAP_DOC_URL_GET = "https://"+SERVER_IP_ADDRESS+"/meaweb/services/D2OFFSITE_mob_doc2";
    public static String SOAP_WL_URL_GET = "https://"+SERVER_IP_ADDRESS+"/meaweb/services/D2OFFSITE_MOB_WL1";
    public static String SOAP_ATTACHMENT_URL_GET ="https://"+SERVER_IP_ADDRESS+"/meaweb/services/D2OFFSITE_mob_doc3";
    public static String SOAP_LICENSE_URL_GET ="https://"+SERVER_IP_ADDRESS+"/meaweb/services/D2OFFSITE_mob_lic";

    public static String SOAP_APP_VERSION_GET = "https://"+D2_SERVER_IP_ADDRESS+"/meaweb/services/D2OFFSITE_MOB_UPDATE";
    public static String SOAP_NEW_APP_GET = "https://"+D2_SERVER_IP_ADDRESS+"/meaweb/services/D2OFFSITE_MOB_UPDATE2";


}
