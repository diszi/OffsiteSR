package hu.d2.offsitesr.ui.view.verifications;

import java.text.ParseException;

import hu.d2.offsitesr.ui.model.Version;

/**
 * Created by szidonia.laszlo on 2018. 01. 29..
 *
 * This interface implemented in TicketListActivity and UpdateActivity.
 *
 */

public interface UpdateApp {

     void downloadApp();

     void verificUpdateInformations(Version updateVersionObj) throws ParseException;
}
