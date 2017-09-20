package hu.d2.offsitesr.ui.view.component;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.app.singleton.OwnerAndStatusSingleton;
import hu.d2.offsitesr.ui.view.ticketdetails.TicketDetailsActivity;

/**
 * Created by csabinko on 2017.09.19..
 */

public class AddWorkLogDialog extends DialogFragment {

    private TicketDetailsActivity view;

    @BindView(R.id.diagWrokLog_saveButton)
    Button saveButton;

    @BindView(R.id.diagWrokLog_cancelButton)
    Button cancelButton;

    @BindView(R.id.diagWrokLog_shortDesc)
    EditText shortDesc;

    @BindView(R.id.diagWrokLog_longDesc)
    EditText longDesc;

    @BindView(R.id.diagWrokLog_title)
    TextView title;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View contentView = inflater.inflate(R.layout.dialog_add_worklog, container, false);

        ButterKnife.bind(this, contentView);

        saveButton.setOnClickListener((v -> {
            String shortDescTxt = shortDesc.getText().toString();
            String longDescText = longDesc.getText().toString();


                view.addWorkLogRemote(shortDescTxt,longDescText);

                dismiss();


        }));

        cancelButton.setOnClickListener((view -> {
            dismiss();
        }));

        title.setText(getString(R.string.dialogWorkLog_title));
        return contentView;
    }

    public void setView(TicketDetailsActivity view) {
        this.view = view;

    }
}
