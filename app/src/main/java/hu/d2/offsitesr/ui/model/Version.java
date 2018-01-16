package hu.d2.offsitesr.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by szidonia.laszlo on 2017. 12. 15..
 */

public class Version implements Serializable {

    public static String SERIALIZABLE_NAME = "Version_Serializable";

    private String appName;
    private String versionNumber;
    private List<DocLinks> newAppDetails;

    public Version(){}

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
       // System.out.println("appname = "+appName);
        this.appName = appName;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String version) {
       // System.out.println("version nr ="+version);
        this.versionNumber = version;
    }

    public List<DocLinks> getNewAppDetails(){
        return newAppDetails;
    }

    public void setNewAppDetails(List<DocLinks> newAppDetails){
        this.newAppDetails = newAppDetails;
    }

}
