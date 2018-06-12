package hu.d2.offsitesr.ui.view.login;

import  io.reactivex.Observable;

public interface LoginPresenter {

    void setView(Login view);

    void login(String userName, String password);

    void onDestroy();

   Observable createObservable(String userName, String password);
}
