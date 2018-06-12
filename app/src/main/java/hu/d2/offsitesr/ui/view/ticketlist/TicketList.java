package hu.d2.offsitesr.ui.view.ticketlist;

import java.util.List;

import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.model.TicketHolder;
import hu.d2.offsitesr.ui.view.base.BaseViewPresenter;

/**
 * Created by csabinko on 2017.09.15..
 */

public interface TicketList {
	interface View {
		void showLoading();

		void hideLoading();

		void showErrorMessage(int messageID);

		void launchDetailsView(TicketHolder entityHolder);

		String getLoggedInUser();

		void loadList(List<ServiceRequestEntity> ticketList);

		void setSyncDate();

	}
	interface Presenter extends BaseViewPresenter{

		void getTicketList();

		void getOwners(String owner);

		void getOwnerGroups(List<String> ownerGroupsList);
	}
}
