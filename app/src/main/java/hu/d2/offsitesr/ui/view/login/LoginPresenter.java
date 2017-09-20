package hu.d2.offsitesr.ui.view.login;

import io.reactivex.Observable;

/**
 * Created by csabinko on 2017.09.14..
 */

public interface LoginPresenter {

    public void setView(Login view);

    public void login(String userName, String password);

    public void onDestroy();

    public Observable createObservable(String userName, String password);

}
