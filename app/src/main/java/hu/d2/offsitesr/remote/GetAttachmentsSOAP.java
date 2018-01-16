package hu.d2.offsitesr.remote;

/**
 * Created by szidonia.laszlo on 2017. 12. 07..
 */

public class GetAttachmentsSOAP {

    public static String SOAP_ACTION = "urn:processDocument";

    public static String getSoapPayload(String tickedID){
        StringBuffer whereCondition = new StringBuffer("");
        whereCondition.append("ticketid='"+tickedID+"'");

        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:max=\"http://www.ibm.com/maximo\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <max:QueryMOB_DOC3 creationDateTime=\"\" baseLanguage=\"\" transLanguage=\"\" messageID=\"\" maximoVersion=\"\"  uniqueResult=\"0\" rsStart=\"0\">\n" +
                "         <max:MOB_DOC3Query orderby=\"\" operandMode=\"AND\">\n" +
                "            <!--Optional:-->\n" +
                "            <max:WHERE>"+whereCondition+"</max:WHERE>\n" +
                "            <!--Optional:-->\n" +
                "            <max:SR>\n" +
                "           \n" +
                "            </max:SR>\n" +
                "         </max:MOB_DOC3Query>\n" +
                "      </max:QueryMOB_DOC3>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }
}
