package hu.d2.offsitesr.ui.view.ticketdetails;

import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;

import hu.d2.offsitesr.R;
import hu.d2.offsitesr.remote.AddWorkLogSOAP;
import hu.d2.offsitesr.remote.UpdateOwnerSOAP;
import hu.d2.offsitesr.remote.UpdateStatusSOAP;
import hu.d2.offsitesr.util.NetworkTool;
import hu.d2.offsitesr.util.UIThrowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by csabinko on 2017.09.17..
 */

public class TicketDetailsPresenterImpl implements TicketDetailsPresenter {

    private TicketDetails view;
    private Disposable disposable;
    private Observable<String> observable;
    private Disposable disposable2;
    private Observable<String> observable2;
    private Disposable disposable3;
    private Observable<String> observable3;

    @Override
    public void setView(TicketDetails view) {
        this.view = view;
    }

    @Override
    public void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            Log.d("------------------>"," Dispose observer");
            disposable.dispose();
        }
        if (disposable2 != null && !disposable2.isDisposed()) {
            Log.d("------------------>"," Dispose observer");
            disposable2.dispose();
        }
        if (disposable3 != null && !disposable3.isDisposed()) {
            Log.d("------------------>"," Dispose observer");
            disposable3.dispose();
        }
    }

    public void updateStatusRemote(String ticketID, String status) {
        view.showLoading();
        if (observable == null){
            Log.d("------------------>"," Observable created");
            observable = createUpdateStatusObservable(ticketID,status);
        }

        Log.d("------------------>"," Subscribe to Observable");
        disposable = observable
                .subscribe((newStatus) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data");
//                    view.loadList(ticketList);
                    view.updateStatus(newStatus);
                }, (throwable) -> { // onError Consumer
                    int errorMessageCode = R.string.error_general;
                    if (throwable instanceof UIThrowable){
                        UIThrowable uiThrowable = (UIThrowable) throwable;
                        errorMessageCode = uiThrowable.getMessageId();
                    }

                    view.showErrorMessage(errorMessageCode);
                    view.hideLoading();
                }, () -> { // onComplate Action
                    view.showSuccessMessage();
                    view.hideLoading();
                });
    }

    @Override
    public void updateOwnerRemote(String ticketID, String owner) {
        view.showLoading();
        if (observable2 == null){
            Log.d("------------------>"," Observable created");
            observable2 = createUpdateOwnerObservable(ticketID,owner);
        }

        Log.d("------------------>"," Subscribe to Observable");
        disposable2 = observable2
                .subscribe((newOwner) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data");
//                    view.loadList(ticketList);
                    view.updateOwner(newOwner);
                }, (throwable) -> { // onError Consumer
                    int errorMessageCode = R.string.error_general;
                    if (throwable instanceof UIThrowable){
                        UIThrowable uiThrowable = (UIThrowable) throwable;
                        errorMessageCode = uiThrowable.getMessageId();
                    }

                    view.showErrorMessage(errorMessageCode);
                    view.hideLoading();
                }, () -> { // onComplate Action
                    view.showSuccessMessage();
                    view.hideLoading();
                });
    }

    @Override
    public void addWorkLogRemote(String ticketID, String owner,String shortDesc, String longDesc) {
        view.showLoading();
        if (observable3 == null){
            Log.d("------------------>"," Observable created");
            observable3= createAddWorkLogObservable(ticketID, owner, shortDesc, longDesc);
        }

        Log.d("------------------>"," Subscribe to Observable");
        disposable3 = observable3
                .subscribe((newOwner) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data");
//                    view.loadList(ticketList);
//                    view.updateOwner(newOwner);
                }, (throwable) -> { // onError Consumer
                    int errorMessageCode = R.string.error_general;
                    if (throwable instanceof UIThrowable){
                        UIThrowable uiThrowable = (UIThrowable) throwable;
                        errorMessageCode = uiThrowable.getMessageId();
                    }

                    view.showErrorMessage(errorMessageCode);
                    view.hideLoading();
                }, () -> { // onComplate Action
                    view.showSuccessMessage();
                    view.hideLoading();
                });
    }


    public Observable<String> createUpdateStatusObservable(String ticketID, String status) {
        Observable<String> result = Observable.create(emitter -> {
            try {
//                Thread.sleep(2000);
                Log.d("------------------>"," Start Remote SOAP Call");
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = NetworkTool.createSOAPConnection(NetworkTool.SOAP_SR_URL_UPDATE, UpdateStatusSOAP.SOAP_ACTION,String.format(UpdateStatusSOAP.getSoapPayload(ticketID,status),view.getLoggedInUser()));

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        inputStream = connection.getInputStream();
//                        List<ServiceRequestEntity> ticketList = EntityMapper.transformTicketList(inputStream);
                        emitter.onNext(status);
                        emitter.onComplete();
                    }else if (responseCode == 500) {
                        emitter.onError(new UIThrowable(R.string.error_failedOperation));
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

    public Observable<String> createUpdateOwnerObservable(String ticketID, String owner) {
        Observable<String> result = Observable.create(emitter -> {
            try {
//                Thread.sleep(2000);
                Log.d("------------------>"," Start Remote SOAP Call");
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = NetworkTool.createSOAPConnection(NetworkTool.SOAP_SR_URL_UPDATE, UpdateOwnerSOAP.SOAP_ACTION,String.format(UpdateOwnerSOAP.getSoapPayload(ticketID,owner),view.getLoggedInUser()));

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        inputStream = connection.getInputStream();
//                        List<ServiceRequestEntity> ticketList = EntityMapper.transformTicketList(inputStream);
                        emitter.onNext(owner);
                        emitter.onComplete();
                    }else if (responseCode == 500) {
                        emitter.onError(new UIThrowable(R.string.error_failedOperation));
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

    public Observable<String> createAddWorkLogObservable(String ticketID, String owner,String shortDesc, String longDesc) {
        Observable<String> result = Observable.create(emitter -> {
            try {
//                Thread.sleep(2000);
                Log.d("------------------>"," Start Remote SOAP Call");
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = NetworkTool.createSOAPConnection(NetworkTool.SOAP_SR_URL_UPDATE, UpdateOwnerSOAP.SOAP_ACTION,String.format(AddWorkLogSOAP.getSoapPayload(ticketID,owner,shortDesc,longDesc),view.getLoggedInUser()));

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        inputStream = connection.getInputStream();
//                        List<ServiceRequestEntity> ticketList = EntityMapper.transformTicketList(inputStream);
//                        emitter.onNext(owner);
                        emitter.onComplete();
                    }else if (responseCode == 500) {
                        emitter.onError(new UIThrowable(R.string.error_failedOperation));
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
}
