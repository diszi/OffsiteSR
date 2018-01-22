package hu.d2.offsitesr.remote;

import java.util.Date;

import hu.d2.offsitesr.util.EnvironmentTool;
import hu.d2.offsitesr.util.UIConstans;

/**
 * Created by csabinko on 2017.09.19..
 */

public class UpdateOwnerGroupSOAP {

    public static String SOAP_ACTION = "urn:processDocument";


    public static String getSoapPayload(String ticketId,String ownerGroup){
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:max=\"http://www.ibm.com/maximo\">\n" +
                "\n" +
                "   <soapenv:Header/>\n" +
                "\n" +
                "   <soapenv:Body>\n" +
                "\n" +
                "      <max:UpdateMOB_SR2 creationDateTime=\"\" baseLanguage=\"EN\" transLanguage=\"EN\" messageID=\"\" maximoVersion=\"\">\n" +
                "\n" +
                "         <max:MOB_SR2Set>\n" +
                "\n" +
                "            <max:SR action=\"Change\"  deleteForInsert=\"\" relationship=\"\" transLanguage=\"EN\">\n" +
                "\n" +
                "  <max:MAXINTERRORMSG></max:MAXINTERRORMSG>\n" +
                "\n" +
                "  <max:CLASS maxvalue=\"SR\" >SR</max:CLASS>\n" +
                "\n" +
                " \n" +
                "\n" +
                "      <max:STATUSDATE changed=\"true\">"+ EnvironmentTool.convertDate(new Date(), UIConstans.DATE_PATTERN_STANDARD)+"</max:STATUSDATE>\n" +
                "\n" +
                "  <max:TICKETID >"+ticketId+"</max:TICKETID>\n" +
                "\n" +
                "<max:OWNERGROUP >"+ownerGroup+"</max:OWNERGROUP>\n" +
                "\n" +
                " \n" +
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
