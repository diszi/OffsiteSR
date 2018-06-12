package hu.d2.offsitesr.remote;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import hu.d2.offsitesr.app.CustomerProperties;
import io.reactivex.ObservableEmitter;

/**
 * Created by csabinko on 2017.09.19..
 */

public class AddWorkLogSOAP<T extends String> extends AbstractSOAP<T> {

    public static String SOAP_ACTION = "urn:processDocument";

    private String ticketID;
    private String user;
    private String shortDesc;
    private String longDesc;

    public AddWorkLogSOAP(String ticketID, String user, String shortDesc,String longDesc){
        this.ticketID = ticketID;
        this.user = user;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
    }


    @Override
    protected void onSucces(InputStream inputStream, ObservableEmitter<T> emitter) throws IOException, SAXException, ParserConfigurationException {
        emitter.onNext((T)ticketID);
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
                "<max:TICKETID >"+ticketID+"</max:TICKETID>\n" +
                "\n" +
                " \n" +
                "\n" +
                " \n" +
                "\n" +
                "<max:WORKLOG action=\"Add\" relationship=\"\" deleteForInsert=\"no\">\n" +
                "\n" +
                "  <max:CLIENTVIEWABLE changed=\" \">1</max:CLIENTVIEWABLE>\n" +
                "\n" +
                "  <max:CREATEBY changed=\" \">"+user+"</max:CREATEBY>\n" +
                "\n" +
                "  <max:DESCRIPTION changed=\" \">"+shortDesc+"</max:DESCRIPTION>\n" +
                "\n" +
                "  <max:DESCRIPTION_LONGDESCRIPTION changed=\" \">"+longDesc+"</max:DESCRIPTION_LONGDESCRIPTION>\n" +
                "\n" +
                "  <max:LOGTYPE maxvalue=\"WORK\" changed=\" \">WORK</max:LOGTYPE>\n" +
                "\n" +
                " \n" +
                "\n" +
                "  <max:WORKLOGID changed=\" \"> </max:WORKLOGID>\n" +
                "\n" +
                "  </max:WORKLOG>\n" +
                "\n" +
                " \n" +
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
