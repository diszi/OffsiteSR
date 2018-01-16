package hu.d2.offsitesr.remote;

/**
 * Created by szidonia.laszlo on 2017. 11. 28..
 */

public class GetFileSOAP {

    public static String SOAP_ACTION = "urn:processDocument";

    public static String getSoapPayload(String tickedID){
        StringBuffer whereCondition = new StringBuffer("");
        whereCondition.append("ticketid='"+tickedID+"'");


        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:max=\"http://www.ibm.com/maximo\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <max:QueryMOB_DOC2 creationDateTime=\"\" baseLanguage=\"\" transLanguage=\"\" messageID=\"\" maximoVersion=\"\"  uniqueResult=\"0\" rsStart=\"0\">\n" +
                "         <max:MOB_DOC2Query orderby=\"\" operandMode=\"AND\">\n" +
                "            <!--Optional:-->\n" +
                "            <max:WHERE>"+whereCondition+"</max:WHERE>\n" +
                "            <!--Optional:-->\n" +
                "            <max:SR>\n" +
                "           \n" +
                "            </max:SR>\n" +
                "         </max:MOB_DOC2Query>\n" +
                "      </max:QueryMOB_DOC2>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

}
