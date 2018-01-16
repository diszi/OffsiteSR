package hu.d2.offsitesr.remote;

/**
 * Created by szidonia.laszlo on 2017. 12. 04..
 */

public class GetWorkLogSOAP {

    public static String SOAP_ACTION = "urn:processDocument";

    public static String getSoapPayload(String tickedID){
        StringBuffer whereCondition = new StringBuffer("");
        whereCondition.append("recordkey='"+tickedID+"'");


        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:max=\"http://www.ibm.com/maximo\">\n" +
                "                   <soapenv:Header/>\n" +
                "                   <soapenv:Body>\n" +
                "                      <max:QueryMOB_WL1 creationDateTime=\"\" baseLanguage=\"\" transLanguage=\"\" messageID=\"\" maximoVersion=\"\"  uniqueResult=\"0\" rsStart=\"0\">\n" +
                "                         <max:MOB_WL1Query orderby=\"\" operandMode=\"AND\">\n" +
                "                            <!--Optional:-->\n" +
                "                            <max:WHERE>"+whereCondition+"</max:WHERE>\n" +
                "                            <!--Optional:-->\n" +
                "                            <max:WORKLOG>\n" +
                "                           \n" +
                "                            </max:WORKLOG>\n" +
                "                         </max:MOB_WL1Query>\n" +
                "                      </max:QueryMOB_WL1>\n" +
                "                   </soapenv:Body>\n" +
                "                </soapenv:Envelope>";
    }
}
