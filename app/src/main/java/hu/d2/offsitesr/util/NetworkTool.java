package hu.d2.offsitesr.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by csabinko on 2017.09.15..
 */

public class NetworkTool {

    private static String SERVER_IP_ADDRESS="192.168.100.101";
    private static int CONNECTION_TIME_OUT = 5000;
    private static int READ_TIME_OUT = 5000;

    public static String LOGIN_URL ="http://"+SERVER_IP_ADDRESS+"/maxrest/rest/login";
    public static String SOAP_SR_URL_GET ="http://"+SERVER_IP_ADDRESS+"/meaweb/services/EXTSYS1_MOB_SR1";
    public static String SOAP_SR_URL_UPDATE ="http://"+SERVER_IP_ADDRESS+"/meaweb/services/EXTSYS1_MOB_SR2";
    public static String SOAP_OWNER_URL ="http://"+SERVER_IP_ADDRESS+"/meaweb/services/EXTSYS1_MOB_OWNER";



    public static HttpURLConnection createConnection(String url) throws IOException {
        return createConnection(url,null,null,false,true);
    }

    public static HttpURLConnection createSOAPConnection(String url, String soapAction, String soapPayload) throws IOException {
        return createConnection(url, soapAction, soapPayload, true,true);
    }

    public static HttpURLConnection createSOAPGETConnection(String url, String soapAction, String soapPayload) throws IOException {
        return createConnection(url, soapAction, soapPayload, true,false);
    }


    private static synchronized HttpURLConnection createConnection(String url, String soapAction, String soapPayload, boolean isSoap,boolean isPost) throws IOException {
        URL serviceUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) serviceUrl.openConnection();
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod(isPost?"POST":"GET");

        connection.setDoInput(true);
        connection.setConnectTimeout(CONNECTION_TIME_OUT);
        connection.setReadTimeout(READ_TIME_OUT);

        if (isSoap){
            connection.setRequestProperty("Content-Type", "text/xml");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("SOAPAction", soapAction);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(soapPayload.getBytes());

        } else {
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        }


        return connection;
    }

}
