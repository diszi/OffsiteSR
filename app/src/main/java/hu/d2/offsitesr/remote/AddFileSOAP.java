package hu.d2.offsitesr.remote;

/**
 * Created by szidonia.laszlo on 2017. 11. 10..
 */

public class AddFileSOAP {


    public static String SOAP_ACTION = "urn:processDocument";


    public static String getSoapPayload(String ticketID, String fileName , String fileNameWithoutExtension, String base64, String urlname){

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
                "<max:DESCRIPTION >"+fileNameWithoutExtension+"</max:DESCRIPTION>\n" +
                "<max:DOCTYPE  >Attachments</max:DOCTYPE>\n" +
                "<max:DOCUMENT  >"+fileNameWithoutExtension+"</max:DOCUMENT>\n" +
                "<max:DOCUMENTDATA  >"+base64+"</max:DOCUMENTDATA>\n" +
                "\n" +
                " <max:OWNERTABLE  >SR</max:OWNERTABLE>\n" +
                "<max:REFERENCE  >"+ticketID+"</max:REFERENCE>\n" +
                "\n" +
                " <max:URLTYPE  >FILE</max:URLTYPE>\n" +
                "<max:URLNAME  >"+urlname+"</max:URLNAME>\n" +
                "  <max:WEBURL  >http://192.168.100.101/ATTACHMENTS/"+fileName+"</max:WEBURL>          \n" +
                "               </max:DOCLINKS>\n" +
                "            </max:SR>\n" +
                "         </max:MOB_SR2Set>\n" +
                "      </max:UpdateMOB_SR2>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>\n";
    }
}
