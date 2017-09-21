package hu.d2.offsitesr.ui.view.ticketlist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.model.TicketHolder;
import hu.d2.offsitesr.ui.view.component.VerticalSpaceItemDecoration;
import hu.d2.offsitesr.ui.view.login.LoginActivity;
import hu.d2.offsitesr.ui.view.settings.SettingsActivity;
import hu.d2.offsitesr.ui.view.ticketdetails.TicketDetailsActivity;
import hu.d2.offsitesr.util.UIConstans;

public class TicketListActivity extends AppCompatActivity implements  TicketList{

    public static int TICKET_REQUEST_CODE = 0;

    private TicketListPresenter presenter;
    private TicketListAdapter ticketListAdapter;

    private List<ServiceRequestEntity> ticketList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);
        Log.d("------------------>"," Start Acitvity");
        ButterKnife.bind(this);

        this.setupRecyclerView();

        presenter = new TicketListPresenterImpl();
        presenter.setView(this);

        String username = getLoggedInUser();
        compUserName.setText(username);

        compSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                compSwipeRefreshLayout.setRefreshing(false);
                presenter.getTicketList();
            }
        });
        setSupportActionBar(compToolBar);

        presenter.getTicketList();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.ticketList!=null){
            ServiceRequestEntity ticket = this.ticketList.get(0);
        }

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

    @Override
    public String getLoggedInUser() {
        return PreferenceManager.getDefaultSharedPreferences(this).getString(UIConstans.LOGGED_IN_USER, "Unknown");
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
            case R.id.menuTicketList_log_out:
                PreferenceManager.getDefaultSharedPreferences(this).edit().remove(UIConstans.LOGGED_IN_USER).commit();
                this.launchLogInView();
                return true;
            default:
                this.showErrorMessage(R.string.error_noOption);
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchLogInView() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void launchSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
