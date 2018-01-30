package hu.d2.offsitesr.ui.view.verifications;

import android.app.Activity;
import android.content.Context;

import java.util.List;

import hu.d2.offsitesr.ui.model.License;
import hu.d2.offsitesr.ui.model.Version;
import hu.d2.offsitesr.ui.view.ticketlist.TicketListActivity;
import io.reactivex.Observable;

/**
 * Created by szidonia.laszlo on 2017. 12. 08..
 */

public interface VerificationPresenter {

    void setLicenseView(LicenseActivity viewLicens);

    void setUpdateView(UpdateActivity viewUpdate);

//    void setTicketView(TicketListActivity viewTicket);

    void onDestroy();

    void validateLicense(String IMEInumber);

    void getUpdateVersion(String appName,Context contextActivity);

    void getNewApp(String appName, String newVersion,Activity activity);

    Observable<List<License>> createLicenseObservable(String IMEInumber);

    Observable<Version> createUpdateVersionObservable(String appname);

    Observable<Version> createDownloadNewAppObservable(String appName,String newVersion);

}
