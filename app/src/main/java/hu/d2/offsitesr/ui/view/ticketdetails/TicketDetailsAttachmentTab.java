package hu.d2.offsitesr.ui.view.ticketdetails;


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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.ui.model.Attachment;
import hu.d2.offsitesr.ui.model.DocLinks;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.view.base.BaseTab;
import hu.d2.offsitesr.ui.view.base.helper.RemoteCallBack;
import hu.d2.offsitesr.ui.view.component.VerticalSpaceItemDecoration;

/**
 * This class is a fragment. Contains attachments of the specified ticket.
 */

public class TicketDetailsAttachmentTab extends Fragment implements BaseTab{


    private TicketDetailsAttachmentAdapter adapter;
    private ServiceRequestEntity ticket;
    private List<Attachment> attachmentList;

    private TicketDetails.Presenter presenter;

    @BindView(R.id.actDetails_attachmentList)
    RecyclerView compAttachments;
    @BindView(R.id.actDetails_emptyText)
    TextView compEmpty;

    public TicketDetailsAttachmentTab() {

    }


    /**
     * @param savedInstanceState - contain the activity previously frozen state
     *  Called to do initial creation of the fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("---------------->", "TicketDetailsAttachment Tab");
        if (getArguments() != null) {
            ticket = (ServiceRequestEntity) getArguments().getSerializable(ServiceRequestEntity.SERIALIZABLE_NAME);
        }

    }

    /**
     * @param inflater - The LayoutInflater object that can be used to inflate view in the fragment (R.layout.tab_ticket_details_attachment)
     * @param container - can be used to generate the LayoutParams of the view
     * @param savedInstanceState - this fragment is being re-constructed from a previous saved state as given here
     * @return - Return the View for the fragment's UI, or null
     *
     * Called to have the fragment instantiate its user interface view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.tab_ticket_details_attachment, container, false);
        ButterKnife.bind(this, contentView);

        this.setupRecyclerView();
        remoteGetAttachmentList();

        adapter.setAttachments(ticket.getAttachments());
        compEmpty.setVisibility(ticket.getAttachments().isEmpty() ? View.VISIBLE : View.GONE);

        return contentView;
    }

    private void remoteGetAttachmentList(){
        presenter.getAttachmentList(ticket.getTicketId(), new RemoteCallBack<List<Attachment>>(){

            @Override
            public void onSuccess(List<Attachment> ticketAttachmentList) {
                //System.out.println(" --> remoteGetAttachmentList ticketID = "+ticket.getTicketId()+" >> attachment list size = "+ticketAttachmentList.size());
                /*compEmpty.setVisibility(attachmentList.isEmpty() ? View.VISIBLE : View.GONE);
                attachmentList = ticketAttachmentList;
                System.out.println(" list size = "+attachmentList.size());
                adapter.setAttachments(attachmentList);*/
                loadAttachmentRefreshList(ticketAttachmentList);
            }


        });
    }
    /**
     * @param attachmentDocLinksList - is a list, contains attachment properties of ticket
     */
    public void loadAttachmentDocLinksList(List<DocLinks> attachmentDocLinksList) {
        adapter.setAttachmentDetails(attachmentDocLinksList);
    }


    public void loadAttachmentRefreshList(List<Attachment> attachmentList) {
        compEmpty.setVisibility(attachmentList.isEmpty() ? View.VISIBLE : View.GONE);
        this.attachmentList = attachmentList;
        adapter.setAttachments(this.attachmentList);
    }

    private void setupRecyclerView() {
        Context context = getActivity().getApplicationContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(
                compAttachments.getContext(), layoutManager.getOrientation(), 20);
        this.adapter = new TicketDetailsAttachmentAdapter(this);
        this.compAttachments.setLayoutManager(layoutManager);
        this.compAttachments.addItemDecoration(verticalSpaceItemDecoration);
        this.compAttachments.setAdapter(this.adapter);
    }


    /**
     * @param doclinksID
     * OnClick on download icon - attachment tab
     * Call a method, which download attachment
     */
    public void setOnClickDownloadButton(String doclinksID) {
        presenter.getFileDetails(ticket.getTicketId(), doclinksID, new RemoteCallBack<List<DocLinks>>() {
            @Override
            public void onSuccess(List<DocLinks> attachmentDocLinkList) {
               // System.out.println(" ---> setOnClick Download Btn -> "+ticket.getTicketId()+" - "+doclinksID);
                adapter.setAttachmentDetails(attachmentDocLinkList);
            }
        });
    }


    @Override
    public void setPresenter(TicketDetails.Presenter presenter) {
        this.presenter = presenter;
    }
}
