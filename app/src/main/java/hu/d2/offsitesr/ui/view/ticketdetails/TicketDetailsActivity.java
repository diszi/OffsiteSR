package hu.d2.offsitesr.ui.view.ticketdetails;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Base64OutputStream;
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
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.model.TicketHolder;
import hu.d2.offsitesr.ui.view.component.SavePictureDialog;
import hu.d2.offsitesr.util.FileUtils;
import hu.d2.offsitesr.util.UIConstans;

import static android.R.attr.data;
import static android.R.attr.endColor;

public class TicketDetailsActivity extends AppCompatActivity implements TicketDetails {

	private TicketDetailsPresenter presenter;
	private ServiceRequestEntity ticket;
	private TicketHolder ticketHolder;

	private  SavePictureDialog savePictureDialog;

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

		savePictureDialog = new SavePictureDialog();

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

		viewPager.setAdapter(adapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		String encodedFile="", path="", fileName="", fileNameWithoutExtension="";
		Uri selectedFileUri= null;
		int pos;

		if (requestCode == 0 && resultCode == RESULT_OK && null != data && data.getData() != null) {
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

		} //UPLOAD
		else
			if (requestCode == 1 && resultCode ==RESULT_OK &&  data.getData() == null){
				android.app.FragmentManager fm = this.getFragmentManager();
				Bundle extras = data.getExtras();
				savePictureDialog.setArguments(extras);
				savePictureDialog.show(fm,"SaveAndUploadPicture");

			} //TAKE PICTURE

	}


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


	public void addFile(String fileName ,String pureFileName,String encode, String urlname){
		if (fileName.length() > 20){
			fileName = fileName.substring(0, 20);
		}
		presenter.addFile(ticket.getTicketId(), fileName, pureFileName, encode, urlname);

	}



	@Override
	public void addWorkLogRemote(String shortDesc, String longDesc) {

		presenter.addWorkLogRemote(ticket.getTicketId(),getLoggedInUser(),shortDesc,longDesc);
	}
}
