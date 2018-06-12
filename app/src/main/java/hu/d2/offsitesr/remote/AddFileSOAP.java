package hu.d2.offsitesr.remote;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import hu.d2.offsitesr.app.CustomerProperties;
import hu.d2.offsitesr.app.singleton.SettingsSingleton;
import io.reactivex.ObservableEmitter;


/**
 * Created by szidonia.laszlo on 2017. 11. 10..
 */

public class AddFileSOAP<T extends String> extends AbstractSOAP<T> {


    public static String SOAP_ACTION = "urn:processDocument";
    private String ticketID;
    private String generatedName;
    private String fileNameWithExtension;
    private String base64;
    private String urlname;

    public AddFileSOAP(String ticketID, String generatedName , String fileNameWithExtension, String base64, String urlname){
        this.ticketID = ticketID;
        this.generatedName = generatedName;
        this.fileNameWithExtension = fileNameWithExtension;
        this.base64 = base64;
        this.urlname = urlname;
    }

    @Override
    protected void onSucces(InputStream inputStream, ObservableEmitter<T> emitter) throws IOException, SAXException, ParserConfigurationException {
        emitter.onNext((T)generatedName);
        emitter.onComplete();
    }

    @Override
    protected String getSOAPURL() {
        return CustomerProperties.SOAP_SR_URL_UPDATE;
    }

    @Override
    protected String getSOAPPayload() {

        System.out.println("SOAP \t => "+ticketID+" } "+fileNameWithExtension+" } "+urlname+" } "+generatedName);

        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:max=\"http://www.ibm.com/maximo\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <max:UpdateMOB_SR2  creationDateTime=\"\" baseLanguage=\"en\" transLanguage=\"en\" messageID=\"\" maximoVersion=\"\">\n" +
                "         <max:MOB_SR2Set>\n" +
                "         <max:SR action=\"Change\"  transLanguage=\"en\">\n" +
                "           <max:TICKETID > "+ticketID+" </max:TICKETID>\n" +
                "             <max:CLASS maxvalue=\"SR\" >SR</max:CLASS>\n" +
                "\n" +
                "\n" +
                "               <max:DOCLINKS action=\"Add\" relationship=\"\" deleteForInsert=\"no\">\n" +
                "  \n" +
                "\n" +
                "<max:DESCRIPTION >"+fileNameWithExtension+"</max:DESCRIPTION>\n" +
                "<max:DOCTYPE  >Attachments</max:DOCTYPE>\n" +
                "<max:DOCUMENT  >"+generatedName+"</max:DOCUMENT>\n" +
                "<max:DOCUMENTDATA  >"+base64+"</max:DOCUMENTDATA>\n" +
                "\n" +
                " <max:OWNERTABLE  >SR</max:OWNERTABLE>\n" +
                "<max:REFERENCE  >"+ticketID+"</max:REFERENCE>\n" +
                "\n" +
                " <max:URLTYPE  >FILE</max:URLTYPE>\n" +
                "<max:URLNAME  >"+urlname+"</max:URLNAME>\n" +

                "  <max:WEBURL  >"+ SettingsSingleton.getInstance().getWebUrl()+fileNameWithExtension+"</max:WEBURL>          \n" +


                "               </max:DOCLINKS>\n" +
                "            </max:SR>\n" +
                "         </max:MOB_SR2Set>\n" +
                "      </max:UpdateMOB_SR2>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>\n";
    }
}
