package hu.d2.offsitesr.ui.view.ticketdetails;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.view.component.VerticalSpaceItemDecoration;


public class TicketDetailsTaskTab extends Fragment {

    private TicketDetailsTaskAdapter adapter;
    private ServiceRequestEntity ticket;

    @BindView(R.id.actDetails_taskList)
    RecyclerView compTasks;
    @BindView(R.id.actDetails_emptyText)
    TextView compEmpty;

    public TicketDetailsTaskTab() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            ticket = (ServiceRequestEntity) getArguments().getSerializable(ServiceRequestEntity.SERIALIZABLE_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.tab_ticket_details_task, container, false);
        ButterKnife.bind(this,contentView);

        this.setupRecyclerView();

        adapter.setTasks(ticket.getTasks());
        compEmpty.setVisibility(ticket.getTasks().isEmpty()?View.VISIBLE:View.GONE);

        return contentView;
    }


    private void setupRecyclerView() {
        Context context = getActivity().getApplicationContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(
                compTasks.getContext(), layoutManager.getOrientation(), 20);
        this.adapter = new TicketDetailsTaskAdapter();
        this.compTasks.setLayoutManager(layoutManager);
        this.compTasks.addItemDecoration(verticalSpaceItemDecoration);
        this.compTasks.setAdapter(this.adapter);

    }


	public void onClickAddWorkLogButton() {
		FragmentManager fm = getActivity().getFragmentManager();

//		addWorkLogDialog.show(fm, "addWorkLog");
	}
}
