package hu.d2.offsitesr.remote;

/**
 * Created by szidonia.laszlo on 2017. 12. 12..
 */

public class GetUpdateVersionSOAP {

    public static String SOAP_ACTION = "urn:processDocument";

    public static String getSoapPayload(String appname) {

        StringBuffer whereCondition = new StringBuffer("");
        whereCondition.append("appname='"+appname+"'");


        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:max=\"http://www.ibm.com/maximo\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <max:QueryMOB_UPDATE creationDateTime=\"\" baseLanguage=\"\" transLanguage=\"\" messageID=\"\" maximoVersion=\"\" uniqueResult=\"0\"  rsStart=\"0\">\n" +
                "         <max:MOB_UPDATEQuery orderby=\"\" operandMode=\"AND\">\n" +
                "            <!--Optional:-->\n" +
                "            <max:WHERE>"+whereCondition+"</max:WHERE>\n" +
                "            <!--Optional:-->\n" +
                "            <max:MOB_UPDATE>\n" +
                "              \n" +
                "            </max:MOB_UPDATE>\n" +
                "         </max:MOB_UPDATEQuery>\n" +
                "      </max:QueryMOB_UPDATE>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }


    }
