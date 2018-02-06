package hu.d2.offsitesr.ui.model;

import java.io.Serializable;

/**
 * Created by csabinko on 2017.10.11..
 */

public class Task implements Serializable{

    public static String SERIALIZABLE_NAME = "Task_Serializable";

    private String activity; //wonum
    private String summary;
    private String owner;
    private String ownerGroup;
    private String location;
    private String asset;
    private String ci;
    private String status;
    private String siteId;

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setSiteId(String siteId){
        this.siteId =siteId;
    }

    public String getSiteId(){
        return siteId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerGroup() {
        return ownerGroup;
    }

    public void setOwnerGroup(String ownerGroup) {
        this.ownerGroup = ownerGroup;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
