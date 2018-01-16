package hu.d2.offsitesr.ui.view.ticketdetails;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.support.v4.view.ViewPager;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;


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
import hu.d2.offsitesr.util.UIConstans;

import static android.R.attr.data;
import static android.R.attr.endColor;
import static android.R.attr.manageSpaceActivity;

public class TicketDetailsActivity extends AppCompatActivity implements TicketDetails {


	private TicketDetailsPresenter presenter;
	private ServiceRequestEntity ticket;
	private TicketHolder ticketHolder;
	private  SavePictureDialog savePictureDialog;
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
		TimerSingleton.getInstance().timerStart();

		setContentView(R.layout.activity_ticket_details);
		ButterKnife.bind(this);

		setSupportActionBar(compToolbar);
		getSupportActionBar().setTitle(ticket.getTicketId());
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		savePictureDialog = new SavePictureDialog();
        compSyncDate.setText(syncDateString);

		presenter = new TicketDetailsPresenterImpl();
		presenter.setView(this);

		compTabLayout.setupWithViewPager(compTabViewPager);

		addTabFragmentsToViewPager(compTabViewPager);

		compTabLayout.getTabAt(3).setIcon(tabAttachmentIcon[0]);

		loadTicketDetails(ticket);

	}


	/**
	 *  - add 4 fragments page to viewPager
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		String encodedFile="", path="", fileName="", fileNameWithoutExtension="";
		Uri selectedFileUri= null;
		int pos;

		if (requestCode == 0 && resultCode == RESULT_OK && null != data && data.getData() != null) {
			//UPLOAD FILE
			selectedFileUri =data.getData();
			path = FileUtils.getPath(this, selectedFileUri);
			Toast.makeText(TicketDetailsActivity.this,getString(R.string.assDetails_fileSelected)+path,Toast.LENGTH_SHORT).show();
        	File file = new File(path);
			fileName = file.getName();
			pos = fileName.lastIndexOf(".");
			if (pos > 0){
				fileNameWithoutExtension = fileName.substring(0,pos);
			}
			encodedFile = encodeFile(selectedFileUri);
			this.addFile(fileName, fileNameWithoutExtension, encodedFile, path);

		}
		else
			if (requestCode == 1 && resultCode ==RESULT_OK &&  data.getData() == null) {
				//TAKE PICTURE + SAVE
				android.app.FragmentManager fm = this.getFragmentManager();
				Bundle extras = data.getExtras();
				savePictureDialog.setArguments(extras);
				savePictureDialog.show(fm, "SaveAndUploadPicture");
			}

	}

	/**
	 *
	 * @param fileUri : uri for selected file
	 * @return : base64 code in string
	 */
	public String encodeFile(Uri fileUri){
		String encodeBase64 = "";
		try {
			InputStream inputStream  = getContentResolver().openInputStream(fileUri);
			byte[] inputData = getBytes(inputStream);
			encodeBase64 = Base64.encodeToString(inputData,Base64.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return encodeBase64;

	}

	/**
	 *  - convert InputStream parameter into byte format
	 *
	 * @param inputStream
	 * @return : byte
	 * @throws IOException
	 */
	public byte[] getBytes(InputStream inputStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int buffSize = 1024;
		byte[] buffer = new byte[buffSize];
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1){
			baos.write(buffer,0,len);
		}

		return baos.toByteArray();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		presenter.onDestroy();
	}

	/**
	 * 	- backstep in ticket list
	 */
	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.putExtra(TicketHolder.SERIALIZABLE_NAME,ticketHolder);
		setResult(RESULT_OK,intent);
		super.onBackPressed();
	}

	/*
	* 	 - called when the user has interacted with the device while activity is running
	*/
	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		TimerSingleton.getInstance().timerStop();
		TimerSingleton.getInstance().timerStart();
	}

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
	 *
	 * @return logged user name
	 */
	public String getLoggedInUser() {
		return SettingsSingleton.getInstance().getUserName();
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


	/**
	 *
	 *	Attachment is added to the ticket
	 *
	 * @param fileName :  uploaded file/photo name
	 * @param pureFileName: file name without extension
	 * @param encode: base64 code
	 * @param urlname: file path
	 *
	 */
	public void addFile(String fileName ,String pureFileName,String encode, String urlname){

		if (fileName.length() > 20){
			fileName = fileName.substring(0, 19);
		}

		presenter.addFile(ticket.getTicketId(), fileName, pureFileName, encode, urlname);

	}


	/**
	 *	Worklog is added to the ticket
	 *
	 * @param shortDesc
	 * @param longDesc
	 *
	 */
	@Override
	public void addWorkLogRemote(String shortDesc, String longDesc) {
		presenter.addWorkLogRemote(ticket.getTicketId(),getLoggedInUser(),shortDesc,longDesc);
	}

	/**
	 *
	 * 	- set screen lock to the page
	 *
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
	 *
	 * 	- set actual date
	 * 	
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
