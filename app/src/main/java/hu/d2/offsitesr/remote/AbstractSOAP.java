package hu.d2.offsitesr.remote;

import android.util.Log;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import javax.xml.parsers.ParserConfigurationException;

import hu.d2.offsitesr.R;
import hu.d2.offsitesr.util.NetworkTool;
import hu.d2.offsitesr.util.UIThrowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class AbstractSOAP<T> {

    private static String SOAP_ACTION = "urn:processDocument";

    public Observable<T> createObserver(){
        Observable<T> result = Observable.create(emitter -> {
            try {
                Log.d("------------------>"," Start Remote SOAP Call ");
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {

                    connection = NetworkTool.createSOAPConnection(getSOAPURL(), getSOAPAction(),getSOAPPayload());

                    int responseCode = connection.getResponseCode();
                    System.out.println(" RESPONSECODE = "+responseCode);
                    if (responseCode == 200) {
                        inputStream = connection.getInputStream();
                        onSucces(inputStream,emitter);
                    } else {
                        emitter.onError(new UIThrowable(R.string.error_network));
                    }

                } finally {
                    if (connection != null) {
                        if (inputStream != null){
                            inputStream.close();
                        }
                        connection.disconnect();
                    }
                }
            } catch (Exception ex) {
                Log.e("", "---------->", ex);
                emitter.onError(new UIThrowable(R.string.error_unknown));
            }

        });

        return result.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
    }

    abstract protected void onSucces(InputStream inputStream,ObservableEmitter<T> emitter) throws IOException, SAXException, ParserConfigurationException;
    abstract protected String getSOAPURL();
    abstract protected String getSOAPPayload();

    private String getSOAPAction(){
        return SOAP_ACTION;
    }


}
