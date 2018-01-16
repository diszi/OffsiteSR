package hu.d2.offsitesr.ui.view.ticketdetails;

import java.util.List;

import hu.d2.offsitesr.ui.model.Attachment;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;

/**
 * Created by csabinko on 2017.09.17..
 */

public interface TicketDetails {

    void showLoading();

    void hideLoading();

    void showErrorMessage(int messageID);

    void showSuccessMessage();

    void loadTicketDetails(ServiceRequestEntity entity);
    String getLoggedInUser();

    void updateStatusRemote(String status);

    void updateStatus(String newStatus);

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
