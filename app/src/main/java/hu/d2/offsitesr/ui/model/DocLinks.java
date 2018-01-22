package hu.d2.offsitesr.ui.model;

import java.io.Serializable;

/**
 * Created by szidonia.laszlo on 2017. 11. 29..
 */

public class DocLinks implements Serializable {

    public static String SERIALIZABLE_NAME = "Doclinks_Serializable";

    private String doclinksID;
    private String documentData;
    private String webURL;


    public DocLinks(){}

    public String getDoclinksID() {

        return doclinksID;
    }

    public void setDoclinksID(String doclinksID) {

        this.doclinksID = doclinksID;
    }

    public String getDocumentData(){

        return documentData;}

    public void setDocumentData(String documentData){

        this.documentData = documentData;
    }

    public String getWebURL() {

        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }


}

