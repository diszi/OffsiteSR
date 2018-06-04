package hu.d2.offsitesr.remote;


import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import hu.d2.offsitesr.app.CustomerProperties;
import hu.d2.offsitesr.app.singleton.SettingsSingleton;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.util.EntityMapper;
import io.reactivex.ObservableEmitter;


/**
 * Created by csabinko on 2017.09.15..
 */

public class GetTicketListSOAP<T extends List<ServiceRequestEntity>> extends AbstractSOAP<T> {

    private String[] status;
    private String maxListView;
    private String daysForSynchronize;


    public GetTicketListSOAP(String[] status, String maxListView, String daysForSynchronize) {
        this.status = status;
        this.maxListView = maxListView;
        this.daysForSynchronize = daysForSynchronize;
    }

    @Override
    protected void onSucces(InputStream inputStream, ObservableEmitter<T> emitter) throws IOException, SAXException, ParserConfigurationException {
        List<ServiceRequestEntity> ticketList = EntityMapper.transformTicketList(inputStream);
        emitter.onNext((T) ticketList);
        emitter.onComplete();
    }

    @Override
    protected String getSOAPURL() {
        return CustomerProperties.SOAP_SR_URL_GET;
    }

    @Override
    protected String getSOAPPayload() {
        StringBuffer whereConditionForStatus = new StringBuffer("");
        StringBuffer whereConditionForSynchronization = new StringBuffer("");
        StringBuffer whereConditionForOwner = new StringBuffer("");

        if (status != null && status.length != 0) {
            whereConditionForStatus.append("status in (");
            for (int i = 0; i < status.length; i++) {
                if (i == status.length - 1) {
                    whereConditionForStatus.append("'" + status[i] + "')");
                } else {
                    whereConditionForStatus.append("'" + status[i] + "',");
                }
            }
        } // STATUS

        int daySync = Integer.parseInt(daysForSynchronize);

        whereConditionForSynchronization.append("and reportdate >= CURRENT_DATE-" + daySync + " DAYS");
        whereConditionForOwner.append("and owner='" + SettingsSingleton.getInstance().getUserName().toUpperCase() + "'");

        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:max=\"http://www.ibm.com/maximo\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <max:QueryMOB_SR1 creationDateTime=\"\" baseLanguage=\"\" transLanguage=\"\" messageID=\"\" maximoVersion=\"\" maxItems=\"" + maxListView + "\" uniqueResult=\"0\" rsStart=\"0\">\n" +
                "         <max:MOB_SR1Query orderby=\"\" operandMode=\"AND\">\n" +
                "            <!--Optional:-->\n" +
                "            <max:WHERE>" + whereConditionForStatus + " " + whereConditionForSynchronization + " " + whereConditionForOwner + "</max:WHERE>\n" +
                "            <!--Optional:-->\n" +
                "            <max:SR>\n" +
                "            </max:SR>\n" +
                "         </max:MOB_SR1Query>\n" +
                "      </max:QueryMOB_SR1>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }






}
