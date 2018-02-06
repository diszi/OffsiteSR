package hu.d2.offsitesr.ui.view.ticketdetails;

import java.util.List;

import hu.d2.offsitesr.ui.model.WorkLog;

import io.reactivex.Observable;

/**
 * Created by csabinko on 2017.09.17..
 */

public interface TicketDetailsPresenter {

	 void setView(TicketDetails view);

	 void setViewAttachmentTab(TicketDetailsAttachmentTab view2);

	 void setWorklogView(TicketDetailsWorkLogTab view3);

	 void onDestroy();

	 void getFileDetails(String tickedID,String doclinksID);

	 void getWorkLogList(String ticketID);

	 void getAttachmentList(String ticketID);

	 Observable<List<WorkLog>> createWorklogObservable(String ticketID);

	 void updateStatusRemote(String ticketID, String status);

	 void updateTaskStatusRemote(String ticketID, String status,int pos,String wonum,String siteID);

	 void updateOwnerGroupRemote(String ticketID, String ownerGroup);

	 void updateOwnerRemote(String ticketID, String owner);

	 void updatePriorityRemote(String ticketID, String priority);

	 void addWorkLogRemote(String ticketID, String owner, String shortDesc, String longDesc);

	 void addFile(String tickedID,String fileName, String fileNameWithoutExtension, String encode , String urlname );
}
