package hu.d2.offsitesr.remote;

import java.util.List;

/**
 * Created by szidonia.laszlo on 2018. 01. 31..
 */

public class GetOwnerGroupListSOAP {
    public static String SOAP_ACTION = "urn:processDocument";

    public static String getSoapPayload(List<String> ownerGroupList){


        StringBuffer whereCondition = new StringBuffer("");
        if (ownerGroupList != null && ownerGroupList.size() != 0) {
            whereCondition.append("persongroup in (");
            for (int i = 0; i < ownerGroupList.size() ; i++)
            {
                    whereCondition.append("'" + ownerGroupList.get(i) + "',");
            }
            whereCondition.append("'ICT_L0HD')");
        }


        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:max=\"http://www.ibm.com/maximo\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <max:QueryMOB_OWNER creationDateTime=\"\" baseLanguage=\"\" transLanguage=\"\" messageID=\"\" maximoVersion=\"\" uniqueResult=\"0\"  rsStart=\"0\">\n" +
                "         <max:MOB_OWNERQuery orderby=\"\" operandMode=\"AND\">\n" +
                "            <!--Optional:-->\n" +
                "            <max:WHERE>"+whereCondition+"</max:WHERE>\n" +
                "            \n" +
                "         </max:MOB_OWNERQuery>\n" +
                "      </max:QueryMOB_OWNER>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }
}
