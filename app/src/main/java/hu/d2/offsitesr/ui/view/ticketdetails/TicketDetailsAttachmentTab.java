package hu.d2.offsitesr.ui.view.ticketdetails;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import hu.d2.offsitesr.ui.view.component.VerticalSpaceItemDecoration;


public class TicketDetailsAttachmentTab extends Fragment {


    private TicketDetailsAttachmentAdapter adapter;
    private ServiceRequestEntity ticket;
    private List<Attachment> attachmentList;
    private TicketDetailsPresenterImpl presenter;

    @BindView(R.id.actDetails_attachmentList)
    RecyclerView compAttachments;
    @BindView(R.id.actDetails_emptyText)
    TextView compEmpty;

    public TicketDetailsAttachmentTab() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            ticket = (ServiceRequestEntity) getArguments().getSerializable(ServiceRequestEntity.SERIALIZABLE_NAME);
        }
        presenter = new TicketDetailsPresenterImpl();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.tab_ticket_details_attachment, container, false);
        ButterKnife.bind(this, contentView);

        this.setupRecyclerView();
        presenter.setViewAttachmentTab(this);
        presenter.getAttachmentList(ticket.getTicketId());

        adapter.setAttachments(ticket.getAttachments());
        compEmpty.setVisibility(ticket.getAttachments().isEmpty() ? View.VISIBLE : View.GONE);

        return contentView;
    }

    /**
     * @param attachmentDocLinksList: list  - contains attachment properties of ticket
     */
    public void loadAttachmentDocLinksList(List<DocLinks> attachmentDocLinksList) {
        adapter.setAttachmentDetails(attachmentDocLinksList);
    }

    /**
     * @param attachmentList - refresh attachment page
     */
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
     * @param doclinksID OnClick on download icon - attachment tab
     */
    public void setOnClickDownloadButton(String doclinksID) {
        presenter.getFileDetails(ticket.getTicketId(), doclinksID);
    }

}
