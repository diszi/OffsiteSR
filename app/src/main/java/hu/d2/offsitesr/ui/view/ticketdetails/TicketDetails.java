package hu.d2.offsitesr.ui.view.ticketdetails;


import java.util.List;

import hu.d2.offsitesr.ui.model.Attachment;
import hu.d2.offsitesr.ui.model.DocLinks;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.model.WorkLog;
import hu.d2.offsitesr.ui.view.base.BaseViewPresenter;
import hu.d2.offsitesr.ui.view.base.helper.RemoteCallBack;

/**
 * Created by csabinko on 2017.09.17..
 */

public interface TicketDetails {

    interface View {


        void showLoading();

        void hideLoading();

        void showErrorMessage(int messageID);

        void loadTicketDetails(ServiceRequestEntity entity);

        String getLoggedInUser();

        void updateStatusRemote(String status);

        void updateTaskStatusRemote(String status,int pos,String wonum,String siteid);

        void updateStatus(String newStatus);

        void updateTaskStatus(String newStatus,int pos);

        void updateOwnerGroup(String newOwnerGroup);

        void updateOwnerGroupRemote(String ownerGroup);

        void updateOwner(String newOwner);

        void updateOwnerRemote(String owner);

        void updatePriorityRemote(String priority);

        void updatePriority(String newPriority);

        void addWorkLogRemote(String shortDesc, String longDesc);

        void addFile(String fileName , String pureFileName,String encode, String urlname );

        void setSyncDate();
    }

    interface Presenter extends BaseViewPresenter {


        void getOwners(String owner);

        void updateStatusRemote(String ticketID, String status);

        void updateTaskStatusRemote(String ticketID, String status,int pos,String wonum,String siteID);

        void updateOwnerGroupRemote(String ticketID, String ownerGroup);

        void updateOwnerRemote(String ticketID, String owner);

        void updatePriorityRemote(String ticketID, String priority);

        void addWorkLogRemote(String ticketID, String owner, String shortDesc, String longDesc);

        void addFile(String tickedID,String fileName, String fileNameWithoutExtension, String encode , String urlname );


        void getWorkLogList(String ticketID, RemoteCallBack<List<WorkLog>> remoteCallBack);

        void getAttachmentList(String ticketID, RemoteCallBack<List<Attachment>> remoteCallBack);

        void getFileDetails(String ticketID, String docklinksID, RemoteCallBack<List<DocLinks>> remoteCallBack);

    }





}
