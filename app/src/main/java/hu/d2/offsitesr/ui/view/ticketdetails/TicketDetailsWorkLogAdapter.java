package hu.d2.offsitesr.ui.view.ticketdetails;

import android.support.v7.widget.RecyclerView;
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

	private ArrayList<WorkLog> workLogs = new ArrayList<>();

//	private TicketDetails view;

	public TicketDetailsWorkLogAdapter() {
	}

	public void setWorkLogs(List<WorkLog> tickets) {
		this.workLogs.clear();
		this.workLogs.addAll(tickets);
		this.notifyDataSetChanged();
	}

	@Override
	public WorkLogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_ticket_details_worklog_row,
				parent, false);
		return new WorkLogViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(WorkLogViewHolder holder, int position) {
		WorkLog workLog = workLogs.get(position);
        holder.bind(workLog);
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

		@BindView(R.id.actDetailsWorklog_ticketId)
		TextView compTicketId;
		@BindView(R.id.actDetailsWorklog_class)
		TextView compClass;
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
			compTicketId.setText(workLog.getId());
			compClass.setText(workLog.getRecordKey());
			compCreatedBy.setText(workLog.getCreatedBy());
			compCreatedDate.setText(workLog.getCreatedDate());
			compSummary.setText(workLog.getDescription());
		}
	}
}
