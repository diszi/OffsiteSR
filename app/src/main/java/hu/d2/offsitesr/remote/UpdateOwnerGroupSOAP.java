package hu.d2.offsitesr.remote;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import hu.d2.offsitesr.app.CustomerProperties;
import hu.d2.offsitesr.util.EnvironmentTool;
import hu.d2.offsitesr.util.UIConstans;
import io.reactivex.ObservableEmitter;

/**
 * Created by csabinko on 2017.09.19..
 */

public class UpdateOwnerGroupSOAP<T extends String> extends AbstractSOAP<T>{

    public static String SOAP_ACTION = "urn:processDocument";

    private String ticketID;
    private String ownerGroup;

    public UpdateOwnerGroupSOAP(String ticketID,String ownerGroup){
        this.ticketID = ticketID;
        this.ownerGroup = ownerGroup;
    }


    @Override
    protected void onSucces(InputStream inputStream, ObservableEmitter<T> emitter) throws IOException, SAXException, ParserConfigurationException {
        emitter.onNext((T)ownerGroup);
        emitter.onComplete();
    }

    @Override
    protected String getSOAPURL() {
        return CustomerProperties.SOAP_SR_URL_UPDATE;
    }

    @Override
    protected String getSOAPPayload() {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:max=\"http://www.ibm.com/maximo\">\n" +
                "\n" +
                "   <soapenv:Header/>\n" +
                "\n" +
                "   <soapenv:Body>\n" +
                "\n" +
                "      <max:UpdateMOB_SR2 creationDateTime=\"\" baseLanguage=\"EN\" transLanguage=\"EN\" messageID=\"\" maximoVersion=\"\">\n" +
                "\n" +
                "         <max:MOB_SR2Set>\n" +
                "\n" +
                "            <max:SR action=\"Change\"  deleteForInsert=\"\" relationship=\"\" transLanguage=\"EN\">\n" +
                "\n" +
                "  <max:MAXINTERRORMSG></max:MAXINTERRORMSG>\n" +
                "\n" +
                "  <max:CLASS maxvalue=\"SR\" >SR</max:CLASS>\n" +
                "\n" +
                " \n" +
                "\n" +
                "      <max:STATUSDATE changed=\"true\">"+ EnvironmentTool.convertDate(new Date(), UIConstans.DATE_PATTERN_STANDARD)+"</max:STATUSDATE>\n" +
                "\n" +
                "  <max:TICKETID >"+ticketID+"</max:TICKETID>\n" +
                "\n" +
                "<max:OWNERGROUP >"+ownerGroup+"</max:OWNERGROUP>\n" +
                "\n" +
                " \n" +
                "\n" +
                "            </max:SR>\n" +
                "\n" +
                "         </max:MOB_SR2Set>\n" +
                "\n" +
                "      </max:UpdateMOB_SR2>\n" +
                "\n" +
                "   </soapenv:Body>\n" +
                "\n" +
                "</soapenv:Envelope>";
    }
}
