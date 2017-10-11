package hu.d2.offsitesr.ui.view.ticketdetails;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.model.TicketHolder;
import hu.d2.offsitesr.util.UIConstans;

public class TicketDetailsActivity extends AppCompatActivity implements TicketDetails {

	private TicketDetailsPresenter presenter;
	private ServiceRequestEntity ticket;
	private TicketHolder ticketHolder;

	private String syncDateString;

	TicketDetailsTab ticketDetailsTab;

	TicketDetailsWorkLogTab workLogTab;

	TicketDetailsTaskTab taskTab;



	@BindView(R.id.actDetails_progressBar)
	ProgressBar compProgressBar;

	@BindView(R.id.actDetails_userName)
	TextView compUserName;
	@BindView(R.id.actDetails_syncDate)
	TextView compSyncDate;

	@BindView(R.id.actDetails_tabContainer)
	ViewPager compTabViewPager;

	@BindView(R.id.actDetails_tabLayout)
	TabLayout compTabLayout;

    @BindView(R.id.actDetails_toolbar)
    Toolbar compToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ticketHolder =(TicketHolder) getIntent().getExtras()
				.get(TicketHolder.SERIALIZABLE_NAME) ;
		ticket = ticketHolder.getEntity();
		syncDateString = getIntent().getStringExtra(UIConstans.SYNC_DATE);

		setContentView(R.layout.activity_ticket_details);
		ButterKnife.bind(this);

        setSupportActionBar(compToolbar);
        getSupportActionBar().setTitle(ticket.getTicketId());
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);


        compSyncDate.setText(syncDateString);

		presenter = new TicketDetailsPresenterImpl();
		presenter.setView(this);

		compTabLayout.setupWithViewPager(compTabViewPager);
		addTabFragmentsToViewPager(compTabViewPager);

		// recyclerView manual height settings scroll to down automatically, it disable that
//		compscrollview.smoothScrollBy(0,0);

		loadTicketDetails(ticket);
	}

	private void addTabFragmentsToViewPager(ViewPager viewPager) {
		TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager());
		Bundle bundle = new Bundle();
		bundle.putSerializable(ServiceRequestEntity.SERIALIZABLE_NAME,ticket);

		ticketDetailsTab = new TicketDetailsTab();
		ticketDetailsTab.setArguments(bundle);
		adapter.addTab(ticketDetailsTab, getResources().getString(R.string.actDetails_title));

		workLogTab = new TicketDetailsWorkLogTab();
		workLogTab.setArguments(bundle);
		adapter.addTab(workLogTab,getString(R.string.actDetails_worklog) + " (" + ticket.getWorkLogs().size() + ")");

		taskTab = new TicketDetailsTaskTab();
		taskTab.setArguments(bundle);
		adapter.addTab(taskTab,getString(R.string.actDetails_task) + " (" + ticket.getTasks().size() + ")");


//		adapter.addFragment(new ContactsFragment(), "Contacts");
		viewPager.setAdapter(adapter);
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
//		compId.setText(entity.getTicketId());
//		compDescription.setText(entity.getDescription());
//		compStatus.setText(entity.getStatus());
//		compReportDate.setText(entity.getReportDate());
//		compReportedBy.setText(entity.getReportedBy());
//		compAffectedPerson.setText(entity.getAffectedPerson());
//		compClassStructure.setText(entity.getClassStructure());
//		compPriority.setText(HolderSingleton.getInstance().getPriorityValue(entity.getPriority()));
//		compOwnerGroup.setText(entity.getOwnerGroup());
//		compOwner.setText(entity.getOwner());
//		List<WorkLog> workLogs = entity.getWorkLogs();
//		if (!workLogs.isEmpty()) {
//			compWorkLog.setText(getString(R.string.actDetails_worklog) + " (" + workLogs.size() + ") :");
//		}

		String username = getLoggedInUser();
		compUserName.setText(username);
//
//		adapter.setWorkLogs(workLogs);
//
//		// Manually set height
//		int newHeight = workLogs.size() * 195;
//		ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,newHeight);
//		compWorkLogs.setLayoutParams(layoutParams);
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
		ticketDetailsTab.updateStatus(newStatus);
		ticketHolder.setChanged(true);
	}

	@Override
	public void updateOwnerGroup(String newOwnerGroup) {
		ticket.setOwnerGroup(newOwnerGroup);
		ticketDetailsTab.updateOwnerGroup(newOwnerGroup);
		ticketHolder.setChanged(true);
	}

    @Override
    public void updateOwner(String newOwner) {
        ticket.setOwner(newOwner);
		ticketDetailsTab.updateOwner(newOwner);
		ticketHolder.setChanged(true);
    }

	@Override
	public void updateOwnerGroupRemote(String ownerGroup) {
		presenter.updateOwnerGroupRemote(ticket.getTicketId(),ownerGroup);
	}

    @Override
    public void updateOwnerRemote(String owner) {
        presenter.updateOwnerRemote(ticket.getTicketId(),owner);
    }

	@Override
	public void updatePriorityRemote(String priority) {
		presenter.updatePriorityRemote(ticket.getTicketId(),priority);
	}

	@Override
	public void updatePriority(String newPriority) {
		ticket.setPriority(newPriority);
		ticketDetailsTab.updatePriority(newPriority);
		ticketHolder.setChanged(true);
	}

	@Override
	public void addWorkLogRemote(String shortDesc, String longDesc) {
		presenter.addWorkLogRemote(ticket.getTicketId(),getLoggedInUser(),shortDesc,longDesc);
	}
}
