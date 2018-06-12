package hu.d2.offsitesr.ui.view.ticketdetails;

import android.util.Log;

import java.util.List;

import hu.d2.offsitesr.app.singleton.HolderSingleton;
import hu.d2.offsitesr.remote.AddFileSOAP;
import hu.d2.offsitesr.remote.AddWorkLogSOAP;
import hu.d2.offsitesr.remote.GetAttachmentsSOAP;
import hu.d2.offsitesr.remote.GetFileSOAP;
import hu.d2.offsitesr.remote.GetOwnerGroupListSOAP;
import hu.d2.offsitesr.remote.GetOwnerListSOAP;
import hu.d2.offsitesr.remote.GetWorkLogSOAP;
import hu.d2.offsitesr.remote.UpdateOwnerGroupSOAP;
import hu.d2.offsitesr.remote.UpdateOwnerSOAP;
import hu.d2.offsitesr.remote.UpdatePrioritySOAP;
import hu.d2.offsitesr.remote.UpdateStatusSOAP;
import hu.d2.offsitesr.remote.UpdateTaskStatusSOAP;
import hu.d2.offsitesr.ui.model.Attachment;
import hu.d2.offsitesr.ui.model.DocLinks;
import hu.d2.offsitesr.ui.model.OwnerHolder;
import hu.d2.offsitesr.ui.model.WorkLog;
import hu.d2.offsitesr.ui.view.base.BasePresenter;
import hu.d2.offsitesr.ui.view.base.helper.RemoteCallBack;
import io.reactivex.Observable;

/**
 * Created by csabinko on 2017.09.17..
 */

 public class TicketDetailsPresenterImpl extends BasePresenter implements TicketDetails.Presenter {

    private TicketDetails.View view;



    private Observable<String> updateStatusRemoteObservable;

    private Observable<OwnerHolder> getOwnerObservable;
    private Observable<OwnerHolder> getOwnerGroupObservable;



    private Observable<String> updateOwnerRemoteObservable;
    private Observable<String> addWorklogRemoteObservable;
    private Observable<String> updateOwnerGroupRemoteObservable;
    private Observable<String> updatePriorityRemoteObservable;
    private Observable<String> addFileObservable;
    private Observable<String> updateTaskStatusRemoteObservable;


    private Observable<List<DocLinks>> observableFile;
    private Observable<List<WorkLog>> observableWorkLog;
    private Observable<List<Attachment>> observableAttachment;

    public TicketDetailsPresenterImpl(TicketDetails.View view) {
        this.view = view;
    }

//
//    public TicketDetailsPresenterImpl(){
//    }
//


    @Override
    public void getOwners(String owner) {
        Log.d("------------------>", " Observable2 created");
        getOwnerObservable = new GetOwnerListSOAP(owner).createObserver();
        Log.d("------------------>", " Subscribe to Observable2");
        addDisposableToList(getOwnerObservable
                .subscribe((ownerData) -> { // onNext Consumer
                    Log.d("------------------>", " Get Data Owners  ");
                    getOwnerGroups(ownerData.getOwnerGroupList());
                }, (throwable) -> { // onError Consumer
                    view.showErrorMessage(getErrorMessage(throwable));

                    Log.e("------------->", "Dont get data", throwable);
                }, () -> { // onComplate Action
                }));


    }

    public void getOwnerGroups(List<String> ownerGroupsList) {
        Log.d("------------------>", " Observable3 created");
        getOwnerGroupObservable = new GetOwnerGroupListSOAP(ownerGroupsList).createObserver();
        Log.d("------------------>", " Subscribe to Observable3");
        addDisposableToList(getOwnerGroupObservable
                .subscribe((ownerData) -> { // onNext Consumer
                    Log.d("------------------>", " Get Data Owner groups");
                    HolderSingleton.getInstance().setOwners(ownerData.getOwnerList());
                    HolderSingleton.getInstance().setOwnerGroups(ownerData.getOwnerGroupList());
                }, (throwable) -> { // onError Consumer
                    view.showErrorMessage(getErrorMessage(throwable));

                }, () -> { // onComplate Action
                }));
    }


    @Override
    public void getWorkLogList(String ticketID, RemoteCallBack<List<WorkLog>> remoteCallBack){
        Log.d("------------------>"," Observable created");
        observableWorkLog = new GetWorkLogSOAP(ticketID).createObserver();

        addDisposableToList(observableWorkLog
                .subscribe((worklogList) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data");
                    remoteCallBack.onSuccess(worklogList);
                }, (throwable) -> { // onError Consumer
                    view.showErrorMessage(getErrorMessage(throwable));
                }, () -> { // onComplate Action
                }));
    }


    @Override
    public void getAttachmentList(String ticketID,RemoteCallBack<List<Attachment>> remoteCallBack) {
        Log.d("------------------>"," Observable created");

        observableAttachment = new GetAttachmentsSOAP(ticketID).createObserver();

        addDisposableToList(observableAttachment
                .subscribe((ticketList) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data");
                    remoteCallBack.onSuccess(ticketList);
                  //  attachmentTabView.loadAttachmentRefreshList(ticketList);
                }, (throwable) -> { // onError Consumer
                    view.showErrorMessage(getErrorMessage(throwable));
                }, () -> { // onComplate Action
                }));
    }


    @Override
    public void getFileDetails(String ticketID,String doclinksID,RemoteCallBack<List<DocLinks>> remoteCallBack){
        view.showLoading();
            Log.d("------------------>"," ObservableFile created ");
            observableFile = new GetFileSOAP(ticketID,doclinksID).createObserver();


        Log.d("------------------>"," Subscribe to ObservableFile ");
        addDisposableToList(observableFile
                .subscribe((attachmentDocLinkList) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data - File ");
                    remoteCallBack.onSuccess(attachmentDocLinkList);
                    //attachmentTabView.loadAttachmentDocLinksList(attachmentDocLinkList);
                }, (throwable) -> { // onError Consumer
                    view.showErrorMessage(getErrorMessage(throwable));

                    Log.d("-------------->", "Don't get data", throwable);
                }, () -> { // onComplate Action

                    view.hideLoading();
                }));

    }

    public void addFile(String ticketID, String generatedName, String fileNameWithExtension, String encode, String urlname){

        view.showLoading();

            Log.d("------------------>", " Observable created ");

        addFileObservable = new AddFileSOAP(ticketID,generatedName,fileNameWithExtension,encode,urlname).createObserver();


        Log.d("------------------>"," Subscribe to Observable ");
        addDisposableToList(addFileObservable
                .subscribe((filename) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data  ");
                }, (throwable) -> { // onError Consumer
                    view.showErrorMessage(getErrorMessage(throwable));
                    Log.d("-------------->", "Don't get data", throwable);
                }, () -> { // onComplate Action

                    view.hideLoading();
                }));

    }


    @Override
    public void addWorkLogRemote(String ticketID, String owner,String shortDesc, String longDesc) {

        Log.d("------------------>"," Observable created ");

        addWorklogRemoteObservable= new AddWorkLogSOAP(ticketID, owner, shortDesc, longDesc).createObserver();



        Log.d("------------------>"," Subscribe to Observable");
        addDisposableToList(addWorklogRemoteObservable
                .subscribe((ticketList) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data");

                }, (throwable) -> { // onError Consumer
                    view.showErrorMessage(getErrorMessage(throwable));

                    Log.d("-------------->", "Don't get data", throwable);
                }, () -> { // onComplate Action

                    view.hideLoading();
                }));

    }


    public void updateStatusRemote(String ticketID, String status) {
        view.showLoading();
        Log.d("------------------>", " Observable created");
        updateStatusRemoteObservable = new UpdateStatusSOAP(ticketID, status).createObserver();

        addDisposableToList(updateStatusRemoteObservable
                .subscribe((newStatus) -> {
                    Log.d("------------------>", " Get Data - updateStatusRemote  --- newStatus = " + newStatus);
                    view.updateStatus(newStatus);
                }, (throwable) -> { // onError Consumer
                    view.showErrorMessage(getErrorMessage(throwable));

                    Log.d("-------------->", "Don't get data", throwable);
                }, () -> { // onComplate Action

                    view.hideLoading();
                }));

    }

    @Override
    public void  updateTaskStatusRemote(String ticketID, String status,int pos,String wonum,String siteID){
        Log.d("------------------>"," Observable created");
        updateTaskStatusRemoteObservable = new UpdateTaskStatusSOAP(ticketID,status,wonum,siteID).createObserver();

        addDisposableToList(updateTaskStatusRemoteObservable.subscribe((newStatus) -> { // onNext Consumer
            Log.d("------------------>"," Get Data - updateTaskStatusRemote  --- newStatus = "+newStatus);
            view.updateTaskStatus(newStatus,pos);
        }, (throwable) -> { // onError Consumer
            view.showErrorMessage(getErrorMessage(throwable));

            Log.d("-------------->", "Don't get data", throwable);
        }, () -> { // onComplate Action

            view.hideLoading();
        }));
    }


    @Override
    public void updateOwnerRemote(String ticketID, String owner) {
        view.showLoading();

            Log.d("------------------>"," Observable created ");
        updateOwnerRemoteObservable = new UpdateOwnerSOAP(ticketID,owner).createObserver();

        Log.d("------------------>"," Subscribe to Observable ");
        addDisposableToList(updateOwnerRemoteObservable
                .subscribe((newOwner) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data ");
                    view.updateOwner(newOwner);

                }, (throwable) -> { // onError Consumer
                    view.showErrorMessage(getErrorMessage(throwable));

                    Log.d("-------------->", "Don't get data", throwable);
                }, () -> { // onComplate Action

                    view.hideLoading();
                }));
    }

    @Override
    public void updatePriorityRemote(String ticketID, String priority) {
        view.showLoading();

        Log.d("------------------>"," (updatePriority)Observable created "+priority);
        updatePriorityRemoteObservable = new UpdatePrioritySOAP(ticketID,priority).createObserver();


       Log.d("------------------>"," Subscribe to Observable ");
        addDisposableToList(updatePriorityRemoteObservable
                .subscribe((newPriority) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data");
                    view.updatePriority(newPriority);
                }, (throwable) -> { // onError Consumer
                    view.showErrorMessage(getErrorMessage(throwable));
                    Log.d("-------------->", "Don't get data", throwable);
                }, () -> { // onComplate Action

                    view.hideLoading();
                }));
    }

    @Override
    public void updateOwnerGroupRemote(String ticketID, String ownerGroup) {
        view.showLoading();

            Log.d("------------------>"," Observable created");
        updateOwnerGroupRemoteObservable = new UpdateOwnerGroupSOAP(ticketID,ownerGroup).createObserver();


        addDisposableToList(updateOwnerGroupRemoteObservable
                .subscribe((newOwnerGroup) -> { // onNext Consumer
                    Log.d("------------------>"," Get Data");
                    view.updateOwnerGroup(newOwnerGroup);
                }, (throwable) -> { // onError Consumer
                    view.showErrorMessage(getErrorMessage(throwable));
                    Log.d("-------------->", "Don't get data", throwable);
                }, () -> { // onComplate Action

                    view.hideLoading();
                }));
    }





    @Override
    public void onDestroy() {
        disposeAll();
    }
}
