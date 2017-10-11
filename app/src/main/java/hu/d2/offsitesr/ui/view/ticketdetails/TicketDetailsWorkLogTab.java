package hu.d2.offsitesr.ui.view.ticketdetails;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.view.component.AddWorkLogDialog;
import hu.d2.offsitesr.ui.view.component.VerticalSpaceItemDecoration;


public class TicketDetailsWorkLogTab extends Fragment {

    private AddWorkLogDialog addWorkLogDialog;
    private TicketDetailsWorkLogAdapter adapter;
    private ServiceRequestEntity ticket;

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
    public TicketDetailsWorkLogTab() {
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
        View contentView = inflater.inflate(R.layout.tab_ticket_details_worklog, container, false);
        ButterKnife.bind(this,contentView);

        this.setupRecyclerView();
        addWorkLogDialog = new AddWorkLogDialog();

        adapter.setWorkLogs(ticket.getWorkLogs());
        compEmpty.setVisibility(ticket.getWorkLogs().isEmpty()?View.VISIBLE:View.GONE);

//        // Set the adapter
//        if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
////            recyclerView.setAdapter(new WorkLogViewAdapter(DummyContent.ITEMS, mListener));
//        }
        return contentView;
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnListFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onListFragmentInteraction(DummyItem item);
//    }

    private void setupRecyclerView() {
        Context context = getActivity().getApplicationContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(
                compWorkLogs.getContext(), layoutManager.getOrientation(), 20);
        this.adapter = new TicketDetailsWorkLogAdapter();
        this.compWorkLogs.setLayoutManager(layoutManager);
        this.compWorkLogs.addItemDecoration(verticalSpaceItemDecoration);
        this.compWorkLogs.setAdapter(this.adapter);

    }

	@OnClick(R.id.actDetails_workLogAddButton)
	public void onClickAddWorkLogButton() {
		FragmentManager fm = getActivity().getFragmentManager();

		addWorkLogDialog.show(fm, "addWorkLog");
	}
}
