package hu.d2.offsitesr.ui.view.ticketdetails;

import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.app.singleton.HolderSingleton;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.view.component.ChooseOwnerDialog;
import hu.d2.offsitesr.ui.view.component.ChooseOwnerGroupDialog;
import hu.d2.offsitesr.ui.view.component.ChoosePriorityDialog;
import hu.d2.offsitesr.ui.view.component.ChooseStatusDialog;

/**
 *
 */
public class TicketDetailsTab extends Fragment {


    @BindView(R.id.actDetails_id)
    TextView compId;
    @BindView(R.id.actDetails_description)
    TextView compDescription;
    @BindView(R.id.actDetails_status)
    TextView compStatus;
    @BindView(R.id.actDetails_reportDate)
    TextView compReportDate;
    @BindView(R.id.actDetails_reportedBy)
    TextView compReportedBy;
    @BindView(R.id.actDetails_affectedPerson)
    TextView compAffectedPerson;
    @BindView(R.id.actDetails_classStructure)
    TextView compClassStructure;
    @BindView(R.id.actDetails_priority)
    TextView compPriority;
    @BindView(R.id.actDetails_ownerGroup)
    TextView compOwnerGroup;
    @BindView(R.id.actDetails_owner)
    TextView compOwner;
//    @BindView(R.id.actDetails_worklog)
//    TextView compWorkLog;

    @BindView(R.id.actDetails_scrollView)
    ScrollView compScrollView;

    @BindView(R.id.actDetails_editOwnerGroupButton)
    ImageButton compEditOwnerGroupButton;
    @BindView(R.id.actDetails_editOwnerButton)
    ImageButton compEditOwnerButton;

    private ChooseStatusDialog chooseStatusDialog;
    private ChooseOwnerGroupDialog chooseOwnerGroupDialog;
    private ChooseOwnerDialog chooseOwnerDialog;
    private ChoosePriorityDialog choosePriorityDialog;

    private ServiceRequestEntity ticket;

//    private OnFragmentInteractionListener mListener;

    public TicketDetailsTab() {
        // Required empty public constructor
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
        final View contentView = inflater.inflate(R.layout.tab_ticket_details, container, false);
        ButterKnife.bind(this,contentView);

        chooseStatusDialog = new ChooseStatusDialog();
        chooseOwnerGroupDialog = new ChooseOwnerGroupDialog();
        chooseOwnerDialog = new ChooseOwnerDialog();
        choosePriorityDialog = new ChoosePriorityDialog();

        loadTicketDetails(ticket);

        return contentView;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void loadTicketDetails(ServiceRequestEntity entity) {
        compId.setText(entity.getTicketId());
        compDescription.setText(entity.getDescription());
        compStatus.setText(entity.getStatus());
        compReportDate.setText(entity.getReportDate());
        compReportedBy.setText(entity.getReportedBy());
        compAffectedPerson.setText(entity.getAffectedPerson());
        compClassStructure.setText(entity.getClassStructure());
        compPriority.setText(HolderSingleton.getInstance().getPriorityValue(entity.getPriority()));
        compOwnerGroup.setText(entity.getOwnerGroup());
        compOwner.setText(entity.getOwner());
    }

    @OnClick(R.id.actDetails_editStatusButton)
    public void onClickChooseStatusButton() {
        FragmentManager fm = getActivity().getFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ChooseStatusDialog.SERIALIZABLE_NAME,HolderSingleton.getInstance().getTicketStatuses());
        chooseStatusDialog.setArguments(bundle);
        chooseStatusDialog.show(fm,"chooseStatus");
    }

    @OnClick(R.id.actDetails_editOwnerButton)
    public void onClickChooseOwnerButton() {
        FragmentManager fm = getActivity().getFragmentManager();

        chooseOwnerDialog.show(fm,"chooseOwner");
    }

    @OnClick(R.id.actDetails_editOwnerGroupButton)
    public void onClickChooseOwnerGroupButton() {
        FragmentManager fm = getActivity().getFragmentManager();

        chooseOwnerGroupDialog.show(fm,"chooseOwnerGroup");
    }

    @OnClick(R.id.actDetails_editPriorityButton)
    public void onClickChoosePriorityButton() {
        FragmentManager fm = getActivity().getFragmentManager();

        choosePriorityDialog.show(fm,"choosePriority");
    }


    public void updateStatus(String newStatus) {
        ticket.setStatus(newStatus);
		compStatus.setText(newStatus);
    }

    public void updateOwner(String newOwner) {
        ticket.setOwner(newOwner);
        compOwner.setText(newOwner);
    }

    public void updateOwnerGroup(String newOwnerGroup) {
        ticket.setOwnerGroup(newOwnerGroup);
		compOwnerGroup.setText(newOwnerGroup);
    }

    public void updatePriority(String newPriority) {
        ticket.setPriority(newPriority);
		compPriority.setText(HolderSingleton.getInstance().getPriorityValue(newPriority));
    }


}
