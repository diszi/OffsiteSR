package hu.d2.offsitesr.ui.view.ticketlist;

import java.util.List;

import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.view.ticketdetails.TicketDetailsWorkLogTab;
import io.reactivex.Observable;

/**
 * Created by csabinko on 2017.09.15..
 */

public interface TicketListPresenter {

    public void setView(TicketList view);

    public void onDestroy();

    public void getTicketList();

    public Observable<List<ServiceRequestEntity>> createObservable();
}
