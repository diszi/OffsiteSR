package hu.d2.offsitesr.remote;

/**
 * Created by csabinko on 2017.09.15..
 */

public class GetTicketListSOAP {
    public static String SOAP_ACTION = "urn:processDocument";
    public static String SOAP_PAYLOAD =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:max=\"http://www.ibm.com/maximo\">\n"+
                    "   <soapenv:Header/>\n"+
                    "   <soapenv:Body>\n"+
                    "      <max:QueryMOB_SR1 creationDateTime=\"\" baseLanguage=\"\" transLanguage=\"\" messageID=\"\" maximoVersion=\"\" uniqueResult=\"0\" rsStart=\"0\">\n"+
                    "         <max:MOB_SR1Query orderby=\"\" operandMode=\"AND\">\n"+
                    "            <!--Optional:-->\n"+
                    "            <max:WHERE></max:WHERE>\n"+
                    "            <!--Optional:-->\n"+
                    "            <max:SR>\n"+
                    "            </max:SR>\n"+
                    "         </max:MOB_SR1Query>\n"+
                    "      </max:QueryMOB_SR1>\n"+
                    "   </soapenv:Body>\n"+
                    "</soapenv:Envelope>";



}
