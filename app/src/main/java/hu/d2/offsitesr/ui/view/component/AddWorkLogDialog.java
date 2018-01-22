package hu.d2.offsitesr.ui.view.component;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.ui.view.ticketdetails.TicketDetails;

/**
 * Created by csabinko on 2017.09.19..
 *  - dialog will appear after click on floating action button in worklog page
 */

public class AddWorkLogDialog extends DialogFragment {

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
            String shortDescText = shortDesc.getText().toString();
            String longDescText = longDesc.getText().toString();

            ((TicketDetails)getActivity()).addWorkLogRemote(shortDescText,longDescText);

            shortDesc.setText(null);
            longDesc.setText(null);
            dismiss();
        }));

        cancelButton.setOnClickListener((view -> {
            dismiss();
        }));

        title.setText(getString(R.string.dialogWorkLog_title));
        return contentView;
    }
}
