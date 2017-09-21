package hu.d2.offsitesr.ui.view.ticketlist;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.model.TicketHolder;

/**
 * Created by csabinko on 2017.09.15..
 */

public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.TicketListViewHolder>{

    private List<ServiceRequestEntity> ticketList = new ArrayList<>();

    private TicketList view;

    public TicketListAdapter(TicketList view) {
        this.view = view;
    }

    public void setTicketList(List<ServiceRequestEntity> ticketList) {
        Log.d(" ----> ","Ticket List refresh");
//        this.ticketList.clear();
//        this.ticketList.addAll(ticketList);
        this.ticketList = ticketList;
        this.notifyDataSetChanged();
    }

    @Override
	public TicketListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_ticket_list_row,parent,false);
        return new TicketListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TicketListViewHolder holder, int position) {
        ServiceRequestEntity ticket = ticketList.get(position);
        holder.bind(ticket);
        holder.itemView.setOnClickListener((v)-> view.launchDetailsView(new TicketHolder(ticket,position)));
    }

    @Override
    public int getItemCount() {
        if (ticketList != null && ticketList.size() > 0) {
            return ticketList.size();
        } else {
            return 0;
        }
    }

    static class TicketListViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.actListRow_statusIcon)
        ImageView compStatusIcon;
        @BindView(R.id.actListRow_first)
        TextView compFirstRow;
        @BindView(R.id.actListRow_second)
        TextView compSecondRow;
        @BindView(R.id.actListRow_third)
        TextView compThirdRow;
        @BindView(R.id.actListRow_syncIcon)
        ImageView compSyncIcon;

        public TicketListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(ServiceRequestEntity ticket){
            compStatusIcon.setImageResource(getStatusIcon(ticket.getStatus()));
            compFirstRow.setText(ticket.getTicketId()+" - "+ticket.getAffectedPerson());
            compSecondRow.setText(ticket.getDescription());
            compThirdRow.setText(ticket.getStatusDate());

        }

        public static int getStatusIcon(String text){
            int result = R.mipmap.ic_status_new;
            switch (text){
                case "INPROG":
                    result = R.mipmap.ic_status_inprogress;
                    break;
                case "NEW":
                    result = R.mipmap.ic_status_new;
                    break;
                case "RESOLVED":
                    result = R.mipmap.ic_status_resolved;
                    break;
                case "APPLM":
                    result = R.mipmap.ic_status_slahold;
                    break;
                case "CLOSED":
                    result = R.mipmap.ic_status_resolved;
                    break;
                case "QUEUED":
                    result = R.mipmap.ic_status_queued;
                    break;
                case "APPR":
                    result = R.mipmap.ic_status_resolved;
                    break;
                case "PENDING":
                    result = R.mipmap.ic_status_pending;
                    break;
            }
            return result;

        }
    }


}
