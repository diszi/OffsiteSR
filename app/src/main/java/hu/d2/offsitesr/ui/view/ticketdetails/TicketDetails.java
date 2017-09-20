package hu.d2.offsitesr.ui.view.ticketdetails;

import java.util.List;

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

    void updateOwner(String newOwner);

    void updateOwnerRemote(String owner);

    void addWorkLogRemote(String shortDesc, String longDesc);

}
