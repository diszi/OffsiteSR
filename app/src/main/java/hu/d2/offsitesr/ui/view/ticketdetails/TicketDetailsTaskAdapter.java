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
import hu.d2.offsitesr.ui.model.Task;

/**
 * Created by csabinko on 2017.09.18..
 */

public class TicketDetailsTaskAdapter extends RecyclerView.Adapter<TicketDetailsTaskAdapter.TaskViewHolder> {

	private ArrayList<Task> tasks = new ArrayList<>();

	public TicketDetailsTaskAdapter() {
	}

	public void setTasks(List<Task> tasks) {
		this.tasks.clear();
		this.tasks.addAll(tasks);
		this.notifyDataSetChanged();
	}

	@Override
	public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_ticket_details_task_row,
				parent, false);
		return new TaskViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(TaskViewHolder holder, int position) {
		Task task = tasks.get(position);
        holder.bind(task);
	}

	@Override
	public int getItemCount() {
        if (tasks != null && tasks.size() > 0) {
            return tasks.size();
        } else {
            return 0;
        }
	}

	static class TaskViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.actDetailsTask_activity)
		TextView compActivity;
		@BindView(R.id.actDetailsTask_summary)
		TextView compSummary;
		@BindView(R.id.actDetailsTask_owner)
		TextView compOwner;
		@BindView(R.id.actDetailsTask_ownerGroup)
		TextView compOwnerGroup;
		@BindView(R.id.actDetailsTask_location)
		TextView compLocation;
		@BindView(R.id.actDetailsTask_asset)
		TextView compAsset;
		@BindView(R.id.actDetailsTask_ci)
		TextView compCI;
		@BindView(R.id.actDetailsTask_status)
		TextView compStatus;


		public TaskViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		public void bind(Task task) {
			compActivity.setText(task.getActivity());
			compSummary.setText(task.getSummary());
			compOwner.setText(task.getOwner());
			compOwnerGroup.setText(task.getOwnerGroup());
			compLocation.setText(task.getLocation());
			compAsset.setText(task.getAsset());
			compCI.setText(task.getCi());
			compStatus.setText(task.getStatus());
		}
	}
}
