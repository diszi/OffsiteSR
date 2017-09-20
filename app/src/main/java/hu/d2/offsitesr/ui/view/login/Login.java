package hu.d2.offsitesr.ui.view.login;

/**
 * Created by csabinko on 2017.09.14..
 */

public interface Login {

    void showLoading();

    void hideLoading();

    void showErrorMessage(int messageID);

    void launchListView();

    void setUserToContext(String userName);
}
