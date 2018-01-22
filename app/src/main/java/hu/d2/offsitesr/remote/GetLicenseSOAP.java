package hu.d2.offsitesr.remote;



/**
 * Created by szidonia.laszlo on 2017. 12. 11..
 */

public class GetLicenseSOAP {

    public static String SOAP_ACTION = "urn:processDocument";

    public static String getSoapPayload(String IMEInumber){

        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:max=\"http://www.ibm.com/maximo\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <max:QueryMOB_LIC creationDateTime=\"\" baseLanguage=\"\" transLanguage=\"\" messageID=\"\" maximoVersion=\"\" uniqueResult=\"0\" rsStart=\"0\">\n" +
                "         <max:MOB_LICQuery orderby=\"\" operandMode=\"AND\" >\n" +
                "            <!--Optional:-->\n" +
                "            <max:WHERE></max:WHERE>\n" +
                "            <!--Optional:-->\n" +
                "            <max:MOB_LIC1>\n" +
                "\t\t<max:IMEI>"+IMEInumber+"</max:IMEI>\n" +
                "            </max:MOB_LIC1>\n" +
                "         </max:MOB_LICQuery>\n" +
                "      </max:QueryMOB_LIC>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }
}


