package hu.d2.offsitesr.ui.view.ticketdetails;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.model.WorkLog;
import hu.d2.offsitesr.ui.view.component.AddWorkLogDialog;
import hu.d2.offsitesr.ui.view.component.VerticalSpaceItemDecoration;
import hu.d2.offsitesr.ui.view.component.WorklogDetailsDialog;


/**
 * This class is a fragment. Contains tasks of the specified ticket.
 * On this page there is a refresh layout.
 */
public class TicketDetailsWorkLogTab extends Fragment {

    private AddWorkLogDialog addWorkLogDialog;
    private WorklogDetailsDialog showLongDescriptionDialog;
    private TicketDetailsWorkLogAdapter adapter;
    private TicketDetailsPresenter presenter;

    private ServiceRequestEntity ticket;
    private List<WorkLog> workLogsList ;

    @BindView(R.id.actWorklog_swipeRefreshLayout)
    SwipeRefreshLayout compSwipeRefreshLayout;
    @BindView(R.id.actDetails_workLogList)
    RecyclerView compWorkLogs;
    @BindView(R.id.actDetails_workLogAddButton)
    FloatingActionButton compAddWorklogButton;
    @BindView(R.id.actDetails_emptyText)
    TextView compEmpty;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TicketDetailsWorkLogTab() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ticket = (ServiceRequestEntity) getArguments().getSerializable(ServiceRequestEntity.SERIALIZABLE_NAME);
        }
        presenter = new TicketDetailsPresenterImpl();
    }

    /**
     *
     * @param inflater - The LayoutInflater object that can be used to inflate view in the fragment (R.layout.tab_ticket_details_worklog)
     * @param container - can be used to generate the LayoutParams of the view
     * @param savedInstanceState - this fragment is being re-constructed from a previous saved state as given here
     * @return - Return the View for the fragment's UI, or null
     *
     * Called to have the fragment instantiate its user interface view.
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.tab_ticket_details_worklog, container, false);
        ButterKnife.bind(this,contentView);

        this.setupRecyclerView();

        addWorkLogDialog = new AddWorkLogDialog();
        showLongDescriptionDialog = new WorklogDetailsDialog();

        presenter.setWorklogView(this);

        compSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("---------------->","Refresh Worklog " );
                compSwipeRefreshLayout.setRefreshing(false);
                presenter.getWorkLogList(ticket.getTicketId());
                compEmpty.setVisibility(ticket.getWorkLogs().isEmpty()?View.VISIBLE:View.GONE);
            }
        });

        adapter.setWorkLogs(ticket.getWorkLogs());
        compEmpty.setVisibility(ticket.getWorkLogs().isEmpty()?View.VISIBLE:View.GONE);
        return contentView;
    }


    private void setupRecyclerView() {
        Context context = getActivity().getApplicationContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(
                compWorkLogs.getContext(), layoutManager.getOrientation(), 20);
        this.adapter = new TicketDetailsWorkLogAdapter(this);
        this.compWorkLogs.setLayoutManager(layoutManager);
        this.compWorkLogs.addItemDecoration(verticalSpaceItemDecoration);
        this.compWorkLogs.setAdapter(this.adapter);
    }

    /**
     * @param ticketWorklogList - list with worklog details
     */
    public void loadWorklogList(List<WorkLog> ticketWorklogList){
        this.workLogsList = ticketWorklogList;
        adapter.setWorkLogsRefresh(this.workLogsList);
    }


    /*
    * OnClick on floating action button
    * */
	@OnClick(R.id.actDetails_workLogAddButton)
	public void onClickAddWorkLogButton() {
		FragmentManager fm = getActivity().getFragmentManager();
		addWorkLogDialog.show(fm, "addWorkLog");
	}



    /**
     * @param workLog - is an item, which chosen by the user (long click on item)
     *    - display a long description in the dialog IF it exist
     *   - error message IF it does'nt exist: " No assigned long description "
     */
    public void onClickLongDescButton(WorkLog workLog){
        if (workLog.getLongDescription().equals("") || workLog.getLongDescription() == null){
            Toast.makeText(getContext(),getString(R.string.dialogWorklog_showLongDesc),Toast.LENGTH_SHORT).show();
        }
        else{
            FragmentManager fm = getActivity().getFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putSerializable(WorklogDetailsDialog.SERIALIZABLE_NAME,workLog);
            showLongDescriptionDialog.setArguments(bundle);
            showLongDescriptionDialog.show(fm,"ShowLongDesc");
        }
    }

}
