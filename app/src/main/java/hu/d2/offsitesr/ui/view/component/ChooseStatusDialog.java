package hu.d2.offsitesr.ui.view.component;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.ui.view.ticketdetails.TicketDetails;

/**
 * Created by csabinko on 2017.09.19..
 */

public class ChooseStatusDialog extends DialogFragment {

	public static String SERIALIZABLE_NAME = "List_Serializable";

	@BindView(R.id.diagStatus_saveButton)
	Button saveButton;

	@BindView(R.id.diagStatus_cancelButton)
	Button cancelButton;

	@BindView(R.id.diagStatus_radioGroup)
	RadioGroup radioGroup;

	@BindView(R.id.diagStatus_title)
	TextView title;

	private HashMap<String, String> stringMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			stringMap = (HashMap<String,String>) getArguments().getSerializable(ChooseStatusDialog.SERIALIZABLE_NAME);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

		final View contentView = inflater.inflate(R.layout.dialog_choose_status, container, false);

		ButterKnife.bind(this, contentView);

		saveButton.setOnClickListener((v -> {

			RadioButton radioButton = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
			if (radioButton != null) {
				String newData = radioButton.getText().toString();
				String sendData = newData.substring(0,newData.indexOf("-")-1);

				((TicketDetails)getActivity()).updateStatusRemote(sendData);

				dismiss();
			}

		}));

		cancelButton.setOnClickListener((view -> {
			dismiss();
		}));

//		stringMap = HolderSingleton.getInstance().getTicketStatuses();
		title.setText(getString(R.string.dialogSatus_title));

		stringMap.forEach((key, value) -> {
			RadioButton radioButton = new RadioButton(contentView.getContext());
			radioButton.setText(key +" - "+value);
			radioButton.setPadding(5, 25, 5, 25);

			radioGroup.addView(radioButton);
		});

		return contentView;
	}
}
