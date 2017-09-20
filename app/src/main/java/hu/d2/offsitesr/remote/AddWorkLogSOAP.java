package hu.d2.offsitesr.remote;

/**
 * Created by csabinko on 2017.09.19..
 */

public class AddWorkLogSOAP {

    public static String SOAP_ACTION = "urn:processDocument";


    public static String getSoapPayload(String ticketID, String user, String shortDesc,String longDesc){
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
                "<max:TICKETID >"+ticketID+"</max:TICKETID>\n" +
                "\n" +
                " \n" +
                "\n" +
                " \n" +
                "\n" +
                "<max:WORKLOG action=\"Add\" relationship=\"\" deleteForInsert=\"no\">\n" +
                "\n" +
                "  <max:CLIENTVIEWABLE changed=\" \">1</max:CLIENTVIEWABLE>\n" +
                "\n" +
                "  <max:CREATEBY changed=\" \">"+user+"</max:CREATEBY>\n" +
                "\n" +
                "  <maxESCRIPTION changed=\" \">"+shortDesc+"</maxESCRIPTION>\n" +
                "\n" +
                "  <maxESCRIPTION_LONGDESCRIPTION changed=\" \">"+longDesc+"</maxESCRIPTION_LONGDESCRIPTION>\n" +
                "\n" +
                "  <max:LOGTYPE maxvalue=\"WORK\" changed=\" \">WORK</max:LOGTYPE>\n" +
                "\n" +
                " \n" +
                "\n" +
                "  <max:WORKLOGID changed=\" \"> </max:WORKLOGID>\n" +
                "\n" +
                "  </max:WORKLOG>\n" +
                "\n" +
                " \n" +
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
