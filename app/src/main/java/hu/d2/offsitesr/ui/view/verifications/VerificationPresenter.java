package hu.d2.offsitesr.ui.view.verifications;

import java.util.List;

import hu.d2.offsitesr.ui.model.License;

import hu.d2.offsitesr.ui.model.Version;
import io.reactivex.Observable;

/**
 * Created by szidonia.laszlo on 2017. 12. 08..
 */

public interface VerificationPresenter {

    public void setLicenseView(LicenseActivity viewLicens);

    public void setUpdateView(UpdateActivity viewUpdate);

    public void onDestroy();

    public void validateLicense(String IMEInumber);

    public void getUpdateVersion(String appName);

    public void getNewApp(String appName, String newVersion);

    public Observable<List<License>> createLicenseObservable(String IMEInumber);

    public Observable<Version> createUpdateVersionObservable(String appname);

    public Observable<Version> createDownloadNewAppObservable(String appName,String newVersion);

}
