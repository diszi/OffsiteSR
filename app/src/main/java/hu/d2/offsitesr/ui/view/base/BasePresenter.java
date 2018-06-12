package hu.d2.offsitesr.ui.view.base;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import hu.d2.offsitesr.R;
import hu.d2.offsitesr.util.UIThrowable;
import io.reactivex.disposables.Disposable;

/**
 * Created by szidonia.laszlo on 2018. 02. 12..
 */

public class BasePresenter {

//    public abstract void onNext();

    private List<Disposable> disposableList = new ArrayList<>();

    public void disposeAll() {
        for (Disposable disposable: disposableList) {
            if (disposable != null && !disposable.isDisposed()) {
                Log.d("------------------>"," Dispose observer");
                disposable.dispose();
            }
        }

    }

    protected int getErrorMessage(Throwable throwable){
        int errorMessageCode = R.string.error_general;
        System.out.println(" THROWABLE ");

        if (throwable instanceof UIThrowable){
            UIThrowable uiThrowable = (UIThrowable) throwable;
            errorMessageCode = uiThrowable.getMessageId();
        }
        return errorMessageCode;
    }

    protected void addDisposableToList(Disposable disposable){
        disposableList.add(disposable);
    }

    public void onComplete(){

    }
}
