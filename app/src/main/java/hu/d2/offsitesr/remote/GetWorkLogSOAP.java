package hu.d2.offsitesr.remote;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import hu.d2.offsitesr.app.CustomerProperties;
import hu.d2.offsitesr.ui.model.WorkLog;
import hu.d2.offsitesr.util.EntityMapper;
import io.reactivex.ObservableEmitter;

/**
 * Created by szidonia.laszlo on 2017. 12. 04..
 */

public class GetWorkLogSOAP<T extends List<WorkLog>> extends AbstractSOAP<T> {

    private String ticketID;

    public GetWorkLogSOAP(String ticketID) {
        this.ticketID = ticketID;
    }

    @Override
    protected void onSucces(InputStream inputStream, ObservableEmitter<T> emitter) throws IOException, SAXException, ParserConfigurationException {
        emitter.onNext((T)EntityMapper.transformWorkLogList(inputStream));
        emitter.onComplete();
    }

    @Override
    protected String getSOAPURL() {
        return CustomerProperties.SOAP_WL_URL_GET;
    }

    public String getSOAPPayload(){
        StringBuffer whereCondition = new StringBuffer("");
        whereCondition.append("recordkey='"+ticketID+"'");


        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:max=\"http://www.ibm.com/maximo\">\n" +
                "                   <soapenv:Header/>\n" +
                "                   <soapenv:Body>\n" +
                "                      <max:QueryMOB_WL1 creationDateTime=\"\" baseLanguage=\"\" transLanguage=\"\" messageID=\"\" maximoVersion=\"\"  uniqueResult=\"0\" rsStart=\"0\">\n" +
                "                         <max:MOB_WL1Query orderby=\"\" operandMode=\"AND\">\n" +
                "                            <!--Optional:-->\n" +
                "                            <max:WHERE>"+whereCondition+"</max:WHERE>\n" +
                "                            <!--Optional:-->\n" +
                "                            <max:WORKLOG>\n" +
                "                           \n" +
                "                            </max:WORKLOG>\n" +
                "                         </max:MOB_WL1Query>\n" +
                "                      </max:QueryMOB_WL1>\n" +
                "                   </soapenv:Body>\n" +
                "                </soapenv:Envelope>";
    }
}
