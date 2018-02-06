package hu.d2.offsitesr.ui.view.ticketlist;

import java.util.List;

import hu.d2.offsitesr.ui.model.OwnerHolder;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import io.reactivex.Observable;

/**
 * Created by csabinko on 2017.09.15..
 */

public interface TicketListPresenter {

    void setView(TicketList view);

    void onDestroy();

    void getTicketList();

    void getOwners(String owner);

    void getOwnerGroups(List<String> ownerGroupsList);

    Observable<List<ServiceRequestEntity>> createObservable();

    Observable<OwnerHolder> createGetOwnerObservable(String owner);

    Observable<OwnerHolder> createGetOwnerGroupObservable(List<String> ownerGroupsList);
}
