package hu.d2.offsitesr.ui.view.login;

/**
 * Created by csabinko on 2017.09.14..
 */

public interface Login {
   // interface View{
        void showLoading();

        void hideLoading();

        void launchListView();

        void setUserToContext(String userName);
    //}



  //  interface Presenter extends BaseViewPresenter{
     //   void login(String userName, String password);
   // }
}
