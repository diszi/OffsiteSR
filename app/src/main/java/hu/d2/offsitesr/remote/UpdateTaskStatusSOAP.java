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
 * Created by szidonia.laszlo on 2018. 02. 02..
 */

public class UpdateTaskStatusSOAP<T extends String> extends AbstractSOAP<T>{

    private String ticketID;
    private String status;
    private String wonum;
    private String siteID;

    public static String SOAP_ACTION = "urn:processDocument";


    public UpdateTaskStatusSOAP(String ticketID,String status,String wonum,String siteID){
        this.ticketID = ticketID;
        this.status = status;
        this.wonum = wonum;
        this.siteID = siteID;


    }


    @Override
    protected void onSucces(InputStream inputStream, ObservableEmitter<T> emitter) throws IOException, SAXException, ParserConfigurationException {
        emitter.onNext((T)status);
        emitter.onComplete();
    }

    @Override
    protected String getSOAPURL() {
        return CustomerProperties.SOAP_SR_URL_UPDATE;
    }

    @Override
    protected String getSOAPPayload() {

       // System.out.println("---------> TASK status SOAP > task status = "+status);

        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:max=\"http://www.ibm.com/maximo\">\n" +
                "\n" +
                "   <soapenv:Header/>\n" +
                "\n" +
                "   <soapenv:Body>\n" +
                "\n" +
                "      <max:UpdateMOB_SR2  creationDateTime=\"\" baseLanguage=\"en\" transLanguage=\"en\" messageID=\"\" maximoVersion=\"\">\n" +
                "\n" +
                "         <max:MOB_SR2Set>\n" +
                "\n" +
                "         <max:SR action=\"Change\"  transLanguage=\"en\">\n" +
                "\n" +
                "           <max:TICKETID >"+ticketID+"</max:TICKETID>\n" +
                "\n" +
                "             <max:CLASS maxvalue=\"SR\" >SR</max:CLASS>\n" +
                "\n" +
                "               <!--Zero or more repetitions:-->\n" +
                "\n" +
                "               <max:WOACTIVITY   deleteForInsert=\"no\">\n" +
                "\n" +
                "               \n" +
                "\n" +
                "                  <max:STATUS maxvalue=\""+status+"\" >"+status+"</max:STATUS>\n" +
                "\n" +
                "                  <max:STATUSDATE >"+ EnvironmentTool.convertDate(new Date(), UIConstans.DATE_PATTERN_STANDARD)+"</max:STATUSDATE>\n" +
                "\n" +
                "                  <max:WONUM >"+wonum+"</max:WONUM>\n" +
                "\n" +
                "                  <max:SITEID>"+siteID+"</max:SITEID>\n" +
                "\n" +
                "                 \n" +
                "\n" +
                "                \n" +
                "\n" +
                "               </max:WOACTIVITY>\n" +
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
