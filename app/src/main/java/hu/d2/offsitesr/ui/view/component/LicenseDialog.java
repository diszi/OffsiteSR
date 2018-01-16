package hu.d2.offsitesr.ui.view.component;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import hu.d2.offsitesr.R;

/**
 * Created by szidonia.laszlo on 2017. 12. 11..
 *
 *  - this dialog will appear if license agreement expired for user
 *
 */

public class LicenseDialog extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity()).setMessage(R.string.dialogLicense_message).setNeutralButton(R.string.dialogLicense_buttonOk, new DialogInterface.OnClickListener() {
             @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).create();
    }
}
