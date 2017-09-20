package hu.d2.offsitesr.ui.view.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.app.singleton.OwnerAndStatusSingleton;
import hu.d2.offsitesr.ui.view.ticketlist.TicketListActivity;
import hu.d2.offsitesr.util.UIConstans;

public class LoginActivity extends AppCompatActivity implements Login {

	private LoginPresenter presenter;

	@BindView(R.id.actLogin_Name)
	AutoCompleteTextView compLoginName;
	@BindView(R.id.actLogin_Password)
	EditText compLoginPassword;
	@BindView(R.id.actLogin_Button)
	Button compLoginButton;
	@BindView(R.id.actLogin_ProgressBar)
	ProgressBar compProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		ButterKnife.bind(this);

		presenter = new LoginPresenterImpl();
		presenter.setView(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		presenter.onDestroy();
	}

	@OnClick(R.id.actLogin_Button)
	public void onClick() {
		compLoginButton.setEnabled(false);

		compLoginName.setError(null);
		compLoginPassword.setError(null);
		String loginName = compLoginName.getText().toString();
		String password = compLoginPassword.getText().toString();

		boolean cancel = false;
		View focusView = null;

		if (TextUtils.isEmpty(password)) {
			cancel = true;
			compLoginPassword.setError(getString(R.string.error_isRequired));
			focusView = compLoginPassword;
		}

		if (TextUtils.isEmpty(loginName)) {
			cancel = true;
			compLoginName.setError(getString(R.string.error_isRequired));
			focusView = compLoginName;
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			presenter.login(loginName, password);
		}
		compLoginButton.setEnabled(true);
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
		// Context context = this;
		//
		// Handler handler = new Handler(){
		// @Override
		// public void handleMessage(Message msg) {
		// Toast.makeText(context, messageID, Toast.LENGTH_SHORT).show();
		// }
		// };
		// Message message = handler.obtainMessage();
		// handler.sendMessage(message);

		Toast.makeText(this, messageID, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void launchListView() {
		initApplication();
		Intent intent = new Intent(this,TicketListActivity.class);
		startActivity(intent);

	}

    @Override
    public void setUserToContext(String userName) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(UIConstans.LOGGED_IN_USER, userName).commit();
    }

    private void initApplication(){
		List<String> statusList = Arrays.asList(getResources().getStringArray(R.array.statuses));
		OwnerAndStatusSingleton.getInstance().setStatuses(statusList);

	}
}
