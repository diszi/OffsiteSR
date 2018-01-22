package hu.d2.offsitesr.ui.view.ticketdetails;


import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.app.singleton.HolderSingleton;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.view.component.AssetDetailsDialog;

import hu.d2.offsitesr.ui.view.component.ChooseOwnerDialog;
import hu.d2.offsitesr.ui.view.component.ChooseOwnerGroupDialog;
import hu.d2.offsitesr.ui.view.component.ChoosePriorityDialog;
import hu.d2.offsitesr.ui.view.component.ChooseStatusDialog;


public class TicketDetailsTab extends Fragment {

    private  static int PICK_FILE_REQUEST_CODE=0;
    private static int TAKE_PICTURE_REQUEST =1;

    private ServiceRequestEntity ticket;

    @BindView(R.id.actDetails_id)
    TextView compId;
    @BindView(R.id.actDetails_description)
    TextView compDescription;
    @BindView(R.id.actDetails_status)
    TextView compStatus;
    @BindView(R.id.actDetails_assetNum)
    TextView compAssetNum;
    @BindView(R.id.actDetails_ciNum)
    TextView compCiNum;
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
    @BindView(R.id.actDetails_scrollView)
    ScrollView compScrollView;

    private ChooseStatusDialog chooseStatusDialog;
    private ChooseOwnerGroupDialog chooseOwnerGroupDialog;
    private ChooseOwnerDialog chooseOwnerDialog;
    private ChoosePriorityDialog choosePriorityDialog;
    private AssetDetailsDialog assetDetailsDialog;
    //private SavePictureDialog savePictureDialog;

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
        assetDetailsDialog = new AssetDetailsDialog();
      //  savePictureDialog = new SavePictureDialog();

        loadTicketDetails(ticket);
        return contentView;
    }


    private void loadTicketDetails(ServiceRequestEntity entity) {
        compId.setText(entity.getTicketId());
        compDescription.setText(entity.getDescription());
        compStatus.setText(entity.getStatus());
        compAssetNum.setText(entity.getAssetNum());
        compCiNum.setText(entity.getCINum());
        compReportDate.setText(entity.getReportDate());
        compReportedBy.setText(entity.getReportedBy());
        compAffectedPerson.setText(entity.getAffectedPerson());
        compClassStructure.setText(entity.getClassStructure());
        compPriority.setText(HolderSingleton.getInstance().getPriorityValue(entity.getPriority()));
        compOwnerGroup.setText(entity.getOwnerGroup());
        compOwner.setText(entity.getOwner());

    }

    /*
    *   OnClick on upload icon - show folders  - select file
    * */
    @OnClick(R.id.actDetails_uploadFileButton)
    public void onClickUploadFileButton(){
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(Intent.createChooser(intent,getString(R.string.actDetails_chooseButton)),PICK_FILE_REQUEST_CODE);
    }

    /*
    *   OnClick on camera icon - entry in phone camera mode
    * */
    @OnClick(R.id.actDetails_takePictureButton)
    public  void onClickTakePictureButton() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        getActivity().startActivityForResult(intent,TAKE_PICTURE_REQUEST);
    }

    /*
    *   OnClick on zoom icom - display asset details in dialog or error message (msg: no assigned asset)
    * */
    @OnClick(R.id.actDetails_assetZoomButton)
    public void onClickAssetZoomButton(){
        if (ticket.getAssetNum() == ""){
            Toast.makeText(getActivity(),getString(R.string.assDetails_errorMsg),Toast.LENGTH_SHORT).show();
        }else {
            FragmentManager fm = getActivity().getFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putSerializable(AssetDetailsDialog.SERIALIZABLE_NAME, ticket.getAsset());
            assetDetailsDialog.setArguments(bundle);
            assetDetailsDialog.show(fm, "assetDetails");
        }
    }

    /**
     *  Onclick on status button (pencil icon) - display status list
     */

    @OnClick(R.id.actDetails_editStatusButton)
    public void onClickChooseStatusButton() {
        FragmentManager fm = getActivity().getFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ChooseStatusDialog.SERIALIZABLE_NAME,HolderSingleton.getInstance().getTicketStatuses());
        chooseStatusDialog.setArguments(bundle);
        chooseStatusDialog.show(fm,"chooseStatus");
    }

    /*
    *   OnClick on pencil icon - dislapy owners in dialog
    * */
    @OnClick(R.id.actDetails_editOwnerButton)
    public void onClickChooseOwnerButton() {
        FragmentManager fm = getActivity().getFragmentManager();
        chooseOwnerDialog.show(fm,"chooseOwner");
    }

    /*
    *   OnClick on pencil icon - display owners group
    **/
    @OnClick(R.id.actDetails_editOwnerGroupButton)
    public void onClickChooseOwnerGroupButton() {
        FragmentManager fm = getActivity().getFragmentManager();
        chooseOwnerGroupDialog.show(fm,"chooseOwnerGroup");
    }

    /*
    *   OnClick on pencil icon - display priority list
    * */
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
