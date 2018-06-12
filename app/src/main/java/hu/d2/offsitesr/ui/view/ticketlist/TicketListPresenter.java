package hu.d2.offsitesr.ui.view.ticketlist;

import android.util.Log;

import java.util.List;

import hu.d2.offsitesr.app.singleton.HolderSingleton;
import hu.d2.offsitesr.app.singleton.SettingsSingleton;
import hu.d2.offsitesr.remote.GetOwnerGroupListSOAP;
import hu.d2.offsitesr.remote.GetOwnerListSOAP;
import hu.d2.offsitesr.remote.GetTicketListSOAP;
import hu.d2.offsitesr.ui.model.OwnerHolder;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.view.base.BasePresenter;
import io.reactivex.Observable;

/**
 * Created by csabinko on 2017.09.15..
 */

public class TicketListPresenter extends BasePresenter implements TicketList.Presenter {

    private TicketList.View view;
    private Observable<List<ServiceRequestEntity>> getTicketListObservable;
    private Observable<OwnerHolder> getOwnerObservable;
    private Observable<OwnerHolder> getOwnerGroupObservable;

    public TicketListPresenter(TicketList.View view) {
        this.view = view;
    }

    @Override
    public void getTicketList() {
        view.showLoading();
        if (getTicketListObservable == null) {
            Log.d("------------------>", " (getTicketList)Observable created");
            getTicketListObservable = new GetTicketListSOAP(SettingsSingleton.getInstance().getSelectedStatus(), SettingsSingleton.getInstance().getMaxListValue(), SettingsSingleton.getInstance().getTicketSynchronization()).createObserver();
        } else {
            disposeAll();
        }
        Log.d("------------------>", " Subscribe to Observable");
        addDisposableToList(getTicketListObservable
                .subscribe((ticketList) -> { // onNext Consumer
                    Log.d("------------------>", " Get Data - ticketList size = "+ticketList.size());
                    view.loadList(ticketList);
                    view.setSyncDate();
                }, (throwable) -> { // onError Consumer
                    view.showErrorMessage(getErrorMessage(throwable));
                    view.hideLoading();
                }, () -> { // onComplate Action
                    view.hideLoading();
                    getOwners(SettingsSingleton.getInstance().getUserName());
                }));

    }

    @Override
    public void getOwners(String owner) {
        Log.d("------------------>", " (getOwner)Observable2 created");
        getOwnerObservable = new GetOwnerListSOAP(owner).createObserver();
        Log.d("------------------>", " Subscribe to Observable2");
        addDisposableToList(getOwnerObservable
                .subscribe((ownerData) -> { // onNext Consumer
                    Log.d("------------------>", " Get Data Owners  - "+ownerData.toString());
                    getOwnerGroups(ownerData.getOwnerGroupList());
                }, (throwable) -> { // onError Consumer
                    view.showErrorMessage(getErrorMessage(throwable));
                    Log.e("------------->", "Dont get data", throwable);
                }, () -> { // onComplate Action
                }));


    }
    @Override
    public void getOwnerGroups(List<String> ownerGroupsList) {
        Log.d("------------------>", "  (getOwnerGroup)Observable3 created");
        getOwnerGroupObservable = new GetOwnerGroupListSOAP(ownerGroupsList).createObserver();
        Log.d("------------------>", " Subscribe to Observable3");
        addDisposableToList(getOwnerGroupObservable
                .subscribe((ownerData) -> { // onNext Consumer
                    Log.d("------------------>", " Get Data Owner groups - "+ownerData.toString());
                    HolderSingleton.getInstance().setOwners(ownerData.getOwnerList());
                    HolderSingleton.getInstance().setOwnerGroups(ownerData.getOwnerGroupList());
                }, (throwable) -> { // onError Consumer
                    view.showErrorMessage(getErrorMessage(throwable));
                }, () -> { // onComplate Action
                }));
    }

    @Override
    public void onDestroy() {
        disposeAll();
    }

}
