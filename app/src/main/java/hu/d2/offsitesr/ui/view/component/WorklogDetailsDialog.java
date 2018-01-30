package hu.d2.offsitesr.ui.view.component;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import hu.d2.offsitesr.R;
import hu.d2.offsitesr.ui.model.WorkLog;

/**
 * Created by szidonia.laszlo on 2017. 12. 04..
 *
 *   Dialog will show after  long onclick on worklog item
 *   Display long description
 */

public class WorklogDetailsDialog extends DialogFragment {

    public static String SERIALIZABLE_NAME = "WorkLog_Serializable";
    private WorkLog workLog;

    @BindView(R.id.diagWrokLog_cancelButton)
    Button cancelButton;
    @BindView(R.id.diagWorklog_title)
    TextView title;
    @BindView(R.id.diagWorklog_longDescription)
    TextView longDescription;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            workLog = (WorkLog)getArguments().getSerializable(WorklogDetailsDialog.SERIALIZABLE_NAME);
        }
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View contentView = inflater.inflate(R.layout.dialog_worklog_details, container, false);
        ButterKnife.bind(this, contentView);

        cancelButton.setOnClickListener((view -> {
            dismiss();
        }));

        title.setText(getString(R.string.dialogWorkLog_longDescription));
        longDescription.setText(workLog.getLongDescription());
        return contentView;
    }
}

