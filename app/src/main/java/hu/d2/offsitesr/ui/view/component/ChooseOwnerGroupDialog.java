package hu.d2.offsitesr.ui.view.component;

import java.util.Map;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.app.singleton.OwnerAndStatusSingleton;
import hu.d2.offsitesr.ui.view.ticketdetails.TicketDetailsActivity;

/**
 * Created by csabinko on 2017.09.19..
 */

public class ChooseOwnerGroupDialog extends DialogFragment {

	private TicketDetailsActivity view;

	@BindView(R.id.diagOwnerGroup_saveButton)
	Button saveButton;

	@BindView(R.id.diagOwnerGroup_cancelButton)
	Button cancelButton;

	@BindView(R.id.diagOwnerGroup_radioGroup)
	RadioGroup radioGroup;

	@BindView(R.id.diagOwnerGroup_title)
	TextView title;

	private Map<String, String> stringMap;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

		final View contentView = inflater.inflate(R.layout.dialog_choose_owner_group, container, false);

		ButterKnife.bind(this, contentView);

		saveButton.setOnClickListener((v -> {

			RadioButton radioButton = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
			if (radioButton != null) {
				String newData = radioButton.getText().toString();

				view.updateOwnerGroupRemote(newData);

				dismiss();
			}

		}));

		cancelButton.setOnClickListener((view -> {
			dismiss();
		}));

		stringMap = OwnerAndStatusSingleton.getInstance().getOwnerGroups();
		title.setText(getString(R.string.dialogOwnerGroup_title));

		stringMap.forEach((key, value) -> {
			RadioButton radioButton = new RadioButton(contentView.getContext());
			radioButton.setText(value);
			radioButton.setPadding(5, 25, 5, 25);

			radioGroup.addView(radioButton);
		});

		return contentView;
	}

	public void setView(TicketDetailsActivity view) {
		this.view = view;
	}
}