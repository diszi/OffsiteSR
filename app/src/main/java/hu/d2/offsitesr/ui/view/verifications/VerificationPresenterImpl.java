package hu.d2.offsitesr.ui.view.verifications;

import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

import hu.d2.offsitesr.R;
import hu.d2.offsitesr.app.singleton.SettingsSingleton;
import hu.d2.offsitesr.remote.GetLicenseSOAP;
import hu.d2.offsitesr.remote.GetNewAppSOAP;
import hu.d2.offsitesr.remote.GetTicketListSOAP;
import hu.d2.offsitesr.remote.GetUpdateVersionSOAP;
import hu.d2.offsitesr.ui.model.License;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.model.Version;
import hu.d2.offsitesr.util.EntityMapper;
import hu.d2.offsitesr.util.NetworkTool;
import hu.d2.offsitesr.util.UIThrowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by szidonia.laszlo on 2017. 12. 08..
 */

public class VerificationPresenterImpl implements VerificationPresenter {

    private LicenseActivity licens;
    private UpdateActivity update;
    private Disposable disposableLicense,disposableUpdate, disposableDownloadNewApp;
    private Observable<List<License>> observableLicense;
    private Observable<Version> observableUpdate;
    private Observable<Version> observableDownloadNewApp;


    @Override
    public void setLicenseView(LicenseActivity viewLicens) {
            this.licens = viewLicens;
    }

    @Override
    public void setUpdateView(UpdateActivity viewUpdate) {
        this.update =viewUpdate;

    }

    @Override
    public void onDestroy() {
        if (disposableLicense != null && disposableLicense.isDisposed()){
            Log.d("------------------>"," Dispose observer");
            disposableLicense.dispose();
        }
        if (disposableUpdate != null && disposableUpdate.isDisposed()){
            disposableUpdate.dispose();
        }

    }

    @Override
    public void validateLicense(String IMEInumber) {

        //if (observableLicense != null){
            Log.d("------------------>"," License Observable created");
            observableLicense = createLicenseObservable(IMEInumber);

      //  }
        Log.d("------------------>"," License Subscribe to Observable");
        disposableLicense = observableLicense.subscribe((licenseResponse) -> { // onNext Consumer
                                        Log.d("------------------>"," Get Data - License ");
                                        licens.startProcess(licenseResponse);

                                    }, (throwable) -> { // onError Consumer
                                        int errorMessageCode = R.string.error_general;
                                        if (throwable instanceof UIThrowable){
                                            UIThrowable uiThrowable = (UIThrowable) throwable;
                                            errorMessageCode = uiThrowable.getMessageId();
                                        }
//                                        view.showErrorMessage(errorMessageCode);
//                                        view.hideLoading();
                                    }, () -> { // onComplate Action

//                                        view.hideLoading();
//                                        getOwners();
                                    });

    }

    public void getUpdateVersion(String appName){
        if (observableUpdate == null){
            Log.d("------------------>"," Observable created");
            observableUpdate =createUpdateVersionObservable(appName);
        }
        Log.d("------------------>"," Subscribe to Observable");
        disposableUpdate = observableUpdate.subscribe((getVersionResponse) -> { // onNext Consumer
                                Log.d("------------------>"," Get Data - License ");
                                //System.out.println("getVersionResponse = "+getVersionResponse.getAppName()+"   getVersionResponse object = "+getVersionResponse);

                                update.verificUpdateInformations(getVersionResponse);
                            }, (throwable) -> { // onError Consumer
                                int errorMessageCode = R.string.error_general;
                                if (throwable instanceof UIThrowable){
                                    UIThrowable uiThrowable = (UIThrowable) throwable;
                                    errorMessageCode = uiThrowable.getMessageId();
                                }
                    //                                        view.showErrorMessage(errorMessageCode);
                    //                                        view.hideLoading();
                            }, () -> { // onComplate Action

                    //                                        view.hideLoading();
                    //                                        getOwners();
                            });
    }

    public void getNewApp(String appName, String newVersion){
        //if (observableDownloadNewApp == null){
            Log.d("------------------>"," Observable created");
            observableDownloadNewApp =createDownloadNewAppObservable(appName,newVersion);
        //}
        Log.d("------------------>"," Subscribe to Observable");
        disposableDownloadNewApp = observableDownloadNewApp.subscribe((getVersionResponse) -> { // onNext Consumer
                            Log.d("------------------>"," Get Data - License ");

                            update.downloadNewAppAttachment(getVersionResponse);
                        }, (throwable) -> { // onError Consumer
                            int errorMessageCode = R.string.error_general;
                            if (throwable instanceof UIThrowable){
                                UIThrowable uiThrowable = (UIThrowable) throwable;
                                errorMessageCode = uiThrowable.getMessageId();
                            }
                            //                                        view.showErrorMessage(errorMessageCode);
                            //                                        view.hideLoading();
                        }, () -> { // onComplate Action

                            //                                        view.hideLoading();
                            //                                        getOwners();
                        });
    }

   public Observable<List<License>> createLicenseObservable(String IMEInumber){
        Observable<List<License>> result = Observable.create(emitter -> {
            try {
                Log.d("------------------>"," Start  LICENSE SOAP Call ");
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = NetworkTool.createSOAPConnection(NetworkTool.SOAP_LICENSE_URL_GET, GetLicenseSOAP.SOAP_ACTION,String.format(GetLicenseSOAP.getSoapPayload(IMEInumber),SettingsSingleton.getInstance().getUserName()));

                    int responseCode = connection.getResponseCode();
                   // System.out.println("Response Verification = "+responseCode);
                    if (responseCode == 200) {
                        inputStream = connection.getInputStream();

                        List<License> licenseResponse = EntityMapper.transformLicenseData(inputStream,IMEInumber);
                       // System.out.println("LicenseResponse = "+licenseResponse);
                        emitter.onNext(licenseResponse);

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

    public Observable<Version> createUpdateVersionObservable(String appName){
        Observable<Version> result = Observable.create(emitter -> {
            try {
                Log.d("------------------>"," Start  LICENSE SOAP Call ");
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {

                    connection = NetworkTool.createSOAPConnection(NetworkTool.SOAP_APP_VERSION_GET, GetUpdateVersionSOAP.SOAP_ACTION,String.format(GetUpdateVersionSOAP.getSoapPayload(appName),SettingsSingleton.getInstance().getUserName()));
                    int responseCode = connection.getResponseCode();

                    if (responseCode == 200) {
                        inputStream = connection.getInputStream();
                        Version versionObj = EntityMapper.transformVersionData(inputStream);
                        emitter.onNext(versionObj);

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

    public Observable<Version> createDownloadNewAppObservable(String appName,String newVersion){
        Observable<Version> result = Observable.create(emitter -> {
            try {
                Log.d("------------------>"," Start  LICENSE SOAP Call ");
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = NetworkTool.createSOAPConnection(NetworkTool.SOAP_NEW_APP_GET, GetNewAppSOAP.SOAP_ACTION,String.format(GetNewAppSOAP.getSoapPayload(appName,newVersion),SettingsSingleton.getInstance().getUserName()));
                    int responseCode = connection.getResponseCode();
//                    System.out.println("Response Verification = "+responseCode);
                    if (responseCode == 200) {
                        inputStream = connection.getInputStream();
                        Version downloadVersion = EntityMapper.transformVersionData(inputStream);

                        emitter.onNext(downloadVersion);


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
}
