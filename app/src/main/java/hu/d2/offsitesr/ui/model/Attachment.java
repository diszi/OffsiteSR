package hu.d2.offsitesr.ui.model;

import java.io.Serializable;
import java.util.List;

import hu.d2.offsitesr.util.EnvironmentTool;

/**
 * Created by szidonia.laszlo on 2017. 11. 27..
 */

public class Attachment implements Serializable {


    public static String SERIALIZABLE_NAME = "Attachment_Serializable";

    private String createDate;
    private String createBy;
    private String webURL;
    private String doclinksID;
    private String fileSize;
    private String fileName;
    private String reference;

    public Attachment(){}


    public String getCreateDate() {
        return EnvironmentTool.convertDateString(createDate);
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getWebURL() {

        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }



    public String getFileSize(){
        return fileSize;
    }

    public void setFileSize(String fileSize){
        this.fileSize = fileSize;
    }


    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public String getFileName(){
        if (getWebURL() != null) {
            fileName = getWebURL().substring(getWebURL().lastIndexOf('/') + 1, getWebURL().length());
        }else
        {
            fileName = null;
        }
        return fileName;
    }

    public String getDoclinksID(){
        return doclinksID;
    }

    public void setDoclinksID(String doclinksID){

        this.doclinksID = doclinksID;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

}
