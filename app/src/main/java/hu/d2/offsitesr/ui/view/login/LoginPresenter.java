package hu.d2.offsitesr.ui.view.login;

import io.reactivex.Observable;

/**
 * Created by csabinko on 2017.09.14..
 */

public interface LoginPresenter {

     void setView(Login view);

     void login(String userName, String password);

     void onDestroy();

     Observable createObservable(String userName, String password);

}
