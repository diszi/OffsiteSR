package hu.d2.offsitesr.remote;

/**
 * Created by csabinko on 2017.09.19..
 */

public class UpdateOwnerSOAP {

    public static String SOAP_ACTION = "urn:processDocument";


    public static String getSoapPayload(String ticketId,String owner){
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
                "      <max:STATUSDATE changed=\"true\">2015-10-27T10:44:59</max:STATUSDATE>\n" +
                "\n" +
                "  <max:TICKETID >"+ticketId+"</max:TICKETID>\n" +
                "\n" +
                "<max:OWNER >"+owner+"</max:OWNER>\n" +
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
