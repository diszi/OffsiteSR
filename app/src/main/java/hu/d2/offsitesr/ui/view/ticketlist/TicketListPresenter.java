package hu.d2.offsitesr.ui.view.ticketlist;

import java.util.List;

import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import io.reactivex.Observable;

/**
 * Created by csabinko on 2017.09.15..
 */

public interface TicketListPresenter {

    void setView(TicketList view);

    void onDestroy();

    void getTicketList();

    Observable<List<ServiceRequestEntity>> createObservable();
}
