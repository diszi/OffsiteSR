package hu.d2.offsitesr.ui.view.ticketlist;


import android.content.Context;
import android.content.Intent;


import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.app.PropertySettings;
import hu.d2.offsitesr.app.info.AboutAppActivity;
import hu.d2.offsitesr.app.singleton.SettingsSingleton;
import hu.d2.offsitesr.app.singleton.TimerSingleton;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.model.TicketHolder;
import hu.d2.offsitesr.ui.model.Version;
import hu.d2.offsitesr.ui.view.base.BaseActivity;
import hu.d2.offsitesr.ui.view.component.ChooseStatusDialog;
import hu.d2.offsitesr.ui.view.component.OnBackPressedDialog;
import hu.d2.offsitesr.ui.view.component.UpdateAppDialog;
import hu.d2.offsitesr.ui.view.component.VerticalSpaceItemDecoration;
import hu.d2.offsitesr.ui.view.login.LoginActivity;
import hu.d2.offsitesr.ui.view.settings.SettingsActivity;
import hu.d2.offsitesr.ui.view.ticketdetails.TicketDetailsActivity;
import hu.d2.offsitesr.ui.view.verifications.UpdateActivity;
import hu.d2.offsitesr.ui.view.verifications.UpdateApp;
import hu.d2.offsitesr.ui.view.verifications.VerificationPresenter;
import hu.d2.offsitesr.ui.view.verifications.VerificationPresenterImpl;
import hu.d2.offsitesr.util.EnvironmentTool;


public class TicketListActivity extends BaseActivity implements  TicketList,UpdateApp{

    public static int TICKET_REQUEST_CODE = 0;

    private TicketListPresenter presenter;
    private VerificationPresenter presenterVerification ;
    private TicketListAdapter ticketListAdapter;
    private List<ServiceRequestEntity> ticketList;
    private String  syncDateString;
    private String newAppVersion ;

    UpdateActivity updateActivity;
    ChooseStatusDialog chooseStatusDialog;

    @BindView(R.id.actList_toolbar)
    Toolbar compToolBar;
    @BindView(R.id.actList_recyclerView)
    RecyclerView compRecyclerView;
    @BindView(R.id.actList_swipeRefreshLayout)
    SwipeRefreshLayout compSwipeRefreshLayout;
    @BindView(R.id.actList_progressBar)
    ProgressBar compProgressBar;
    @BindView(R.id.actList_userName)
    TextView compUserName;
    @BindView(R.id.actList_syncDate)
    TextView compSyncDate;

    /**
     * @param savedInstanceState - contain the activity previously frozen state, if there was one.
     * Called when the activity is first created.
     * Initialize activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);


        Log.d("------------------>","Start Activity");
        ButterKnife.bind(this);

        updateActivity = new UpdateActivity();
        chooseStatusDialog  = new ChooseStatusDialog();

        EnvironmentTool.setLanguage(this,SettingsSingleton.getInstance().getLanguage());
        setScreenLock();
        TimerSingleton.getInstance().setMyActivity(this);

        this.setupRecyclerView();

        presenter = new TicketListPresenterImpl();
        presenter.setView(this);

        presenterVerification = new VerificationPresenterImpl();

        String loggidUserName = getLoggedInUser();
        compUserName.setText(loggidUserName);

        compSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                compSwipeRefreshLayout.setRefreshing(false);
                presenter.getTicketList();
                setScreenLock();
                setSupportActionBar(compToolBar);
            }
        });

//        if (PropertySettings.BACKGROUND_COLOR != -1){
//            compToolBar.setBackgroundColor(PropertySettings.BACKGROUND_COLOR);
//        }


        setSupportActionBar(compToolBar);
        presenter.getTicketList();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.ticketList!=null && this.ticketList.size() != 0){
            ServiceRequestEntity ticket = this.ticketList.get(0);
        }
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     */
    @Override
    public void onBackPressed() {
        android.app.FragmentManager fm = getFragmentManager();
        OnBackPressedDialog alertDialogFragment = new OnBackPressedDialog();
        alertDialogFragment.show(fm,"AlertDialogFragment");
    }

    /**
     * Called whenever a key, touch, or trackball event is dispatched to the activity.
     *  - user interaction on screen - reset timer counter
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
//    @Override
//    protected void onUserLeaveHint() {
//        super.onUserLeaveHint();
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        presenter.onDestroy();
//    }

    @Override
    public void showLoading() {
        Log.d("------------------>"," Show Loading");
        compProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        Log.d("------------------>"," Hide Loading");
        compProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorMessage(int messageID) {
        Toast.makeText(this, messageID, Toast.LENGTH_SHORT).show();
    }

    /**
     *  - loading the details of the ticket
     */
    @Override
    public void launchDetailsView(TicketHolder entityHolder) {
        Intent intent = new Intent(this, TicketDetailsActivity.class);
        intent.putExtra(TicketHolder.SERIALIZABLE_NAME,entityHolder);
        startActivityForResult(intent,TICKET_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TICKET_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                TicketHolder ticketHolder = (TicketHolder) data.getExtras().get(TicketHolder.SERIALIZABLE_NAME);
                if (ticketHolder.isChanged()){
                    ticketList.add(ticketHolder.getPosition(),ticketHolder.getEntity());
                    this.loadList(ticketList);
                }
            }
        }
    }

    /**
     * @return username
     */
    public String getLoggedInUser()
    {
        return SettingsSingleton.getInstance().getUserName();
    }

    /**
     * @param ticketList
     *  - loading @param
     */
    @Override
    public void loadList(List<ServiceRequestEntity> ticketList){
        this.ticketList = ticketList;
        ticketListAdapter.setTicketList(this.ticketList);
        compToolBar.setTitle(getString(R.string.actList_title)+" ("+this.ticketList.size()+")");

    }


    private void setupRecyclerView() {
        Context context = getApplicationContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(
                compRecyclerView.getContext(), layoutManager.getOrientation(), 20);
        this.ticketListAdapter = new TicketListAdapter(this);
        this.compRecyclerView.setLayoutManager(layoutManager);
        this.compRecyclerView.addItemDecoration(verticalSpaceItemDecoration);
        this.compRecyclerView.setAdapter(this.ticketListAdapter);
    }


    /**
     * Initialize the contents of the Activity's standard options menu.
     * @param menu - menu layout initialization
     * @return - You must return true for the menu to be displayed; if you return false it will not be shown.
     *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.act_ticket_list,menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * This hook is called whenever an item in your options menu is selected.
     * @param item - initialization
     * @return - boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuTicketList_history:
                return true;
            case R.id.menuTicketList_logs:
                return true;
            case R.id.menuTicketList_settings:
                this.launchSettings();
                return true;
            case R.id.menuTicketList_update:
                this.launchUpdate();
                return true;
            case R.id.menuTicketList_about:
                this.launchAboutApp();
                return true;
            case R.id.menuTicketList_log_out:
                this.launchLoginView();
                return true;
            default:
                this.showErrorMessage(R.string.error_noOption);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *   Log out  => launch login page
     */
    public void launchLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     *  Launch settings page
     */
    public void launchSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     *  Launch Update -> check the application update
     */
    public void launchUpdate(){
        presenterVerification.getUpdateVersion(EnvironmentTool.getAppPackageName(),this);
    }

    /**
     *  Launch about page -> presenting general information about the application
     */
    public void launchAboutApp(){
        Intent intent = new Intent(this,AboutAppActivity.class);
        startActivity(intent);
    }

    /**
     * @param updateVersionObj  - object which contains information about new app
     *   If @param doesn't contain information about new app => no updates
     *   If @param != null => new version is available => alert dialog
     */
    @Override
    public void verificUpdateInformations(Version updateVersionObj) throws ParseException {
        newAppVersion = updateVersionObj.getVersionNumber();
        if ( updateVersionObj.getAppName() == null){
            updateActivity.setUpdateAvailable(false);
            Toast.makeText(this,getString(R.string.updateTab_unavailableUpdate),Toast.LENGTH_SHORT).show();
        }else
        {
            if (updateActivity.compareVersionNames(EnvironmentTool.getVersionApp(),newAppVersion) == -1){
                updateActivity.setUpdateAvailable(true);
                android.app.FragmentManager fm = getFragmentManager();
                UpdateAppDialog updateDialog = UpdateAppDialog.newInstance("TicketListActivity",newAppVersion);
                updateDialog.show(fm, "update");
            }else
            {
                updateActivity.setUpdateAvailable(false);
                Toast.makeText(this,getString(R.string.updateTab_unavailableUpdate),Toast.LENGTH_SHORT).show();
            }
        }


    }

    /**
     *  Download new app, if the user wants to update app
     */
    @Override
    public void downloadApp() {
        presenterVerification.getNewApp(EnvironmentTool.getAppPackageName(),newAppVersion,this);
    }


    /**
     *  Runtime method - thread
     *  Real-time synchronization
     */
    @Override
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


    public void setScreenLock(){
          if (SettingsSingleton.getInstance().getScreenLockValue() == false){
            EnvironmentTool.setScreenLockOff(this);
        }
        else
        {
            EnvironmentTool.setScreenLockOn(this);
        }

    }

}
