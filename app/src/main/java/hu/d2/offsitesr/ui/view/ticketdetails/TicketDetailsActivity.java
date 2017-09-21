package hu.d2.offsitesr.ui.view.ticketdetails;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.model.TicketHolder;
import hu.d2.offsitesr.ui.model.WorkLog;
import hu.d2.offsitesr.ui.view.component.AddWorkLogDialog;
import hu.d2.offsitesr.ui.view.component.ChooseOwnerDialog;
import hu.d2.offsitesr.ui.view.component.ChooseStatusDialog;
import hu.d2.offsitesr.ui.view.component.VerticalSpaceItemDecoration;
import hu.d2.offsitesr.util.UIConstans;

public class TicketDetailsActivity extends AppCompatActivity implements TicketDetails {

	private TicketDetailsPresenter presenter;
	private TicketDetailsWorkLogAdapter adapter;
	private ServiceRequestEntity ticket;
	private TicketHolder ticketHolder;

	private ChooseStatusDialog chooseStatusDialog;
	private ChooseOwnerDialog chooseOwnerDialog;
	private AddWorkLogDialog addWorkLogDialog;

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
	@BindView(R.id.actDetails_owner)
	TextView compOwner;
	@BindView(R.id.actDetails_worklog)
	TextView compWorkLog;

	@BindView(R.id.actDetails_workLogList)
	RecyclerView compWorkLogs;
	@BindView(R.id.actDetails_addWorkLogButton)
	ImageButton compAddWorklogButton;
	@BindView(R.id.actDetails_editStatusButton)
	ImageButton compEditStatusButton;
	@BindView(R.id.actDetails_editOwnerButton)
	ImageButton compEditOwnerButton;

	@BindView(R.id.actDetails_progressBar)
	ProgressBar compProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ticketHolder =(TicketHolder) getIntent().getExtras()
				.get(TicketHolder.SERIALIZABLE_NAME) ;
		ticket = ticketHolder.getEntity();


		setContentView(R.layout.activity_ticket_details);
		ButterKnife.bind(this);
		this.setupRecyclerView();

		presenter = new TicketDetailsPresenterImpl();
		presenter.setView(this);

		chooseStatusDialog = new ChooseStatusDialog();
		chooseStatusDialog.setView(this);
		chooseOwnerDialog = new ChooseOwnerDialog();
		chooseOwnerDialog.setView(this);
		addWorkLogDialog = new AddWorkLogDialog();
		addWorkLogDialog.setView(this);

		loadTicketDetails(ticket);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		presenter.onDestroy();
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.putExtra(TicketHolder.SERIALIZABLE_NAME,ticketHolder);
		setResult(RESULT_OK,intent);
		super.onBackPressed();
	}

	@Override
	public void loadTicketDetails(ServiceRequestEntity entity) {
		compId.setText(entity.getTicketId());
		compDescription.setText(entity.getDescription());
		compStatus.setText(entity.getStatus());
		compReportDate.setText(entity.getReportDate());
		compReportedBy.setText(entity.getReportedBy());
		compAffectedPerson.setText(entity.getAffectedPerson());
		compClassStructure.setText(entity.getClassStructure());
		compPriority.setText(entity.getPriority());
		compOwner.setText(entity.getOwner());
		List<WorkLog> workLogs = entity.getWorkLogs();
		if (!workLogs.isEmpty()) {
			compWorkLog.setText(getString(R.string.actDetails_worklog) + " (" + workLogs.size() + ") :");
		}

		adapter.setWorkLogs(workLogs);
	}

	@Override
	public void showLoading() {
		compProgressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideLoading() {
		compProgressBar.setVisibility(View.GONE);
	}

	@Override
	public void showErrorMessage(int messageID) {
		Toast.makeText(this, messageID, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showSuccessMessage() {
		Toast.makeText(this, getString(R.string.actDetails_saveSuccess), Toast.LENGTH_SHORT).show();
	}

	private void setupRecyclerView() {
		Context context = getApplicationContext();
		LinearLayoutManager layoutManager = new LinearLayoutManager(context);
		VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(
				compWorkLogs.getContext(), layoutManager.getOrientation(), 20);
		this.adapter = new TicketDetailsWorkLogAdapter(this);
		this.compWorkLogs.setLayoutManager(layoutManager);
		this.compWorkLogs.addItemDecoration(verticalSpaceItemDecoration);
		this.compWorkLogs.setAdapter(this.adapter);

	}

	@OnClick(R.id.actDetails_editStatusButton)
	public void onClickChooseStatusButton() {
        FragmentManager fm = getFragmentManager();

        chooseStatusDialog.show(fm,"chooseStatus");
	}

    @OnClick(R.id.actDetails_editOwnerButton)
    public void onClickChooseOwnerButton() {
        FragmentManager fm = getFragmentManager();

        chooseOwnerDialog.show(fm,"chooseOwner");
    }

	@OnClick(R.id.actDetails_addWorkLogButton)
	public void onClickAddWorkLogButton() {
		FragmentManager fm = getFragmentManager();

		addWorkLogDialog.show(fm,"addWorkLog");
	}

	public String getLoggedInUser() {
		return PreferenceManager.getDefaultSharedPreferences(this).getString(UIConstans.LOGGED_IN_USER, "Unknown");
	}

	@Override
	public void updateStatusRemote(String status) {
		presenter.updateStatusRemote(ticket.getTicketId(),status);
	}

	@Override
	public void updateStatus(String newStatus) {
        ticket.setStatus(newStatus);
		compStatus.setText(newStatus);
		ticketHolder.setChanged(true);
	}

    @Override
    public void updateOwner(String newOwner) {
        ticket.setOwner(newOwner);
        compOwner.setText(newOwner);
		ticketHolder.setChanged(true);
    }

    @Override
    public void updateOwnerRemote(String owner) {
        presenter.updateOwnerRemote(ticket.getTicketId(),owner);
    }

	@Override
	public void addWorkLogRemote(String shortDesc, String longDesc) {
		presenter.addWorkLogRemote(ticket.getTicketId(),getLoggedInUser(),shortDesc,longDesc);
	}
}
