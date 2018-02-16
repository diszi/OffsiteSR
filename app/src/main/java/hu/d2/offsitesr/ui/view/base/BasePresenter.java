package hu.d2.offsitesr.ui.view.base;

import hu.d2.offsitesr.R;
import hu.d2.offsitesr.ui.view.ticketdetails.TicketDetails;
import hu.d2.offsitesr.ui.view.ticketlist.TicketList;
import hu.d2.offsitesr.util.UIThrowable;

/**
 * Created by szidonia.laszlo on 2018. 02. 12..
 */

public abstract class BasePresenter {

    public abstract void onNext();

    public void onError(UIThrowable throwable){
        int errorMessageCode = R.string.error_general;
        System.out.println(" THROWABLE ");

        if (throwable instanceof UIThrowable){
            UIThrowable uiThrowable = (UIThrowable) throwable;
            errorMessageCode = uiThrowable.getMessageId();
        }
    }

    public void onComplate(){

    }
}
