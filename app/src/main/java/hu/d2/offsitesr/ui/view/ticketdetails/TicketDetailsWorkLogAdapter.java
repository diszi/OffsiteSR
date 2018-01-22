package hu.d2.offsitesr.ui.view.ticketdetails;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.ui.model.WorkLog;


/**
 * Created by csabinko on 2017.09.18..
 */

public class TicketDetailsWorkLogAdapter extends RecyclerView.Adapter<TicketDetailsWorkLogAdapter.WorkLogViewHolder> {

	private List<WorkLog> workLogs = new ArrayList<>();
	private TicketDetailsWorkLogTab ticketDetailsWorkLogTab;


	public TicketDetailsWorkLogAdapter(TicketDetailsWorkLogTab ticketDetailsWorkLogTab) {
		this.ticketDetailsWorkLogTab = ticketDetailsWorkLogTab;
	}

	public void setWorkLogs(List<WorkLog> tickets) {
		this.workLogs.clear();
		this.workLogs.addAll(tickets);
		this.notifyDataSetChanged();
	}


	public void setWorkLogsRefresh(List<WorkLog> worklogList){
		Log.d("------------------>","Worklog List refresh");
		this.workLogs = worklogList;
		this.notifyDataSetChanged();
		ticketDetailsWorkLogTab.compEmpty.setVisibility(worklogList.isEmpty()?View.VISIBLE:View.GONE);
	}


	@Override
	public WorkLogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_ticket_details_worklog_row,
				parent, false);
		return new WorkLogViewHolder(itemView);
	}

	/**
	 *
	 * @param holder
	 * @param position
	 *
	 * 	Long Onclick action on item in @param position
	 */
	@Override
	public void onBindViewHolder(WorkLogViewHolder holder, int position) {

		WorkLog workLog = workLogs.get(position);
        holder.bind(workLog);
		holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				ticketDetailsWorkLogTab.onClickLongDescButton(workLog);
				return true;
			}
		});
	}


	@Override
	public int getItemCount() {
        if (workLogs != null && workLogs.size() > 0) {
            return workLogs.size();
        } else {
            return 0;
        }
	}

	static class WorkLogViewHolder extends RecyclerView.ViewHolder {


		@BindView(R.id.actDetailsWorklog_createdBy)
		TextView compCreatedBy;
		@BindView(R.id.actDetailsWorklog_createdDate)
		TextView compCreatedDate;
		@BindView(R.id.actDetailsWorklog_summary)
		TextView compSummary;


		public WorkLogViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		public void bind(WorkLog workLog) {
			compCreatedBy.setText(workLog.getCreatedBy());
			compCreatedDate.setText(workLog.getCreatedDate());
			compSummary.setText(workLog.getDescription());

		}


	}
}
