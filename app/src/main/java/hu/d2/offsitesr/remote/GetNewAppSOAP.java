package hu.d2.offsitesr.remote;

/**
 * Created by szidonia.laszlo on 2017. 12. 15..
 */

public class GetNewAppSOAP {

    public static String SOAP_ACTION = "urn:processDocument";

    public static String getSoapPayload(String appname, String newVersion) {

        System.out.println("SOAP => appname = " + appname+"  newVersion = "+newVersion);
        StringBuffer whereCondition = new StringBuffer("");
        whereCondition.append("appname='" + appname + "' AND version='"+newVersion+"'");


        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:max=\"http://www.ibm.com/maximo\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <max:QueryMOB_UPDATE2 creationDateTime=\"\" baseLanguage=\"\" transLanguage=\"\" messageID=\"\" maximoVersion=\"\" uniqueResult=\"0\" rsStart=\"0\">\n" +
                "         <max:MOB_UPDATE2Query orderby=\"\" operandMode=\"AND\">\n" +
                "            <!--Optional:-->\n" +
                "            <max:WHERE>"+whereCondition+"</max:WHERE>\n" +
                "            <!--Optional:-->\n" +
                "            <max:MOB_UPDATE>\n" +
                "        \n" +
                "            </max:MOB_UPDATE>\n" +
                "         </max:MOB_UPDATE2Query>\n" +
                "      </max:QueryMOB_UPDATE2>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }
}
