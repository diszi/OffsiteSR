package hu.d2.offsitesr.remote;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import hu.d2.offsitesr.app.CustomerProperties;
import hu.d2.offsitesr.ui.model.OwnerHolder;
import hu.d2.offsitesr.util.EntityMapper;
import io.reactivex.ObservableEmitter;

/**
 * Created by csabinko on 2017.09.15..
 */

public class GetOwnerListSOAP <T extends OwnerHolder> extends AbstractSOAP<T>{

    private String owner;

    public GetOwnerListSOAP(String owner) {
        this.owner = owner;
    }

    @Override
    public String getSOAPPayload(){

        StringBuffer whereCondition = new StringBuffer("");
        whereCondition.append("respparty='"+owner.toUpperCase()+"'");

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


    @Override
    protected void onSucces(InputStream inputStream, ObservableEmitter<T> emitter) throws IOException, SAXException, ParserConfigurationException {
        emitter.onNext((T)EntityMapper.transformOwnerDataList(inputStream));
        emitter.onComplete();
    }

    @Override
    protected String getSOAPURL() {
        return CustomerProperties.SOAP_OWNER_URL;
    }

}
