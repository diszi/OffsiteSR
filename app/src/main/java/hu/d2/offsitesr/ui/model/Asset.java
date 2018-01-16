package hu.d2.offsitesr.ui.model;

import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.List;

/**
 * Created by szidonia.laszlo on 2017. 11. 02..
 */

public class Asset implements Serializable {

    public static String SERIALIZABLE_NAME = "Asset_Serializable";


    private String assetNum;
    private String description;
    private String status;
    private String location;
    private String pluspCustomer;
    private String serialNum;
    private List<AssetUserCust> assetUserCustList;


    public String getAssetNum(){
        return assetNum;

    }

    public void setAssetNum(String assetNum){
        this.assetNum = assetNum ;
    }

    public String getDescription(){
        return description;

    }

    public void setDescription(String description){
        this.description=description;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status=status;
    }

    public String getLocation(){
        return location;
    }

    public void setLocation(String location){
        this.location=location;
    }

    public String getPluspCustomer(){
        return pluspCustomer;
    }

    public void setPluspCustomer(String pluspCustomer){
        this.pluspCustomer= pluspCustomer;
    }


    public String getSerialNum(){
        return serialNum;
    }

    public void setSerialNum(String serialNum)
    {
        this.serialNum=serialNum;
    }

    public void setAssetUserCustList(List<AssetUserCust> assetUserCustList){
        this.assetUserCustList=assetUserCustList;
    }

    public List<AssetUserCust> getAssetUserCustList(){
        return assetUserCustList;
    }


}
