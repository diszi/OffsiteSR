package hu.d2.offsitesr.remote;

/**
 * Created by csabinko on 2017.09.15..
 */

public class GetOwnerListSOAP {
    public static String SOAP_ACTION = "urn:processDocument";
    public static String SOAP_PAYLOAD ="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:max=\"http://www.ibm.com/maximo\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <max:QueryMOB_OWNER creationDateTime=\"\" baseLanguage=\"\" transLanguage=\"\" messageID=\"\" maximoVersion=\"\" uniqueResult=\"0\"  rsStart=\"0\">\n" +
            "         <max:MOB_OWNERQuery orderby=\"\" operandMode=\"AND\">\n" +
            "            <!--Optional:-->\n" +
            "            <max:WHERE></max:WHERE>\n" +
            "            \n" +
            "         </max:MOB_OWNERQuery>\n" +
            "      </max:QueryMOB_OWNER>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";



}
