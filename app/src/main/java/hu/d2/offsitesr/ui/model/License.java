package hu.d2.offsitesr.ui.model;

import java.io.Serializable;

/**
 * Created by szidonia.laszlo on 2017. 12. 11..
 */

public class License implements Serializable{

    public static String SERIALIZABLE_NAME = "License_Serializable";

    private String IMEInumber;
    private String expirationDate;
    private String personID;
    private String mob_lic1ID;

    public License(){}

    public String getIMEInumber() {
        return IMEInumber;
    }

    public void setIMEInumber(String IMEInumber) {
        this.IMEInumber = IMEInumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getPersonID(){
        return personID;
    }

    public void setPersonID(String personID){
        this.personID = personID;
    }

    public String getMob_lic1ID(){ return mob_lic1ID;}

    public void setMob_lic1ID(String mob_lic1ID){
        this.mob_lic1ID = mob_lic1ID;
    }







}
