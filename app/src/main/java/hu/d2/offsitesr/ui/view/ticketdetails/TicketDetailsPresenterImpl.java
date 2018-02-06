package hu.d2.offsitesr.ui.view.ticketdetails;

import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

import butterknife.OnClick;
import hu.d2.offsitesr.R;

import hu.d2.offsitesr.app.singleton.SettingsSingleton;

import hu.d2.offsitesr.remote.AddFileSOAP;
import hu.d2.offsitesr.remote.AddWorkLogSOAP;
import hu.d2.offsitesr.remote.GetAttachmentsSOAP;
import hu.d2.offsitesr.remote.GetFileSOAP;
import hu.d2.offsitesr.remote.GetWorkLogSOAP;
import hu.d2.offsitesr.remote.UpdateOwnerGroupSOAP;
import hu.d2.offsitesr.remote.UpdateOwnerSOAP;
import hu.d2.offsitesr.remote.UpdatePrioritySOAP;
import hu.d2.offsitesr.remote.UpdateStatusSOAP;

import hu.d2.offsitesr.remote.UpdateTaskStatusSOAP;
import hu.d2.offsitesr.ui.model.Attachment;
import hu.d2.offsitesr.ui.model.DocLinks;
import hu.d2.offsitesr.ui.model.WorkLog;
import hu.d2.offsitesr.util.EntityMapper;
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
    private TicketDetailsAttachmentTab attachmentTabView;
    private TicketDetailsWorkLogTab worklogTabView;

    private Disposable disposable;
    private Observable<String> observable;

    private Disposable disposable2;
    private Observable<String> observable2;

    private Disposable disposable3;
    private Observable<String> observable3;

    private Disposable disposable4;
    private Observable<String> observable4;

    private Disposable disposable5;
    private Observable<String> observable5;

    private Disposable disposable6;
    private Observable<String> observable6;

    private Disposable disposable7;
    private Observable<String> observable7;

    private Observable<List<DocLinks>> observableFile;
    private Disposable disposableFile;

    private Observable<List<WorkLog>> observableWorkLog;
    private Disposable disposableWorklog;

    private Observable<List<Attachment>> observableAttachment;
    private Disposable disposableAttachment;



    @Override
    public void setView(TicketDetails view) {
        this.view = view;

    }

    public void setViewAttachmentTab( TicketDetailsAttachmentTab viewAttachmentFrag){
         this.attachmentTabView = viewAttachmentFrag;
    }


    public void setWorklogView(TicketDetailsWorkLogTab worklogTabView){
        this.worklogTabView = worklogTabView;
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
        if (disposable4 != null && !disposable4.isDisposed()) {
            Log.d("------------------>"," Dispose observer");
            disposable4.dispose();
        }
        if (disposable5 != null && !disposable5.isDisposed()) {
            Log.d("------------------>"," Dispose observer");
            disposable5.dispose();
        }
        if (disposable6 != null && !disposable6.isDisposed()) {
            Log.d("------------------>"," Dispose observer");
            disposable6.dispose();
        }

        if (disposable7 != null && !disposable7.isDisposed()) {
            Log.d("------------------>"," Dispose observer");
            disposable7.dispose();
        }

        if (disposableFile != null && !disposableFile.isDisposed()){
            Log.d("------------------>"," DisposeFile observer");
            disposableFile.dispose();
        }
        if (disposableWorklog != null && !disposableWorklog.isDisposed()) {
            Log.d("------------------>"," DisposeWorklog observer");
            disposableWorklog.dispose();
        }
        if (disposableAttachment != null && !disposableAttachment.isDisposed()){
            Log.d("------------------>"," DisposeAttachment observer");
            disposableAttachment.dispose();
        }


    }


    public void getWorkLogList(String ticketID){
        Log.d("------------------>"," Observable created");
        observableWorkLog = createWorklogObservable(ticketID);

        disposableWorklog = observableWorkLog
                .subscribe((worklogList) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data");
                    worklogTabView.loadWorklogList(worklogList);
                }, (throwable) -> { // onError Consumer
                    int errorMessageCode = R.string.error_general;
                    if (throwable instanceof UIThrowable){
                        UIThrowable uiThrowable = (UIThrowable) throwable;
                        errorMessageCode = uiThrowable.getMessageId();
                    }

                   // worklogTabView.showErrorMessage(errorMessageCode);
                    //worklogTabView.hideLoading();
                }, () -> { // onComplate Action

                    //view.hideLoading();

                    //getOwners();
                });
    }


    @Override
    public void getAttachmentList(String ticketID) {
        Log.d("------------------>"," Observable created");
        // if (observableWorkLog == null){
        observableAttachment = createAttachmentObservable(ticketID);
        // }

        disposableAttachment = observableAttachment
                .subscribe((ticketList) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data");
                    attachmentTabView.loadAttachmentRefreshList(ticketList);
                }, (throwable) -> { // onError Consumer
                    int errorMessageCode = R.string.error_general;
                    if (throwable instanceof UIThrowable){
                        UIThrowable uiThrowable = (UIThrowable) throwable;
                        errorMessageCode = uiThrowable.getMessageId();
                    }

                    // worklogTabView.showErrorMessage(errorMessageCode);
                    //worklogTabView.hideLoading();
                }, () -> { // onComplate Action

                    //view.hideLoading();

                    //getOwners();
                });
    }


    @Override
    public void getFileDetails(String ticketID,String doclinksID){
        view.showLoading();
        //if (observableFile == null){
            Log.d("------------------>"," ObservableFile created ");
            observableFile = createFileObservable(ticketID,doclinksID);

       // }
        Log.d("------------------>"," Subscribe to ObservableFile ");
        disposableFile = observableFile
                .subscribe((attachmentDocLinkList) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data - File ");
                    attachmentTabView.loadAttachmentDocLinksList(attachmentDocLinkList);
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
                   // getOwners();
                });
    }

    public void addFile(String ticketID, String generatedName, String fileNameWithExtension, String encode, String urlname){

        view.showLoading();
       // if (observable6 == null) {
            Log.d("------------------>", " Observable created ");

            observable6 = createAddFileObservable(ticketID,generatedName,fileNameWithExtension, encode,urlname);
       // }
        Log.d("------------------>"," Subscribe to Observable ");
        disposable6 = observable6
                .subscribe((filename) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data  ");
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

        Log.d("------------------>"," Observable created ");
        observable3= createAddWorkLogObservable(ticketID, owner, shortDesc, longDesc);

        Log.d("------------------>"," Subscribe to Observable");
        disposable3 = observable3
                .subscribe((ticketList) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data");

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


    public void updateStatusRemote(String ticketID, String status) {
        view.showLoading();
       // if (observable == null){
            Log.d("------------------>"," Observable created");
            observable = createUpdateStatusObservable(ticketID,status);
        //}

        Log.d("------------------>"," Subscribe to Observable ");
        disposable = observable
                .subscribe((newStatus) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data - updateStatusRemote  --- newStatus = "+newStatus);
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
    public void  updateTaskStatusRemote(String ticketID, String status,int pos,String wonum,String siteID){
        Log.d("------------------>"," Observable created");
        observable7 = createUpdateTaskStatusObservable(ticketID,status,wonum,siteID);
        disposable7 = observable7.subscribe((newStatus) -> { // onNext Consumer
            Log.d("------------------>"," Get Data - updateTaskStatusRemote  --- newStatus = "+newStatus);
            view.updateTaskStatus(newStatus,pos);
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
        //if (observable2 == null){
            Log.d("------------------>"," Observable created ");
            observable2 = createUpdateOwnerObservable(ticketID,owner);
        //}
        Log.d("------------------>"," Subscribe to Observable ");
        disposable2 = observable2
                .subscribe((newOwner) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data ");
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
    public void updatePriorityRemote(String ticketID, String priority) {
        view.showLoading();
     //   if (observable5 == null){
            Log.d("------------------>"," Observable created ");
            observable5 = createUpdatePriorityObservable(ticketID,priority);
//        }

       Log.d("------------------>"," Subscribe to Observable ");
        disposable5 = observable5
                .subscribe((newPriority) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data");
                    view.updatePriority(newPriority);
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
    public void updateOwnerGroupRemote(String ticketID, String ownerGroup) {
        view.showLoading();
      //  if (observable4 == null){
            Log.d("------------------>"," Observable created");
            observable4 = createUpdateOwnerGroupObservable(ticketID,ownerGroup);
     //   }

        disposable4 = observable4
                .subscribe((newOwnerGroup) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data");
                    view.updateOwnerGroup(newOwnerGroup);
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





    public Observable<List<WorkLog>> createWorklogObservable(String ticketID){
        Observable<List<WorkLog>> result = Observable.create(emitter -> {
            try {
                Log.d("------------------>"," Start GetWorkLogSOAP Call  ");
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = NetworkTool.createSOAPConnection(NetworkTool.SOAP_WL_URL_GET, GetWorkLogSOAP.SOAP_ACTION,String.format(GetWorkLogSOAP.getSoapPayload(ticketID), SettingsSingleton.getInstance().getUserName()));

                    int responseCode = connection.getResponseCode();

                    if (responseCode == 200) {
                        inputStream = connection.getInputStream();
                        List<WorkLog> workLogList = EntityMapper.transformWorkLogList(inputStream);
                        emitter.onNext(workLogList);
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

    public Observable<List<Attachment>>  createAttachmentObservable(String ticketID){
        Observable<List<Attachment>> result = Observable.create(emitter -> {
            try {
                Log.d("------------------>"," Start GetAttachmentsSOAP Call");
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = NetworkTool.createSOAPConnection(NetworkTool.SOAP_ATTACHMENT_URL_GET, GetAttachmentsSOAP.SOAP_ACTION,String.format(GetAttachmentsSOAP.getSoapPayload(ticketID),SettingsSingleton.getInstance().getUserName()));

                    int responseCode = connection.getResponseCode();

                    if (responseCode == 200) {
                        inputStream = connection.getInputStream();
                        List<Attachment> attachmentList = EntityMapper.transformAttachmentList(inputStream);
                        emitter.onNext(attachmentList);
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

    public Observable<List<DocLinks>> createFileObservable(String ticketID,String doclinksID){

        Observable<List<DocLinks>> result = Observable.create(emitter -> {
            try {
                Log.d("------------------>"," Start GetFileSOAP Call ");
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = NetworkTool.createSOAPConnection(NetworkTool.SOAP_DOC_URL_GET, GetFileSOAP.SOAP_ACTION,String.format(GetFileSOAP.getSoapPayload(ticketID), SettingsSingleton.getInstance().getUserName()));

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        inputStream = connection.getInputStream();
                        List<DocLinks> attachmentDocLinkList = EntityMapper.transformAttachmentDocLinksList(inputStream,doclinksID);
                        emitter.onNext(attachmentDocLinkList);
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


    public Observable<String> createUpdateTaskStatusObservable(String ticketID, String status,String wonum,String siteID) {
        Observable<String> result = Observable.create(emitter -> {
            try {
//                Thread.sleep(2000);
                Log.d("------------------>"," Start Remote SOAP Call ");
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = NetworkTool.createSOAPConnection(NetworkTool.SOAP_SR_URL_UPDATE, UpdateTaskStatusSOAP.SOAP_ACTION,String.format(UpdateTaskStatusSOAP.getSoapPayload(ticketID,status, wonum,  siteID)));

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        inputStream = connection.getInputStream();
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


    public Observable<String> createUpdateOwnerGroupObservable(String ticketID, String ownerGroup) {
        Observable<String> result = Observable.create(emitter -> {
            try {
//                Thread.sleep(2000);
                Log.d("------------------>"," Start Remote SOAP Call");
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = NetworkTool.createSOAPConnection(NetworkTool.SOAP_SR_URL_UPDATE, UpdateOwnerGroupSOAP.SOAP_ACTION,String.format(UpdateOwnerGroupSOAP.getSoapPayload(ticketID,ownerGroup),view.getLoggedInUser()));

                    int responseCode = connection.getResponseCode();

                    if (responseCode == 200) {
                        inputStream = connection.getInputStream();
                        emitter.onNext(ownerGroup);
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
                Log.d("------------------>"," Start Remote SOAP Call ");
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = NetworkTool.createSOAPConnection(NetworkTool.SOAP_SR_URL_UPDATE, UpdateOwnerSOAP.SOAP_ACTION,String.format(UpdateOwnerSOAP.getSoapPayload(ticketID,owner),view.getLoggedInUser()));

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        inputStream = connection.getInputStream();
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

    public Observable<String> createUpdatePriorityObservable(String ticketID, String priority) {
        Observable<String> result = Observable.create(emitter -> {
            try {
//                Thread.sleep(2000);
                Log.d("------------------>"," Start Remote SOAP Call");
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = NetworkTool.createSOAPConnection(NetworkTool.SOAP_SR_URL_UPDATE, UpdatePrioritySOAP.SOAP_ACTION,String.format(UpdatePrioritySOAP.getSoapPayload(ticketID,priority),view.getLoggedInUser()));

                    int responseCode = connection.getResponseCode();
                    System.out.println(" responseCode -> priority = "+responseCode);
                    if (responseCode == 200) {
                        inputStream = connection.getInputStream();
                        emitter.onNext(priority);
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
                        emitter.onNext(ticketID);
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

    public Observable<String> createAddFileObservable(String ticketID,String generatedName , String fileNameWithExtension, String encode, String urlname) {
        Observable<String> result = Observable.create(emitter -> {
            try {
//                Thread.sleep(2000);
                Log.d("------------------>"," Start Remote SOAP Call ");
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = NetworkTool.createSOAPConnection(NetworkTool.SOAP_SR_URL_UPDATE,UpdateOwnerSOAP.SOAP_ACTION,String.format(AddFileSOAP.getSoapPayload(ticketID,generatedName, fileNameWithExtension, encode, urlname),view.getLoggedInUser()));


                    int responseCode = connection.getResponseCode();

                    if (responseCode == 200) {
                        inputStream = connection.getInputStream();
                        emitter.onNext(generatedName);
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
