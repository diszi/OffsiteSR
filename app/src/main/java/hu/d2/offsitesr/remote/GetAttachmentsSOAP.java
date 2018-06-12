package hu.d2.offsitesr.remote;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import hu.d2.offsitesr.app.CustomerProperties;
import hu.d2.offsitesr.ui.model.Attachment;
import hu.d2.offsitesr.util.EntityMapper;
import io.reactivex.ObservableEmitter;

/**
 * Created by szidonia.laszlo on 2017. 12. 07..
 */

public class GetAttachmentsSOAP<T extends List<Attachment>> extends AbstractSOAP<T> {

    public static String SOAP_ACTION = "urn:processDocument";

    private String ticketID;

    public GetAttachmentsSOAP(String ticketID){
        this.ticketID = ticketID;
    }

    @Override
    protected void onSucces(InputStream inputStream, ObservableEmitter<T> emitter) throws IOException, SAXException, ParserConfigurationException {
        emitter.onNext((T)EntityMapper.transformAttachmentList(inputStream));
        emitter.onComplete();
    }

    @Override
    protected String getSOAPURL() {
        return CustomerProperties.SOAP_ATTACHMENT_URL_GET;
    }

    @Override
    protected String getSOAPPayload() {
        StringBuffer whereCondition = new StringBuffer("");
        whereCondition.append("ticketid='"+ticketID+"'");

       // System.out.println("GETATTACHMENT soap -> where = "+whereCondition);

        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:max=\"http://www.ibm.com/maximo\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <max:QueryMOB_DOC3 creationDateTime=\"\" baseLanguage=\"\" transLanguage=\"\" messageID=\"\" maximoVersion=\"\"  uniqueResult=\"0\" rsStart=\"0\">\n" +
                "         <max:MOB_DOC3Query orderby=\"\" operandMode=\"AND\">\n" +
                "            <!--Optional:-->\n" +
                "            <max:WHERE>"+whereCondition+"</max:WHERE>\n" +
                "            <!--Optional:-->\n" +
                "            <max:SR>\n" +
                "           \n" +
                "            </max:SR>\n" +
                "         </max:MOB_DOC3Query>\n" +
                "      </max:QueryMOB_DOC3>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }




}
