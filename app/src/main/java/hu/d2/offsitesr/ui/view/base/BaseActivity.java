package hu.d2.offsitesr.ui.view.base;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import hu.d2.offsitesr.R;

/**
 * Created by szidonia.laszlo on 2018. 02. 12..
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    protected abstract BaseViewPresenter getBasePresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getBasePresenter().onDestroy();
    }

    public void showErrorMessage(int messageID) {
        Toast.makeText(this, messageID, Toast.LENGTH_SHORT).show();
    }


}
