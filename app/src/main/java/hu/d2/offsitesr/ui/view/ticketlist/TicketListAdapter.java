package hu.d2.offsitesr.ui.view.ticketlist;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
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


    public TicketListAdapter(TicketList view)
    {
        this.view = view;
    }

    public void setTicketList(List<ServiceRequestEntity> ticketList) {
        Log.d(" ----> ","Ticket List refresh");
        this.ticketList = ticketList;
        this.notifyDataSetChanged();
    }

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
     * @param parent - the ViewGroup into which the new View will be added after it is bound to an adapter position
     * @param viewType- the view type of the new View
     * @return - a new ViewHolder that holds a View of the given view type
     */
    @Override
	public TicketListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_ticket_list_row,parent,false);
        return new TicketListViewHolder(itemView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the itemView to reflect
     * the item at the given position.
     * @param holder - the ViewHolder which should be updated to represent
     *              the contents of the item at the given position in the data set.
     * @param position - the position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(TicketListViewHolder holder, int position) {
        ServiceRequestEntity ticket = ticketList.get(position);
        holder.bind(ticket);
        holder.itemView.setOnClickListener((v)-> view.launchDetailsView(new TicketHolder(ticket,position)));
    }

    /**
     * @return - the total number of items in the data set held by the adapter.
     */
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

        /**
         *  Set text with value
         * @param ticket - details of the ticket
         */
        public void bind(ServiceRequestEntity ticket){
            compStatusIcon.setImageResource(getStatusIcon(ticket.getStatus()));

            if (ticket.getPriority().equals("1")){ //1 = critical
                compFirstRow.setTextColor(Color.RED);
            }else
            {
                compFirstRow.setTextColor(Color.BLACK);
            }
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
                    result = R.mipmap.ic_status_closed;
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
