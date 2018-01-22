package hu.d2.offsitesr.ui.view.component;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;

import hu.d2.offsitesr.R;
import hu.d2.offsitesr.app.singleton.SettingsSingleton;


/**
 * Created by szidonia.laszlo on 2017. 11. 24..
 *
 *  - called from settings_preference.xml - dialog for reset user settings
 *
 */

public class QuestionDialogPreference extends DialogPreference {

    private SharedPreferences sharedPreferences;

    public QuestionDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        sharedPreferences = context.getSharedPreferences(SettingsSingleton.getInstance().getFileConstant(),Context.MODE_PRIVATE);

    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setTitle(null);
        builder.setMessage(R.string.SettingsResetQuestion);

        builder.setNegativeButton(R.string.SettingsResetNegativeButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        builder.setPositiveButton(R.string.SettingsResetPositiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final AlertDialog alertDialog = builder.create();
                alertDialog.setTitle(getContext().getString(R.string.SettingsResetTitle2));
                alertDialog.setMessage(getContext().getString(R.string.SettingsResetMessage));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getContext().getString(R.string.SettingsResetPositiveButton2), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SettingsSingleton.getInstance().resetSettings(getContext(),sharedPreferences);

                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getContext().getString(R.string.SettingsResetNegativeButton), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
    }
}
