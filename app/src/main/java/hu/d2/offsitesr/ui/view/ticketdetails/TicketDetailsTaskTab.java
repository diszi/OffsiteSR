package hu.d2.offsitesr.ui.view.ticketdetails;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.app.singleton.HolderSingleton;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.view.component.ChooseStatusDialog;
import hu.d2.offsitesr.ui.view.component.ChooseTaskStatusDialog;
import hu.d2.offsitesr.ui.view.component.VerticalSpaceItemDecoration;

/**
 * This class is a fragment. Contains tasks of the specified ticket.
 */
public class TicketDetailsTaskTab extends Fragment {

    private TicketDetailsTaskAdapter adapter;
    private ServiceRequestEntity ticket;

    @BindView(R.id.actDetails_taskList)
    RecyclerView compTasks;
    @BindView(R.id.actDetails_emptyText)
    TextView compEmpty;

    private ChooseTaskStatusDialog chooseTaskStatusDialog;

    public TicketDetailsTaskTab() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("---------------->", "TicketDetailsTask Tab");

        if (getArguments() != null) {
            ticket = (ServiceRequestEntity) getArguments().getSerializable(ServiceRequestEntity.SERIALIZABLE_NAME);
        }
        //System.out.println("TICKET "+ticket.getTicketId()+" >> "+ticket.getTasks().size());
    }

    /**
     * @param inflater - The LayoutInflater object that can be used to inflate view in the fragment (R.layout.tab_ticket_details_task)
     * @param container - can be used to generate the LayoutParams of the view
     * @param savedInstanceState - this fragment is being re-constructed from a previous saved state as given here
     * @return - Return the View for the fragment's UI, or null
     *
     * Called to have the fragment instantiate its user interface view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.tab_ticket_details_task, container, false);
        ButterKnife.bind(this,contentView);

        chooseTaskStatusDialog = new ChooseTaskStatusDialog();
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
        this.adapter = new TicketDetailsTaskAdapter(this);
        this.compTasks.setLayoutManager(layoutManager);
        this.compTasks.addItemDecoration(verticalSpaceItemDecoration);
        this.compTasks.setAdapter(this.adapter);

    }


    /*
    *   OnClick on pencil icon - display statuses in dialog
    * */
	public void onClickOnStatusImageButton(int position,String wonum, String siteID){
        FragmentManager fm = getActivity().getFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ChooseStatusDialog.SERIALIZABLE_NAME,HolderSingleton.getInstance().getTaskStatuses());
        bundle.putString("wonum",wonum);
        bundle.putString("siteid",siteID);
        bundle.putInt("pos",position);
        chooseTaskStatusDialog.setArguments(bundle);
        chooseTaskStatusDialog.show(fm,"chooseTask");

    }

    public void updateTaskStatus(String newStatus,int pos){
        adapter.setTaskStatus(newStatus,pos);
    }


}
