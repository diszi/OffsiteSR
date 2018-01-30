package hu.d2.offsitesr.ui.view.ticketlist;

import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

import hu.d2.offsitesr.R;
import hu.d2.offsitesr.app.singleton.HolderSingleton;
import hu.d2.offsitesr.app.singleton.SettingsSingleton;
import hu.d2.offsitesr.remote.GetOwnerListSOAP;
import hu.d2.offsitesr.remote.GetTicketListSOAP;
import hu.d2.offsitesr.ui.model.OwnerHolder;
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
    private Observable<OwnerHolder> observable2;


	@Override
	public void setView(TicketList view) {
		this.view = view;
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


    @Override
    public void getTicketList() {

        view.showLoading();
        if (observable == null){
            Log.d("------------------>"," Observable created");
            observable = createObservable();
        }
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


	public void getOwners(){
        if (observable2 == null){
            Log.d("------------------>"," Observable2 created");
            observable2 = createGetOwnerObservable();
        }

        Log.d("------------------>"," Subscribe to Observable2");
        disposable2 = observable2
                .subscribe((ownerData) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data Owners");
                    HolderSingleton.getInstance().setOwners(ownerData.getOwnerList());
                    HolderSingleton.getInstance().setOwnerGroups(ownerData.getOwnerGroupList());

                }, (throwable) -> { // onError Consumer
//                    int errorMessageCode = R.string.error_general;
//                    if (throwable instanceof UIThrowable){
//                        UIThrowable uiThrowable = (UIThrowable) throwable;
//                        errorMessageCode = uiThrowable.getMessageId();
//                    }
                    Log.e("------------->","Dont get data",throwable);
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
                Log.d("------------------>"," Start Remote SOAP Call ");
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = NetworkTool.createSOAPConnection(NetworkTool.SOAP_SR_URL_GET, GetTicketListSOAP.SOAP_ACTION,String.format(GetTicketListSOAP.getSoapPayload(SettingsSingleton.getInstance().getSelectedStatus(),SettingsSingleton.getInstance().getMaxListValue(),SettingsSingleton.getInstance().getTicketSynchronization()),SettingsSingleton.getInstance().getUserName()));

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





    public Observable<OwnerHolder> createGetOwnerObservable() {
        Observable<OwnerHolder> result = Observable.create(emitter -> {
            try {
                Log.d("------------------>"," Start Remote SOAP Call");
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = NetworkTool.createSOAPGETConnection(NetworkTool.SOAP_OWNER_URL, GetOwnerListSOAP.SOAP_ACTION,String.format(GetOwnerListSOAP.SOAP_PAYLOAD,SettingsSingleton.getInstance().getUserName()));

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        inputStream = connection.getInputStream();
                        emitter.onNext(EntityMapper.transformOwnerDataList(inputStream));
                        emitter.onComplete();
                    } else {
                        emitter.onError(new UIThrowable(R.string.error_network));
                    }

                } finally {
                    if (connection != null) {
                        connection.disconnect();
                        if (inputStream != null){
                            inputStream.close();
                        }

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
