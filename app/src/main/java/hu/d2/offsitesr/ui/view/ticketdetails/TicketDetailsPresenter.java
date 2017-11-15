package hu.d2.offsitesr.ui.view.ticketdetails;

import java.util.List;

import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.view.ticketlist.TicketList;
import io.reactivex.Observable;

/**
 * Created by csabinko on 2017.09.17..
 */

public interface TicketDetailsPresenter {

	public void setView(TicketDetails view);

	public void onDestroy();

	public void updateStatusRemote(String ticketID, String status);

	public void updateOwnerGroupRemote(String ticketID, String ownerGroup);

	public void updateOwnerRemote(String ticketID, String owner);

	public void updatePriorityRemote(String ticketID, String priority);

	public void addWorkLogRemote(String ticketID, String owner, String shortDesc, String longDesc);

	public void addFile(String tickedID,String fileName, String fileNameWithoutExtension, String encode , String urlname );
}
