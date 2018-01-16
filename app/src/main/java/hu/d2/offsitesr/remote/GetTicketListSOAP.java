package hu.d2.offsitesr.remote;

import java.sql.Date;

import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.view.ticketlist.TicketListPresenterImpl;
import hu.d2.offsitesr.util.EnvironmentTool;

/**
 * Created by csabinko on 2017.09.15..
 */

public class GetTicketListSOAP {
    public static String SOAP_ACTION = "urn:processDocument";


   public static String getSoapPayload(String[] status, String maxListView, String daysForSynchronize){
       StringBuffer whereConditionForStatus = new StringBuffer("");
       StringBuffer whereConditionForSynchronization = new StringBuffer("");
       StringBuffer whereCondition = new StringBuffer("");


       if (status != null && status.length != 0) {
           whereConditionForStatus.append("status in (");
           for (int i = 0; i < status.length ; i++)
               {
                   if (i == status.length-1) {
                       whereConditionForStatus.append("'" + status[i] + "')");
                   }else
                   {
                       whereConditionForStatus.append("'" + status[i] + "',");
                   }
               }
       } // STATUS

       int daySync = Integer.parseInt(daysForSynchronize);

       whereConditionForSynchronization.append("and reportdate >= CURRENT_DATE-"+daySync+" DAYS");

       // - WHERE : a user  == owner  - 2.eset == ownergroup

        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:max=\"http://www.ibm.com/maximo\">\n"+
                "   <soapenv:Header/>\n"+
                "   <soapenv:Body>\n"+
                "      <max:QueryMOB_SR1 creationDateTime=\"\" baseLanguage=\"\" transLanguage=\"\" messageID=\"\" maximoVersion=\"\" maxItems=\""+maxListView+"\" uniqueResult=\"0\" rsStart=\"0\">\n"+
                "         <max:MOB_SR1Query orderby=\"\" operandMode=\"AND\">\n"+
                "            <!--Optional:-->\n"+
                "            <max:WHERE>"+whereConditionForStatus+" "+whereConditionForSynchronization+"</max:WHERE>\n"+
                "            <!--Optional:-->\n"+
                "            <max:SR>\n"+
                "            </max:SR>\n"+
                "         </max:MOB_SR1Query>\n"+
                "      </max:QueryMOB_SR1>\n"+
                "   </soapenv:Body>\n"+
                "</soapenv:Envelope>";
    }

}
