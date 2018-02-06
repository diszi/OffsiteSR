package hu.d2.offsitesr.remote;

import java.util.Date;

import hu.d2.offsitesr.util.EnvironmentTool;
import hu.d2.offsitesr.util.UIConstans;

/**
 * Created by szidonia.laszlo on 2018. 02. 02..
 */

public class UpdateTaskStatusSOAP {

    public static String SOAP_ACTION = "urn:processDocument";


    public static String getSoapPayload(String ticketID, String status,String wonum, String siteID) {


        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:max=\"http://www.ibm.com/maximo\">\n" +
                "\n" +
                "   <soapenv:Header/>\n" +
                "\n" +
                "   <soapenv:Body>\n" +
                "\n" +
                "      <max:UpdateMOB_SR2  creationDateTime=\"\" baseLanguage=\"en\" transLanguage=\"en\" messageID=\"\" maximoVersion=\"\">\n" +
                "\n" +
                "         <max:MOB_SR2Set>\n" +
                "\n" +
                "         <max:SR action=\"Change\"  transLanguage=\"en\">\n" +
                "\n" +
                "           <max:TICKETID >"+ticketID+"</max:TICKETID>\n" +
                "\n" +
                "             <max:CLASS maxvalue=\"SR\" >SR</max:CLASS>\n" +
                "\n" +
                "               <!--Zero or more repetitions:-->\n" +
                "\n" +
                "               <max:WOACTIVITY   deleteForInsert=\"no\">\n" +
                "\n" +
                "               \n" +
                "\n" +
                "                  <max:STATUS maxvalue=\""+status+"\" >"+status+"</max:STATUS>\n" +
                "\n" +
                "                  <max:STATUSDATE >"+ EnvironmentTool.convertDate(new Date(), UIConstans.DATE_PATTERN_STANDARD)+"</max:STATUSDATE>\n" +
                "\n" +
                "                  <max:WONUM >"+wonum+"</max:WONUM>\n" +
                "\n" +
                "                  <max:SITEID>"+siteID+"</max:SITEID>\n" +
                "\n" +
                "                 \n" +
                "\n" +
                "                \n" +
                "\n" +
                "               </max:WOACTIVITY>\n" +
                "\n" +
                "            </max:SR>\n" +
                "\n" +
                "         </max:MOB_SR2Set>\n" +
                "\n" +
                "      </max:UpdateMOB_SR2>\n" +
                "\n" +
                "   </soapenv:Body>\n" +
                "\n" +
                "</soapenv:Envelope>";
    }
}
