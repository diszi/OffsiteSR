package hu.d2.offsitesr.ui.view.ticketdetails;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.app.singleton.SettingsSingleton;
import hu.d2.offsitesr.app.singleton.TimerSingleton;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.model.TicketHolder;
import hu.d2.offsitesr.ui.view.component.SavePictureDialog;
import hu.d2.offsitesr.ui.view.ticketlist.TicketListActivity;
import hu.d2.offsitesr.ui.view.ticketlist.TicketListPresenter;
import hu.d2.offsitesr.ui.view.ticketlist.TicketListPresenterImpl;
import hu.d2.offsitesr.util.EnvironmentTool;
import hu.d2.offsitesr.util.FileUtils;

/**
 * 	This activity set viewPager with details for ticket.
 * 	The ViewPager build from 4 fragments, the last fragment title is an icon.
 */

public class TicketDetailsActivity extends AppCompatActivity implements TicketDetails {


	private TicketDetailsPresenter presenter;
	private TicketListPresenter presenterTicketList;
	private ServiceRequestEntity ticket;
	private TicketHolder ticketHolder;
	private String syncDateString;
	private int[] tabAttachmentIcon ={
			R.drawable.ic_attachment_noti
	};

	TicketDetailsTab ticketDetailsTab;
	TicketDetailsWorkLogTab workLogTab;
	TicketDetailsTaskTab taskTab;
	TicketDetailsAttachmentTab attachmentTab;


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


	/**
	 * @param savedInstanceState - contain the activity previously frozen state
	 * Setting all property: language, screen lock, date
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ticketHolder =(TicketHolder) getIntent().getExtras()
				.get(TicketHolder.SERIALIZABLE_NAME) ;
		ticket = ticketHolder.getEntity();

		setSyncDate();
		setScreenLock();
		EnvironmentTool.setLanguage(this,SettingsSingleton.getInstance().getLanguage());
		TimerSingleton.getInstance().setMyActivity(this);

		setContentView(R.layout.activity_ticket_details);
		ButterKnife.bind(this);

		setSupportActionBar(compToolbar);
		getSupportActionBar().setTitle(ticket.getTicketId());
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

        compSyncDate.setText(syncDateString);

		presenter = new TicketDetailsPresenterImpl();
		presenter.setView(this);

		presenterTicketList = new TicketListPresenterImpl();

		compTabLayout.setupWithViewPager(compTabViewPager);
		addTabFragmentsToViewPager(compTabViewPager);
		compTabLayout.getTabAt(3).setIcon(tabAttachmentIcon[0]);

		loadTicketDetails(ticket);

	}



	/**
	 *
	 * @param viewPager - layout manager that allows the user to flip left and right through pages of data
	 *	Set 4 fragments page to @param viewPager
	 *                  - ticket details list
	 *               	- ticket worklog
	 *               	- ticket task
	 *               	- ticket attachment
	 */
	private void addTabFragmentsToViewPager(ViewPager viewPager) {
		TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager());
		Bundle bundle = new Bundle();
		bundle.putSerializable(ServiceRequestEntity.SERIALIZABLE_NAME, ticket);

		ticketDetailsTab = new TicketDetailsTab();
		ticketDetailsTab.setArguments(bundle);
		adapter.addTab(ticketDetailsTab, getResources().getString(R.string.actDetails_title));

		workLogTab = new TicketDetailsWorkLogTab();
		workLogTab.setArguments(bundle);
		adapter.addTab(workLogTab, getString(R.string.actDetails_worklog) + " (" + (ticket.getWorkLogs().size())+ ")");

		taskTab = new TicketDetailsTaskTab();
		taskTab.setArguments(bundle);

		adapter.addTab(taskTab, getString(R.string.actDetails_task) + " (" + ticket.getTasks().size() + ")");

		attachmentTab = new TicketDetailsAttachmentTab();
		attachmentTab.setArguments(bundle);
		adapter.addTab(attachmentTab, null);

		viewPager.setAdapter(adapter);
	}


	/**
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 * Called when an activity you launched exits (upload file/photo or take photo), giving you the @param requestCode
	 * you started it with, the @param resultCode it returned, and any additional @param data from it.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		String encodedFile="", path="", fileName="";
		Uri selectedFileUri= null;

		if (requestCode == 0 && resultCode == RESULT_OK && null != data && data.getData() != null) {
			//UPLOAD FILE
			selectedFileUri =data.getData();
			path = FileUtils.getPath(this, selectedFileUri);
			Toast.makeText(TicketDetailsActivity.this,getString(R.string.assDetails_fileSelected)+path,Toast.LENGTH_SHORT).show();
        	File file = new File(path);
			fileName = file.getName();
			encodedFile = EnvironmentTool.encodeFile(selectedFileUri,this);

			this.addFile(fileName, fileName, encodedFile, path);
		}
		else

			if (requestCode == 1 && resultCode ==RESULT_OK ) {
				//TAKE PICTURE + SAVE
				android.app.FragmentManager fm = this.getFragmentManager();
				SavePictureDialog savePictureDialog = SavePictureDialog.newInstance(EnvironmentTool.mCurrentPhotoPath);
				savePictureDialog.show(fm,"SaveAndUploadPicture");

			}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		presenter.onDestroy();
	}

	/**
	 * Called when the activity has detected the user's press of the back key.
	 */
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this,TicketListActivity.class);
		startActivity(intent);
		super.onBackPressed();
	}

	/**
	 * Called whenever a key, touch, or trackball event is dispatched to the activity.
     *  - user interaction on screen - reset timer (stop timer + start timer)
     */
	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		TimerSingleton.getInstance().resetTimer();
	}

	/**
	 * Called as part of the activity lifecycle when an activity is
	 * about to go into the background as the result of user choice.
	 */
	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
	}

	/**
	 * @param entity - ticket details
	 * Add the username to the text field
	 */
	@Override
	public void loadTicketDetails(ServiceRequestEntity entity) {
		String loggidUserName = getLoggedInUser();
		compUserName.setText(loggidUserName);
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


	/**
	 * @return logged username
	 */
	public String getLoggedInUser() {
		return SettingsSingleton.getInstance().getUserName();
	}

	@Override
	public void updateStatusRemote(String status) {
//		System.out.println(" Activity ===> newSTatus = "+status);
		presenter.updateStatusRemote(ticket.getTicketId(),status);

	}

	@Override
	public void updateTaskStatusRemote(String status,int pos,String wonum, String siteid){
		presenter.updateTaskStatusRemote(ticket.getTicketId(),status,pos,wonum,siteid);
	}
	@Override
	public void updateStatus(String newStatus) {
        ticket.setStatus(newStatus);
		ticketDetailsTab.updateStatus(newStatus);
		ticketHolder.setChanged(true);

	}

	public void updateTaskStatus(String newStatus,int pos){
		taskTab.updateTaskStatus(newStatus,pos);
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
		presenterTicketList.getOwners(newOwner);

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

	/**
	 * @param shortDesc - worklog short description
	 * @param longDesc - worklog long description
	 * Worklog is added to the ticket
	 */
	@Override
	public void addWorkLogRemote(String shortDesc, String longDesc) {
		presenter.addWorkLogRemote(ticket.getTicketId(),getLoggedInUser(),shortDesc,longDesc);
	}


	/**
	 *	Attachment is added to the ticket
	 * @param generatedName -  uploaded file/photo name
	 * @param fileNameWithExtension - file name without extension
	 * @param encode - base64 code
	 * @param urlname - file path
	 *
	 */
	public void addFile(String generatedName ,String fileNameWithExtension,String encode, String urlname) {
		if (generatedName.length() > 20) {
			generatedName = generatedName.substring(0, 19);
		}
		presenter.addFile(ticket.getTicketId(), generatedName, fileNameWithExtension, encode, urlname);
	}



	/**
	 * 	Set screen lock to the page
	 */
	public void setScreenLock(){
		if (SettingsSingleton.getInstance().getScreenLockValue() == false){
			EnvironmentTool.setScreenLockOff(this);
		}
		else
		{
			EnvironmentTool.setScreenLockOn(this);
		}
	}

	/**
	 * Set actual date - real-time synchronization
	 */
	public void setSyncDate(){
		Thread t = new Thread(){
			public void run(){
				try{
					while(!isInterrupted()){
						Thread.sleep(1000);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								long date = System.currentTimeMillis();
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
								syncDateString = sdf.format(date);
								compSyncDate.setText(syncDateString);
							}
						});
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();

	}
}
