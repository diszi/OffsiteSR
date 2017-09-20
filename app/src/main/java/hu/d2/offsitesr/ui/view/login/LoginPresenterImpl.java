package hu.d2.offsitesr.ui.view.login;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import java.net.HttpURLConnection;

import hu.d2.offsitesr.R;
import hu.d2.offsitesr.util.NetworkTool;
import hu.d2.offsitesr.util.UIConstans;
import hu.d2.offsitesr.util.UIThrowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by csabinko on 2017.09.14..
 */

public class LoginPresenterImpl implements LoginPresenter {

	private Login view;
	private Disposable disposable;

	@Override
	public void setView(Login view) {
		this.view = view;
	}

	@Override
	public void login(String userName, String password) {
		disposable = createObservable(userName, password).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread()).doOnNext((param) -> {
					view.showLoading();
				}).subscribe((object) -> { // onNext Consumer

				}, (throwable) -> { // onError Consumer
					UIThrowable uiThrowable = (UIThrowable) throwable;
					view.showErrorMessage(uiThrowable.getMessageId());
					view.hideLoading();
				}, () -> { // onComplate Action
					view.setUserToContext(userName);
					view.launchListView();
					view.hideLoading();
				});
	}

	@Override
	public void onDestroy() {
		if (disposable != null && !disposable.isDisposed()) {
			disposable.dispose();
		}
	}

	@Override
	public Observable createObservable(String userName, String password) {
		Observable result = Observable.create(emitter -> {
			try {

				HttpURLConnection connection = null;
				try {
					connection = NetworkTool.createConnection(NetworkTool.LOGIN_URL);
					String credentials = userName + ":" + password;
					String base64Credentials = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
					connection.setRequestProperty("maxauth", base64Credentials);

					// connection.connect();

					int responseCode = connection.getResponseCode();
					if (responseCode == 200) {
						emitter.onComplete();
					} else if (responseCode == 400) {
						emitter.onError(new UIThrowable(R.string.error_wrongUser));
					} else {
						emitter.onError(new UIThrowable(R.string.error_authFailed));
					}

				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			} catch (Exception ex) {
				Log.e("", "---------->", ex);
				emitter.onError(new UIThrowable(R.string.error_unknown));
			}

		});

		return result;
	}
}
