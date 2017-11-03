package hu.d2.offsitesr.ui.model;

import java.io.Serializable;

/**
 * Created by szidonia.laszlo on 2017. 11. 03..
 */

public class AssetUserCust implements Serializable {

    public static String SERIALIZABLE_NAME = "AssetUserCust_Serializable";

    private boolean isCustodian;
    private boolean isUser;
    private boolean isPrimary;
    private String location;
    private String personID;

    public void setIsCustodian(boolean custodian){
        this.isCustodian=custodian;
    }

    public boolean isCustodian(){
        return isCustodian;
    }

    public void setIsUser(boolean user){
        this.isUser=user;
    }

    public boolean isUser(){
        return isUser;
    }

    public void setIsPrimary(boolean primary){
        this.isPrimary=primary;
    }

    public boolean isPrimary(){
        return isPrimary;
    }

    public void setLocation(String location){
        this.location=location;
    }

    public String getLocation(){
        return  location;
    }

    public void setPersonID(String personID){
        this.personID = personID;
    }

    public String getPersonID(){
        return personID;
    }

}
