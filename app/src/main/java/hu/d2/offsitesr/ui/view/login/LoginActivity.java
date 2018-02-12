package hu.d2.offsitesr.ui.view.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import hu.d2.offsitesr.R;
import hu.d2.offsitesr.app.singleton.HolderSingleton;
import hu.d2.offsitesr.app.singleton.SettingsSingleton;
import hu.d2.offsitesr.app.singleton.TimerSingleton;
import hu.d2.offsitesr.ui.view.component.LicenseDialog;
import hu.d2.offsitesr.ui.view.verifications.UpdateActivity;
import hu.d2.offsitesr.util.EnvironmentTool;
import hu.d2.offsitesr.util.UIConstans;

/**
 * 	Login page
 * 	Access to the application requires authentication with the username and password.
 */
public class LoginActivity extends AppCompatActivity implements Login {


	public static Context mContext;
	private LoginPresenter presenter;

	@BindView(R.id.actLogin_Name)
	AutoCompleteTextView compLoginName;
	@BindView(R.id.actLogin_Password)
	EditText compLoginPassword;
	@BindView(R.id.actLogin_Button)
	Button compLoginButton;
	@BindView(R.id.actLogin_ProgressBar)
	ProgressBar compProgressBar;
	@BindView(R.id.version)
	TextView compVersion;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		this.mContext = getApplicationContext();

		ButterKnife.bind(this);

		presenter = new LoginPresenterImpl();
		presenter.setView(this);

		compVersion.setText(EnvironmentTool.getVersionApp());

	}

	/**
	 * Called when the activity has detected the user's press of the back key.
	 */
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		super.onBackPressed();
	}



	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		presenter.onDestroy();
	}

	/**
	 * 	Onclick on login button - check text fields - if the fields are
	 * 							completed and data are correct => entry
	 */
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
		Toast.makeText(this, messageID, Toast.LENGTH_SHORT).show();
	}


	@Override
	public void launchListView() {

		/**
		 * 	Deadline verification
		 * 	If EnvironmentTool.deadLineVerification() method return true => the deadline has not expired
		 * 												OR  return false => the deadline expired
		 */
		if (EnvironmentTool.deadLineVerification(EnvironmentTool.convertDate(new Date(),UIConstans.DATE_PATTERN_HU))){
			initApplication();
			Intent intent = new Intent(this, UpdateActivity.class);
			TimerSingleton.getInstance().initAndStartTimer(this);
			startActivity(intent);
		}else
		{
			android.app.FragmentManager fm = getFragmentManager();
			LicenseDialog alertDialogFragment = new LicenseDialog();
			alertDialogFragment.show(fm,"LicenseDialog");

		}

		/* License verification
		Intent intent = new Intent(this, LicenseActivity.class);
		startActivity(intent);*/
	}

	/**
	 * @param userName - logged username
	 * This method call init method: setting property file
	 */
    @Override
    public void setUserToContext(String userName) {
		SettingsSingleton.getInstance().init(this,userName);

	}


	//  App initialization
	private void initApplication(){
		HolderSingleton.getInstance().setContext(this);
	}


}
