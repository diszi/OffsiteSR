package hu.d2.offsitesr.ui.view.ticketlist;

import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

import hu.d2.offsitesr.R;
import hu.d2.offsitesr.app.singleton.OwnerAndStatusSingleton;
import hu.d2.offsitesr.remote.GetOwnerListSOAP;
import hu.d2.offsitesr.remote.GetTicketListSOAP;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.util.EntityMapper;
import hu.d2.offsitesr.util.NetworkTool;
import hu.d2.offsitesr.util.UIThrowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by csabinko on 2017.09.15..
 */

public class TicketListPresenterImpl implements TicketListPresenter {

	private TicketList view;
	private Disposable disposable;
    private Disposable disposable2;
    private Observable<List<ServiceRequestEntity>> observable;
    private Observable<List<String>> observable2;

	@Override
	public void setView(TicketList view) {
		this.view = view;
	}

    @Override
    public void getTicketList() {
        view.showLoading();
        if (observable == null){
            Log.d("------------------>"," Observable created");
            observable = createObservable();
        }

//        if (this.disposable == null){
//            Log.d("------------------>","     Observer is null");
//        } else if (this.disposable.isDisposed()){
//            Log.d("------------------>","     Observer is disposed");
//        }

        Log.d("------------------>"," Subscribe to Observable");
        disposable = observable
                .subscribe((ticketList) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data");
                    view.loadList(ticketList);
                    view.setSyncDate();
                }, (throwable) -> { // onError Consumer
                    int errorMessageCode = R.string.error_general;
                    if (throwable instanceof UIThrowable){
                        UIThrowable uiThrowable = (UIThrowable) throwable;
                        errorMessageCode = uiThrowable.getMessageId();
                    }

                    view.showErrorMessage(errorMessageCode);
                    view.hideLoading();
                }, () -> { // onComplate Action

                    view.hideLoading();
                    getOwners();
                });
    }

    @Override
	public void onDestroy() {
		if (disposable != null && !disposable.isDisposed()) {
            Log.d("------------------>"," Dispose observer");
			disposable.dispose();
		}
        if (disposable2 != null && !disposable2.isDisposed()) {
            Log.d("------------------>"," Dispose observer2");
            disposable2.dispose();
        }
	}

	public void getOwners(){
        if (observable2 == null){
            Log.d("------------------>"," Observable2 created");
            observable2 = createGetOwnerObservable();
        }

//        if (this.disposable == null){
//            Log.d("------------------>","     Observer is null");
//        } else if (this.disposable.isDisposed()){
//            Log.d("------------------>","     Observer is disposed");
//        }

        Log.d("------------------>"," Subscribe to Observable2");
        disposable2 = observable2
                .subscribe((ownerList) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data Owners, count: "+ownerList.size());
                    OwnerAndStatusSingleton.getInstance().setOwners(ownerList);

                }, (throwable) -> { // onError Consumer
//                    int errorMessageCode = R.string.error_general;
//                    if (throwable instanceof UIThrowable){
//                        UIThrowable uiThrowable = (UIThrowable) throwable;
//                        errorMessageCode = uiThrowable.getMessageId();
//                    }
                    Log.e("------------->","Nem sikerült az Ownereket letölteni",throwable);
//                    view.showErrorMessage(errorMessageCode);
//                    view.hideLoading();
                }, () -> { // onComplate Action

//                    view.hideLoading();
                });


    }



	@Override
	public Observable<List<ServiceRequestEntity>> createObservable() {
        Observable<List<ServiceRequestEntity>> result = Observable.create(emitter -> {
            try {
//                Thread.sleep(2000);
                Log.d("------------------>"," Start Remote SOAP Call");
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = NetworkTool.createSOAPConnection(NetworkTool.SOAP_SR_URL_GET, GetTicketListSOAP.SOAP_ACTION,String.format(GetTicketListSOAP.SOAP_PAYLOAD,view.getLoggedInUser()));

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        inputStream = connection.getInputStream();
                        List<ServiceRequestEntity> ticketList = EntityMapper.transformTicketList(inputStream);
                        emitter.onNext(ticketList);
                        emitter.onComplete();
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


    public Observable<List<String>> createGetOwnerObservable() {
        Observable<List<String>> result = Observable.create(emitter -> {
            try {
//                Thread.sleep(2000);
                Log.d("------------------>"," Start Remote SOAP Call");
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = NetworkTool.createSOAPGETConnection(NetworkTool.SOAP_OWNER_URL, GetOwnerListSOAP.SOAP_ACTION,String.format(GetOwnerListSOAP.SOAP_PAYLOAD,view.getLoggedInUser()));

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        inputStream = connection.getInputStream();
                        List<String> ownerList = EntityMapper.transformOwnerList(inputStream);
                        emitter.onNext(ownerList);
                        emitter.onComplete();
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

        return result.observeOn(Schedulers.io()).subscribeOn(Schedulers.io());
    }
}
