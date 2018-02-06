package hu.d2.offsitesr.remote;

import hu.d2.offsitesr.app.singleton.SettingsSingleton;


/**
 * Created by szidonia.laszlo on 2017. 11. 10..
 */

public class AddFileSOAP {


    public static String SOAP_ACTION = "urn:processDocument";


    public static String getSoapPayload(String ticketID, String generatedName , String fileNameWithExtension, String base64, String urlname){


        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:max=\"http://www.ibm.com/maximo\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <max:UpdateMOB_SR2  creationDateTime=\"\" baseLanguage=\"en\" transLanguage=\"en\" messageID=\"\" maximoVersion=\"\">\n" +
                "         <max:MOB_SR2Set>\n" +
                "         <max:SR action=\"Change\"  transLanguage=\"en\">\n" +
                "           <max:TICKETID > "+ticketID+" </max:TICKETID>\n" +
                "             <max:CLASS maxvalue=\"SR\" >SR</max:CLASS>\n" +
                "\n" +
                "\n" +
                "               <max:DOCLINKS action=\"Add\" relationship=\"\" deleteForInsert=\"no\">\n" +
                "  \n" +
                "\n" +
                "<max:DESCRIPTION >"+fileNameWithExtension+"</max:DESCRIPTION>\n" +
                "<max:DOCTYPE  >Attachments</max:DOCTYPE>\n" +
                "<max:DOCUMENT  >"+generatedName+"</max:DOCUMENT>\n" +
                "<max:DOCUMENTDATA  >"+base64+"</max:DOCUMENTDATA>\n" +
                "\n" +
                " <max:OWNERTABLE  >SR</max:OWNERTABLE>\n" +
                "<max:REFERENCE  >"+ticketID+"</max:REFERENCE>\n" +
                "\n" +
                " <max:URLTYPE  >FILE</max:URLTYPE>\n" +
                "<max:URLNAME  >"+urlname+"</max:URLNAME>\n" +

                "  <max:WEBURL  >"+ SettingsSingleton.getInstance().getWebUrl()+fileNameWithExtension+"</max:WEBURL>          \n" +


                "               </max:DOCLINKS>\n" +
                "            </max:SR>\n" +
                "         </max:MOB_SR2Set>\n" +
                "      </max:UpdateMOB_SR2>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>\n";
    }
}
