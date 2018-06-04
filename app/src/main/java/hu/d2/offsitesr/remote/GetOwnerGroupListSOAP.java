package hu.d2.offsitesr.remote;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import hu.d2.offsitesr.app.CustomerProperties;
import hu.d2.offsitesr.ui.model.OwnerHolder;
import hu.d2.offsitesr.util.EntityMapper;
import io.reactivex.ObservableEmitter;

/**
 * Created by szidonia.laszlo on 2018. 01. 31..
 */

public class GetOwnerGroupListSOAP <T extends OwnerHolder> extends AbstractSOAP<T>{
    private List<String> ownerGroupList;

    public GetOwnerGroupListSOAP(List<String> ownerGroupList) {
        this.ownerGroupList = ownerGroupList;
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

    @Override
    public String getSOAPPayload(){


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
