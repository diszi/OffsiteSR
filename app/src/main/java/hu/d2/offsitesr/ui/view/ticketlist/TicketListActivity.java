package hu.d2.offsitesr.ui.view.ticketlist;

import android.content.Context;
import android.content.Intent;

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

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.app.info.AboutAppActivity;
import hu.d2.offsitesr.app.singleton.SettingsSingleton;
import hu.d2.offsitesr.app.singleton.TimerSingleton;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.model.TicketHolder;
import hu.d2.offsitesr.ui.view.component.ChooseStatusDialog;
import hu.d2.offsitesr.ui.view.component.OnBackPressedDialog;
import hu.d2.offsitesr.ui.view.component.VerticalSpaceItemDecoration;
import hu.d2.offsitesr.ui.view.login.LoginActivity;
import hu.d2.offsitesr.ui.view.settings.SettingsActivity;
import hu.d2.offsitesr.ui.view.ticketdetails.TicketDetailsActivity;
import hu.d2.offsitesr.util.EnvironmentTool;

public class TicketListActivity extends AppCompatActivity implements  TicketList{

    public static int TICKET_REQUEST_CODE = 0;
    //public static int SCREEN_LOCK = 11;
    private TicketListPresenter presenter;
    private TicketListAdapter ticketListAdapter;
    private List<ServiceRequestEntity> ticketList;
    private String  syncDateString;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);
        Log.d("------------------>","Start Activity");
        ButterKnife.bind(this);

        chooseStatusDialog  = new ChooseStatusDialog();

        EnvironmentTool.setLanguage(this,SettingsSingleton.getInstance().getLanguage());
        setScreenLock();

        TimerSingleton.getInstance().setMyActivity(this);
        TimerSingleton.getInstance().timerStart();


        this.setupRecyclerView();

        presenter = new TicketListPresenterImpl();
        presenter.setView(this);

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

    @Override
    public void onBackPressed() {
        // alert - log out question
        android.app.FragmentManager fm = getFragmentManager();
        OnBackPressedDialog alertDialogFragment = new OnBackPressedDialog();
        alertDialogFragment.show(fm,"AlertDialogFragment");
    }



    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        TimerSingleton.getInstance().timerStop();
        TimerSingleton.getInstance().timerStart();
    }

    @Override
    protected void onPause() {
       super.onPause();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

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

    public String getLoggedInUser(){
        return SettingsSingleton.getInstance().getUserName();
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.act_ticket_list,menu);
        return super.onCreateOptionsMenu(menu);
    }

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
            case R.id.menuTicketList_about:
                this.launchAboutApp();
                return true;
            case R.id.menuTicketList_log_out:
                this.launchLogInView();
                return true;
            default:
                this.showErrorMessage(R.string.error_noOption);
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchLogInView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void launchSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void launchAboutApp(){
        Intent intent = new Intent(this,AboutAppActivity.class);
        startActivity(intent);
    }
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
